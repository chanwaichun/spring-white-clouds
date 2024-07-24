package com.byx.pub.service.impl;

import cn.hutool.db.sql.Order;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.byx.pub.bean.xhs.*;
import com.byx.pub.enums.OrderStatusEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.XiaoHongShuFinal;
import com.byx.pub.exception.ApiException;
import com.byx.pub.filter.RedisLock;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.BusinessUserService;
import com.byx.pub.service.XiaoHongShuService;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.HttpUtils;
import com.byx.pub.util.MD5;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Jump
 * @date 2023/12/20 15:49:07
 */
@Slf4j
@Service
public class XiaoHongShuServiceImpl implements XiaoHongShuService {
    @Resource
    XhsAuthTokenPlusDao xhsAuthTokenPlusDao;
    @Resource
    ProductPlusDao productPlusDao;
    @Value("${WeChat.notifyUrl}")
    private String notifyUrl;
    @Resource
    AdminPlusDao adminPlusDao;
    @Resource
    OrdersPlusDao ordersPlusDao;
    @Resource
    OrderDetailPlusDao detailPlusDao;
    @Resource
    UserPlusDao userPlusDao;
    @Resource
    BusinessUserService businessUserService;
    @Resource
    XhsAdminProductPlusDao xhsAdminProductPlusDao;

    /**
     * 解密数据
     * @param xhsToken
     * @param orderId
     * @param resVo
     * @return
     */
    public OrderReceiverInfoVo dataDecrypt(XhsAuthToken xhsToken,String orderId,OrderReceiverInfoVo resVo){
        Map<String,Object> decryptMap = new HashMap<>(10);
        decryptMap.put("accessToken",xhsToken.getAccessToken());
        List<BaseInfoBo> baseInfoBoList = new ArrayList<>(3);
        BaseInfoBo infoBo1 = new BaseInfoBo();
        infoBo1.setDataTag(orderId);
        infoBo1.setEncryptedData(resVo.getReceiverName());
        baseInfoBoList.add(infoBo1);
        BaseInfoBo infoBo2 = new BaseInfoBo();
        infoBo2.setDataTag(orderId);
        infoBo2.setEncryptedData(resVo.getReceiverPhone());
        baseInfoBoList.add(infoBo2);
        BaseInfoBo infoBo3 = new BaseInfoBo();
        infoBo3.setDataTag(orderId);
        infoBo3.setEncryptedData(resVo.getReceiverAddress());
        baseInfoBoList.add(infoBo3);
        decryptMap.put("baseInfos",baseInfoBoList);
        decryptMap.put("actionType","1");
        decryptMap.put("appUserId",xhsToken.getId());
        String decryptStr = postXhs(decryptMap, XiaoHongShuFinal.batchDecrypt, true);
        if(StringUtil.isEmpty(decryptStr)){
            log.info("----------解密参数失败----------");
            return null;
        }
        Map<String, Object> resDataMap =  StringUtil.stringToMap(decryptStr);
        List<DecryptedReceiverVo> dataInfoList = JSONArray.parseArray(resDataMap.get("dataInfoList").toString(), DecryptedReceiverVo.class);
        if(CollectionUtils.isEmpty(dataInfoList)){
            log.info("----------解密参数失败----------");
            return null;
        }
        for(DecryptedReceiverVo vo : dataInfoList){
            if(!"0".equals(vo.getErrorCode())){
                log.info("----------解密参数失败----------");
                return null;
            }
            if(vo.getEncryptedData().equals(resVo.getReceiverName())){
                resVo.setReceiverName(vo.getDecryptedData());
            }
            if(vo.getEncryptedData().equals(resVo.getReceiverPhone())){
                resVo.setReceiverPhone(vo.getDecryptedData());
            }
            if(vo.getEncryptedData().equals(resVo.getReceiverAddress())){
                resVo.setReceiverAddress(vo.getDecryptedData());
            }
        }
        return resVo;
    }

    /**
     * 每25分钟更新一次小红书订单状态
     */
    @RedisLock(constantKey = "synXhsChangeOrders",prefix ="byx:scheduled:xhs:order",isWaitForLock = false)
    public void synChangeOrder(){
        log.info("----------开始定时更新小红书订单列表----------");
        //1.查询有小红书店铺的咨询师
        List<Admin> adminList = adminPlusDao.lambdaQuery().eq(Admin::getDataStatus, true)
                .eq(Admin::getUserStatus, true).ne(Admin::getXhsSellerId, "").list();
        if(CollectionUtils.isEmpty(adminList)){
            log.info("---------获取小红书更新订单列表，没有绑定店铺的咨询师而退出----");
            return;
        }
        //2.查询咨询师小红书店铺过去半小时有更新的订单
        for(Admin admin : adminList){
            log.info("---------开始更新咨询师:{}的订单----",admin.getTrueName());
            List<XhsOrderVo> orderList = getUpdateOrderList(admin.getXhsSellerId());
            if(CollectionUtils.isEmpty(orderList)){
                log.info("---------咨询师:{}小红书店铺过去半小时没有更新订单而跳过----",admin.getTrueName());
                continue;
            }
            //3.更新订单状态
            List<Orders> changeOrders = new ArrayList<>();
            for(XhsOrderVo orderVo : orderList){
                Orders orders = this.ordersPlusDao.lambdaQuery().eq(Orders::getXhsOrderId, orderVo.getOrderId()).last("limit 1").one();
                if(Objects.isNull(orders)){
                    log.info("---------未找到对应的订单：{}而跳过----",orderVo.getOrderId());
                    continue;
                }
                Orders changeOrder = new Orders();
                changeOrder.setId(orders.getId());
                //映射订单状态：订单状态(0->已取消，1->待支付，4->已完成)
                //小红书：1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中
                if(orderVo.getOrderStatus() == 1){
                    changeOrder.setStatus(OrderStatusEnum.NOT_PAY.getCode());
                }
                //已支付的订单(有收货人信息)
                if(orderVo.getOrderStatus() >= 2 && orderVo.getOrderStatus() !=8 && orderVo.getOrderStatus() !=9){
                    changeOrder.setStatus(OrderStatusEnum.ORDER_SUCCESS.getCode());
                    changeOrder.setPaymentTime(new Date(orderVo.getPaidTime()));
                    //4.查询收件人信息(需要已支付)
                    OrderReceiverInfoVo receiverInfo = getReceiverInfo(orderVo.getOrderId(), admin.getXhsSellerId(), orders.getXhsAddressId());
                    if(Objects.isNull(receiverInfo) || StringUtil.isEmpty(receiverInfo.getReceiverPhone())){
                        log.info("------未找到订单收件信息而跳过，xshOrderId:{}",orderVo.getOrderId());
                        continue;
                    }
                    //5.保存会员信息\订单下单人信息
                    User user = checkAndAddMember(admin, receiverInfo);
                    changeOrder.setUserId(user.getId()).setUserName(user.getNickName());
                    changeOrder.setUserMobile(user.getMobile()).setShippingAddress(receiverInfo.getReceiverAddress());
                    changeOrder.setShippingPhone(receiverInfo.getReceiverPhone()).setShippingName(receiverInfo.getReceiverName());
                }
                if(orderVo.getOrderStatus() == 8 || orderVo.getOrderStatus() == 9){
                    changeOrder.setStatus(OrderStatusEnum.CLOSED.getCode());
                }
                changeOrders.add(changeOrder);
            }
            if(CollectionUtils.isNotEmpty(changeOrders)){
                this.ordersPlusDao.updateBatchById(changeOrders);
            }
            log.info("---------咨询师:{}的订单更新完毕----",admin.getTrueName());
        }
        log.info("----------定时更新小红书订单列表完成----------");
    }

    /**
     * 获取过去半小时订单状态变换的订单
     * @param sellerId
     * @return
     */
    public List<XhsOrderVo> getUpdateOrderList(String sellerId){
        //获取30分前的数据
        long start = DateUtil.get30MinuteSeconds();
        return getOrderList(sellerId,start,2);
    }

    /**
     * 每小时拉取最近一小时的新增订单
     */
    @RedisLock(constantKey = "synXhsAddOrders",prefix ="byx:scheduled:xhs:order",isWaitForLock = false)
    public void synAddOrder(){
        log.info("----------开始定时获取小红书订单列表----------");
        //1.查询有小红书店铺的咨询师
        List<Admin> adminList = adminPlusDao.lambdaQuery().eq(Admin::getDataStatus, true)
                .eq(Admin::getUserStatus, true).ne(Admin::getXhsSellerId, "").list();
        if(CollectionUtils.isEmpty(adminList)){
            log.info("---------获取小红书订单列表，没有绑定店铺的咨询师而退出----");
            return;
        }
        //2.循环获取咨询师订单列表
        for(Admin admin : adminList){
            List<XhsOrderVo> orderList = this.getAddOrderList(admin.getXhsSellerId());
            if(CollectionUtils.isEmpty(orderList)){
                log.info("---------咨询师:{}小红书店铺过去1小时没有新增订单而跳过----",admin.getTrueName());
                continue;
            }
            //3.插入订单表
            log.info("---------开始同步咨询师:{}的订单----",admin.getTrueName());
            addXhsOrder(admin,orderList);
            log.info("---------同步咨询师:{}的订单完成----",admin.getTrueName());
        }
    }

    /**
     * 保存咨询师小红书订单
     * @param admin
     * @param orderList
     */
    public void addXhsOrder(Admin admin,List<XhsOrderVo> orderList){
        List<Orders> addOrders = new ArrayList<>(orderList.size());
        List<OrderDetail> addDetails = new ArrayList<>(orderList.size());
        for(XhsOrderVo vo : orderList){
            //1.校验
            Orders order = this.ordersPlusDao.lambdaQuery().eq(Orders::getXhsOrderId, vo.getOrderId()).last("limit 1").one();
            if(Objects.nonNull(order)){
                log.info("------重复订单而跳过，xshOrderId:{}",vo.getOrderId());
                continue;
            }
            //2.查询订单详情
            XhsOrderDetailVo orderDetail = this.getOrderDetail(vo.getOrderId(), admin.getXhsSellerId());
            if(Objects.isNull(orderDetail)){
                log.info("------未找到订单详情而跳过，xshOrderId:{}",vo.getOrderId());
                continue;
            }
            List<XhsOrderSkuVo> skuList = orderDetail.getSkuList();
            if(CollectionUtils.isEmpty(skuList)){
                log.info("------未找到订单sku信息而跳过，xshOrderId:{}",vo.getOrderId());
                continue;
            }
            XhsOrderSkuVo skuVo = skuList.get(0);
            //3.查询商品是否我们同步的
            XhsAdminProduct xhsAdminProduct = this.xhsAdminProductPlusDao.lambdaQuery().eq(XhsAdminProduct::getAdminId, admin.getAdminId())
                    .eq(XhsAdminProduct::getXhsSkuId, skuVo.getSkuId()).last("limit 1").one();
            if(Objects.isNull(xhsAdminProduct)){
                log.info("------订单sku不是系统同步过去的商品而跳过，xshOrderId:{}",vo.getOrderId());
                continue;
            }
            Product product = this.productPlusDao.getById(xhsAdminProduct.getProductId());
            if(Objects.isNull(product)){
                log.info("------订单skuId对应系统商品未找到而跳过，xshSkuId:{}",skuVo.getSkuId());
                continue;
            }
            //已支付订单
            User user = null;
            OrderReceiverInfoVo receiverInfo = null;
            if(vo.getOrderStatus() >= 2 && vo.getOrderStatus() !=8 && vo.getOrderStatus() !=9){
                //4.查询收件人信息(需要已支付)
                receiverInfo =  getReceiverInfo(vo.getOrderId(), admin.getXhsSellerId(), orderDetail.getOpenAddressId());
                if(Objects.isNull(receiverInfo) || StringUtil.isEmpty(receiverInfo.getReceiverPhone())){
                    log.info("------未找到订单收件信息而跳过，xshOrderId:{}",vo.getOrderId());
                    continue;
                }
                //5.保存会员信息
                user = checkAndAddMember(admin, receiverInfo);
            }
            //6.插入订单信息
            Orders addOrder = new Orders();
            String orderId = IdWorker.getIdStr();
            addOrder.setId(orderId).setOrderSn("XHS"+vo.getOrderId());
            addOrder.setXhsOrderId(vo.getOrderId());
            addOrder.setOrderAmount(StringUtil.centToYuan(orderDetail.getTotalPayAmount()));
            //映射订单状态：订单状态(0->已取消，1->待支付，4->已完成)
            //小红书：1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中
            if(vo.getOrderStatus() == 1){
                addOrder.setStatus(OrderStatusEnum.NOT_PAY.getCode());
            }
            if(vo.getOrderStatus() >= 2 && vo.getOrderStatus() !=8 && vo.getOrderStatus() !=9){
                addOrder.setStatus(OrderStatusEnum.ORDER_SUCCESS.getCode());
                addOrder.setPaidAmount(addOrder.getOrderAmount());
                addOrder.setPaymentTime(new Date(vo.getPaidTime()));
            }
            if(vo.getOrderStatus() == 8 || vo.getOrderStatus() == 9){
                addOrder.setStatus(OrderStatusEnum.CLOSED.getCode());
            }
            addOrder.setCreateTime(new Date(vo.getCreatedTime()));
            addOrder.setUpdateTime(new Date(vo.getUpdateTime()));
            addOrder.setAdminId(admin.getAdminId()).setAdminMobile(admin.getMobile());
            addOrder.setAdminName(admin.getTrueName()).setBusinessId(admin.getBusinessId());
            addOrder.setXhsAddressId(orderDetail.getOpenAddressId());
            if(Objects.nonNull(user)){
                addOrder.setUserId(user.getId()).setUserName(user.getNickName());
                addOrder.setUserMobile(user.getMobile()).setShippingAddress(receiverInfo.getReceiverAddress());
                addOrder.setShippingPhone(receiverInfo.getReceiverPhone()).setShippingName(receiverInfo.getReceiverName());
            }
            addOrders.add(addOrder);
            //订单详情
            OrderDetail addDetail = new OrderDetail();
            addDetail.setOrderSn(addOrder.getOrderSn());
            addDetail.setProductId(product.getId()).setProductName(product.getProductName());
            addDetail.setPrice(product.getPrice());
            addDetail.setQuantity(skuVo.getSkuQuantity()).setImg(product.getImg());
            addDetails.add(addDetail);
        }
        //保存数据
        if(CollectionUtils.isNotEmpty(addOrders)){
            this.ordersPlusDao.saveBatch(addOrders);
        }
        if(CollectionUtils.isNotEmpty(addDetails)){
            this.detailPlusDao.saveBatch(addDetails);
        }
    }

    /**
     * 校验并保存用户信息
     * @param admin
     * @param receiverInfo
     */
    public User checkAndAddMember(Admin admin,OrderReceiverInfoVo receiverInfo){
        //校验user表是否存在
        User user = this.userPlusDao.lambdaQuery()
                .eq(User::getDataStatus, true).eq(User::getUserStatus, true)
                .eq(User::getMobile, receiverInfo.getReceiverPhone()).last("limit 1").one();
        //如果存在用户判断是否加入会员
        if(Objects.nonNull(user)){
            businessUserService.addSjMember(admin.getAdminId(),admin.getBusinessId(),user.getId(),4,true);
            return user;
        }
        //不存在 则需要 增加user 表 和会员表
        User addUser = new User();
        addUser.setUserStatus(true).setMobile(receiverInfo.getReceiverPhone())
                .setNickName(receiverInfo.getReceiverName()).setRoleId(0)
                .setShippingAddress(receiverInfo.getReceiverProvinceName()+receiverInfo.getReceiverCityName()
                        +receiverInfo.getReceiverDistrictName()+receiverInfo.getReceiverTownName()
                        +receiverInfo.getReceiverAddress());
        this.userPlusDao.save(addUser);
        businessUserService.addSjMember(admin.getAdminId(),admin.getBusinessId(),addUser.getId(),4,true);
        return addUser;
    }

    /**
     * 获取订单收件人信息
     * @param orderId
     * @param sellerId
     * @param openAddressId
     * @return
     */
    public OrderReceiverInfoVo getReceiverInfo(String orderId,String sellerId,String openAddressId){
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            return null;
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("isReturn",false);
        Map<String,Object> queriesMap = new HashMap<>();
        queriesMap.put("orderId",orderId);
        queriesMap.put("openAddressId",openAddressId);
        paramsMap.put("receiverQueries",queriesMap);
        String resStr = postXhs(paramsMap, XiaoHongShuFinal.orderReceiver, true);
        if(StringUtil.isEmpty(resStr)){
            return null;
        }
        Map<String, Object> resMap =  StringUtil.stringToMap(resStr);
        List<OrderReceiverInfoVo> receiverInfos = JSONArray.parseArray(resMap.get("receiverInfos").toString(), OrderReceiverInfoVo.class);
        if(CollectionUtils.isEmpty(receiverInfos)){
            log.info("----------未获取收件人信息----------");
            return null;
        }
        //解密参数
        return dataDecrypt(xhsToken,orderId,receiverInfos.get(0));
    }


    /**
     * 获取订单详情
     * @param orderId
     * @param sellerId
     * @return
     */
    public XhsOrderDetailVo getOrderDetail(String orderId,String sellerId){
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            return null;
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("orderId",orderId);
        String resStr = postXhs(paramsMap, XiaoHongShuFinal.orderDetail,true);
        if(StringUtil.isEmpty(resStr)){
            return null;
        }
        XhsOrderDetailVo xhsOrderDetailVo = JSON.parseObject(resStr, XhsOrderDetailVo.class);
        Map<String, Object> resMap =  StringUtil.stringToMap(resStr);
        String skuListStr = resMap.get("skuList").toString();
        List<XhsOrderSkuVo> skuVoList = JSONArray.parseArray(skuListStr, XhsOrderSkuVo.class);
        xhsOrderDetailVo.setSkuList(skuVoList);
        return xhsOrderDetailVo;
    }

    /**
     * 获取一小时前新增订单
     * @param sellerId
     * @return
     */
    public List<XhsOrderVo> getAddOrderList(String sellerId){
        //获取1小时5分前的数据
        long start = DateUtil.get1Hour5MinuteSeconds();
        return getOrderList(sellerId,start,1);
    }

    /**
     * 获取小红书订单列表
     * @return
     */public List<XhsOrderVo> getOrderList(String sellerId,long start,Integer getType)
    {
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            return new ArrayList<>();
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("timeType",getType);
        paramsMap.put("startTime",start);
        long end = new Date().getTime() / 1000;
        paramsMap.put("endTime",end);
        paramsMap.put("pageNo",1);
        paramsMap.put("pageSize",100);
        String orderStr = postXhs(paramsMap, XiaoHongShuFinal.orderList,true);
        if(StringUtil.isEmpty(orderStr)){
            return new ArrayList<>();
        }
        Map<String, Object> resMap =  StringUtil.stringToMap(orderStr);
        int total = Integer.parseInt(resMap.get("total").toString());
        if(0 == total){
            return new ArrayList<>();
        }
        return JSONArray.parseArray(resMap.get("orderList").toString(), XhsOrderVo.class);
    }


    /**
     * 创建商品spu
     * @param product
     * @param admin
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String createItem(Product product, Admin admin){
        //获取token信息
        XhsAuthToken xhsToken = getXhsToken(admin.getXhsSellerId());
        if(Objects.isNull(xhsToken)){
            return "小红书店铺未授权";
        }
        //店铺物流方案
        String planInfoId = getLogisticsList(xhsToken.getAccessToken());
        if(StringUtil.isEmpty(planInfoId)){
            return "未配置小红书店铺物流方案";
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("name",product.getProductName());
        //品牌id(亲心)
        paramsMap.put("brandId",203674);
        //末级分类id(健康养生)
        paramsMap.put("categoryId","63fe279e203c5000011b6e17");
        //属性
        /*Map<String,Object> attributesMap = new HashMap<>();
        attributesMap.put("propertyId","5d557a5e69bd8940801359c7");
        attributesMap.put("name","授课老师");
        attributesMap.put("valueId","5a60c48769bd891ed893d036");
        attributesMap.put("value","小清新");
        paramsMap.put("attributes",attributesMap);
        paramsMap.put("attributes","");*/
        //模版id(默认取第一个)
        String templateId = getCarriageTemplate(xhsToken.getAccessToken());
        if(StringUtil.isEmpty(templateId)){
            return "未配置小红书店铺运费模板";
        }
        paramsMap.put("shippingTemplateId",templateId);
        //物流模式,0：普通，1：支持无物流发货（限定类目支持，不支持的类目创建会报错）
        paramsMap.put("deliveryMode",1);
        //上传素材(像素要求图片像素不低于800*800px（1:1比例）或  像素不低于750*1000px（3:4比例）)
        List<String> images = new ArrayList<>();
        String img = uploadMaterial(product.getImg(),xhsToken.getAccessToken());
        if(StringUtil.isEmpty(img)){
            return "上传商品图片素材失败";
        }
        images.add(img);
        paramsMap.put("images",images);
        paramsMap.put("imageDescriptions",images);
        paramsMap.put("description","认真做好服务，追求切实效果");
        String resStr = postXhsRes(paramsMap, XiaoHongShuFinal.createItem);
        if(StringUtil.isEmpty(resStr)){
            return "创建item失败";
        }
        Map<String, Object> itemMap =  StringUtil.stringToMap(resStr);
        String success = itemMap.get("success").toString();
        String errorCode = itemMap.get("error_code").toString();
        if(!"0".equals(errorCode) || "false".equals(success)){
            return itemMap.get("error_msg").toString();
        }
        Map<String, Object> resMap =  StringUtil.stringToMap(itemMap.get("data").toString());
        String itemId = resMap.get("id").toString();
        //创建sku
        Map<String,Object> skuParamsMapMap = new HashMap<>();
        skuParamsMapMap.put("accessToken",xhsToken.getAccessToken());
        skuParamsMapMap.put("itemId",itemId);
        //单价（分）
        int price = product.getPrice().multiply(BigDecimal.valueOf(100)).intValue();
        skuParamsMapMap.put("price",price);
        //库存
        skuParamsMapMap.put("stock",product.getServiceNum());
        //物流方案
        skuParamsMapMap.put("logisticsPlanId",planInfoId);
        //发货时间
        Map<String,Object> deliveryTimeMap = new HashMap<>();
        deliveryTimeMap.put("type","TODAY");
        deliveryTimeMap.put("time","");
        skuParamsMapMap.put("deliveryTime",deliveryTimeMap);
        String skuResStr = postXhsRes(skuParamsMapMap, XiaoHongShuFinal.createSku);
        if(StringUtil.isEmpty(skuResStr)){
            return "创建sku失败";
        }
        Map<String, Object> skuMap =  StringUtil.stringToMap(skuResStr);
        String skuSuccess= skuMap.get("success").toString();
        String skuErrorCode = skuMap.get("error_code").toString();
        if(!"0".equals(skuErrorCode) || "false".equals(skuSuccess)){
            return skuMap.get("error_msg").toString();
        }
        Map<String, Object> skuSuccessMap =  StringUtil.stringToMap(skuMap.get("data").toString());
        String skuId = skuSuccessMap.get("id").toString();
        //修改商品为已推送
        this.productPlusDao.updateById(new Product().setId(product.getId()).setXhsSkuStatus(true));
        //插入推送商品信息
        this.xhsAdminProductPlusDao.save(new XhsAdminProduct().setAdminId(admin.getAdminId()).setBid(admin.getBusinessId())
                        .setProductId(product.getId()).setXhsSellerId(xhsToken.getSellerId()).setXhsSkuId(skuId));
        return "success";
    }


    /**
     * 上下架sku
     * @param productId
     * @param xhsToken
     * @param status
     * @return

    public String availableSku(String productId,String xhsToken,Boolean status){
        Product product = this.productPlusDao.getById(productId);
        if(Objects.isNull(product) || !product.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到商品信息");
        }
        if(StringUtil.isEmpty(product.getXhsSkuId())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"当前商品还未同步至小红书");
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken);
        paramsMap.put("skuId",product.getXhsSkuId());
        paramsMap.put("available",status);
        return postXhs(paramsMap, XiaoHongShuFinal.skuAvailable,false);
    }
     */

    /**
     * 创建sku
     * @param product
     * @param xhsToken
     * @param itemId
     * @param planInfoId
     * @return

    public String createSku(Product product,String xhsToken,String itemId,String planInfoId){
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken);
        paramsMap.put("itemId",itemId);
        //单价（分）
        int price = product.getPrice().multiply(BigDecimal.valueOf(100)).intValue();
        paramsMap.put("price",price);
        //库存
        paramsMap.put("stock",product.getServiceNum());
        //物流方案
        paramsMap.put("logisticsPlanId",planInfoId);
        //发货时间
        Map<String,Object> deliveryTimeMap = new HashMap<>();
        deliveryTimeMap.put("type","TODAY");
        deliveryTimeMap.put("time","");
        paramsMap.put("deliveryTime",deliveryTimeMap);
        return postXhs(paramsMap, XiaoHongShuFinal.createSku,true);
    }*/

    /**
     * 获取物流方案列表
     * @param xhsToken
     * @return
     */
    public String getLogisticsList(String xhsToken){
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken);
        String resStr = postXhs(paramsMap, XiaoHongShuFinal.logisticsList, false);
        Map<String, Object> resMap =  StringUtil.stringToMap(resStr);
        List<XhsLogisticsVo> list = JSONArray.parseArray(resMap.get("logisticsPlans").toString(), XhsLogisticsVo.class);
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0).getPlanInfoId();
        }
        return "";
    }

    /**
     * 上传素材
     * @param img
     * @return
     */
    public String uploadMaterial(String img,String xhsToken){
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken);
        paramsMap.put("name",img);
        paramsMap.put("type","IMAGE");
        //拼接图片地址 转字节数组 并 base64 转string
        String imgUrl = getServiceImgUrl()+"/" + img;
        try {
            byte[] bytes = StringUtil.downloadImage(imgUrl);
            paramsMap.put("materialContent",Base64.getEncoder().encodeToString(bytes));
        } catch (IOException e) {
            return "";
        }
        String dataStr = postXhs(paramsMap, XiaoHongShuFinal.upMaterial,true);
        if(StringUtil.isEmpty(dataStr)){
            return "";
        }
        Map<String, Object> dataMap =  StringUtil.stringToMap(dataStr);
        return dataMap.get("url").toString();
    }

    /**
     * 获取运费模板列表
     * @return
     */
    public String getCarriageTemplate(String xhsToken){
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken);
        paramsMap.put("pageIndex",1);
        paramsMap.put("pageSize",10);
        String resStr = postXhs(paramsMap, XiaoHongShuFinal.carriageTemplateList, false);
        Map<String, Object> resMap =  StringUtil.stringToMap(resStr);
        List<XhsCarriageTemplateVo> list = JSONArray.parseArray(resMap.get("carriageTemplateList").toString(), XhsCarriageTemplateVo.class);
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0).getTemplateId();
        }
        return "";
    }

    /**
     * 获取属性值列表
     * @param attributeId
     * @return
     */
    public String getAttributeValueList(String attributeId,String sellerId){
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            throw new ApiException("小红书店铺未授权");
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("attributeId",attributeId);
        return postXhs(paramsMap, XiaoHongShuFinal.attributeValueList,false);
    }

    /**
     * 获取末级类目下的属性列表
     * @param categoryId
     * @return
     */
    public String getCategoryAttributeList(String categoryId,String sellerId){
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            throw new ApiException("小红书店铺未授权");
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("categoryId",categoryId);
        return postXhs(paramsMap, XiaoHongShuFinal.categoryAttributeList,false);
    }

    /**
     * 获取末级类目下的品牌
     * @param categoryId
     * @return
     */
    public String getCategoryBrand(String categoryId,String sellerId){
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            throw new ApiException("小红书店铺未授权");
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        paramsMap.put("categoryId",categoryId);
        paramsMap.put("keyword","亲心");
        paramsMap.put("pageNo",1);
        paramsMap.put("pageSize",20);
        return postXhs(paramsMap, XiaoHongShuFinal.categoryBrandList,false);
    }

    /**
     * 获取小红书类目信息
     * @return
     */
    public String getCategories(String categoryId,String sellerId){
        XhsAuthToken xhsToken = getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            throw new ApiException("小红书店铺未授权");
        }
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("accessToken",xhsToken.getAccessToken());
        if(!StringUtil.isEmpty(categoryId)){
            paramsMap.put("categoryId",categoryId);
        }
        return postXhs(paramsMap, XiaoHongShuFinal.categoryList,false);
    }

    /**
     * 通过code换token
     * @param code
     * @return
     */
    public void code2AccessToken(String code){
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("code",code);
        String dataStr = postXhs(paramsMap, XiaoHongShuFinal.code2AccessTokenMethod,true);
        if(StringUtil.isEmpty(dataStr)){
            return;
        }
        //处理结果
        saveToken(dataStr);
    }

    /**
     * 刷新店铺token
     * @param authToken
     */
    public void refreshToken(XhsAuthToken authToken){
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("refreshToken",authToken.getRefreshToken());
        //请求小红书
        String dataStr = postXhs(paramsMap,XiaoHongShuFinal.refreshTokenMethod,true);
        if(StringUtil.isEmpty(dataStr)){
            return;
        }
        //修改token
        saveToken(dataStr);
        log.info("------------小红书刷新店铺[{}]token完成",authToken.getSellerName());
    }


    /**
     * 请求小红书(自己处理结果)
     * @param paramsMap
     * @param method
     * @return
     */
    public String postXhsRes(Map<String,Object> paramsMap,String method){
        String timestamp = System.currentTimeMillis()+"";
        paramsMap.put("appId",XiaoHongShuFinal.appKey);
        paramsMap.put("version",XiaoHongShuFinal.version);
        paramsMap.put("sign",getSing(timestamp,method));
        paramsMap.put("timestamp",timestamp);
        paramsMap.put("method",method);
        //请求小红书
        return HttpUtils.requestPostBody(XiaoHongShuFinal.xhs_Url, JSONObject.toJSONString(paramsMap), null);
    }

    /**
     * 请求小红书
     * @param paramsMap
     * @param method
     * @return
     */
    public String postXhs(Map<String,Object> paramsMap,String method,Boolean taskStatus){
        String resStr = postXhsRes(paramsMap,method);
        if(StringUtil.isEmpty(resStr)){
            if(taskStatus){
                return "";
            }else{
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请求小红书失败");
            }
        }
        Map<String, Object> resMap =  StringUtil.stringToMap(resStr);
        String success = resMap.get("success").toString();
        String errorCode = resMap.get("error_code").toString();
        if(!"true".equals(success) && !"0".equals(errorCode)){
            log.info("------------请求小红书接口[{}]失败，请求：{},返回:{}",method,JSONObject.toJSONString(paramsMap),resStr);
            if(taskStatus){
                return "";
            }else{
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请求小红书失败:"+resStr);
            }
        }
        return resMap.get("data").toString();
    }

    /**
     * 签名计算
     * 系统参数有：appId,timestamp,version,method
     * 1.拼接如：product.createItem?appId=21d6***748be8de0&timestamp=1612518379&version=2.0429a***a3aee9ef9e4a858210
     * 2.MD5如：aa4c59****50632d80dc4
     * @param timestamp
     * @param method
     * @return
     */
    public String getSing(String timestamp,String method){
        String resStr = method+"?appId="+ XiaoHongShuFinal.appKey+"&timestamp="
                +timestamp+"&version="+XiaoHongShuFinal.version+XiaoHongShuFinal.appSecret;
        return MD5.md5(resStr);
    }

    /**
     * 获取图片地址
     * @return
     */
    public String getServiceImgUrl(){
        return notifyUrl.substring(0,notifyUrl.indexOf("white/clouds")) + "productUploadImg";
    }

    /**
     * 获取店铺token
     * @param sellerId
     * @return
     */
    public XhsAuthToken getXhsToken(String sellerId){
        return xhsAuthTokenPlusDao.lambdaQuery().eq(XhsAuthToken::getSellerId, sellerId).last("limit 1").one();
    }

    /**
     * 保存授权店铺token信息
     * @param dataStr
     */
    public void saveToken(String dataStr){
        Map<String, Object> dataMap =  StringUtil.stringToMap(dataStr);
        String sellerId = dataMap.get("sellerId").toString();
        //先删除店铺原有token数据
        LambdaQueryWrapper<XhsAuthToken> delWhere = new LambdaQueryWrapper<>();
        delWhere.eq(XhsAuthToken::getSellerId,sellerId);
        this.xhsAuthTokenPlusDao.remove(delWhere);
        //再插入
        XhsAuthToken addToken = new XhsAuthToken();
        addToken.setSellerId(sellerId);
        addToken.setSellerName(dataMap.get("sellerName").toString());
        addToken.setAccessToken(dataMap.get("accessToken").toString());
        addToken.setTokenExpires(dataMap.get("accessTokenExpiresAt").toString());
        addToken.setRefreshToken(dataMap.get("refreshToken").toString());
        addToken.setRefreshExpires(dataMap.get("refreshTokenExpiresAt").toString());
        this.xhsAuthTokenPlusDao.save(addToken);
    }

    /**
     * 定时检查并刷新小红书token
     * 1)accessToken有效期为7天，refreshToken有效时间为14天
     * 2)accessToken未过期且剩余有效时间大于30分钟，使用refreshToken进行刷新后accessToken和refreshToken均不会刷新
     * 3)accessToken未过期且剩余有效时间小于30分钟，使用refreshToken进行刷新后会得到新的accessToken和refreshToken，且旧accessToken有效期为5分钟
     * 4)accessToken过期后使用refreshToken进行刷新后会得到新的accessToken和refreshToken
     * 5)refreshToken过期后需要通过用户重新授权
     * 6)获取accessToken和刷新accessToken的参数均通过body传输
     * 7)后续所有业务接口的url和获取token的url保持一致
     */
    @RedisLock(constantKey = "xhsCheckToken",prefix ="byx:scheduled:refresh:toekn",isWaitForLock = false)
    public void checkAndRefreshToken(){
        log.info("--------------进入到每定时检查并刷新小红书token--------");
        //获取当前半小时后的时间戳
        long time = DateUtil.addDateMinutes(new Date(), 30).getTime();
        //查询半小时后过期的token
        List<XhsAuthToken> list = xhsAuthTokenPlusDao.lambdaQuery().lt(XhsAuthToken::getTokenExpires, time).list();
        if(CollectionUtils.isEmpty(list)){
            log.info("--------------没有需要刷新的token而退出--------");
            return;
        }
        //刷新token
        for(XhsAuthToken authToken : list){
            refreshToken(authToken);
        }
    }

}
