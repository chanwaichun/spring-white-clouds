package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.*;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.*;
import com.byx.pub.exception.ApiException;
import com.byx.pub.filter.RedisLock;
import com.byx.pub.mapper.OrdersSqlMapper;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * [客户端]-[用户]订单服务Api
 * @author Jump
 * @date 2023/5/24 16:11:36
 */
@Service
@Slf4j
public class FrontOrderServiceImpl implements FrontOrderService {
    @Resource
    OrdersPlusDao orderPlusDao;
    @Resource
    OrderDetailPlusDao detailPlusDao;
    @Resource
    ProductPlusDao productPlusDao;
    @Resource
    AdminPlusDao adminPlusDao;
    @Resource
    UserPlusDao userPlusDao;
    @Resource
    PayRecordPlusDao payRecordPlusDao;
    @Resource
    BusinessUserService businessUserService;
    @Resource
    OrdersSqlMapper ordersSqlMapper;
    @Resource
    BusinessService businessService;
    @Resource
    RefundsRecordPlusDao refundsRecordPlusDao;
    @Resource
    FlyerInfoService flyerInfoService;
    @Resource
    InteractiveClickService clickService;
    @Resource
    AdminCardService cardService;

    /**
     * 生成预订单
     * @param qo
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#userId",prefix ="byx-front:order:create:order",isWaitForLock = false)
    public PreOrderVo createOrder(CreateOrderQo qo,String userId){
        PreOrderVo resVo = new PreOrderVo();
        //1.校验
        User user = this.userPlusDao.getById(userId);
        if(Objects.isNull(user) || !user.getDataStatus() || !user.getUserStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户信息已失效");
        }
        if(StringUtils.isEmpty(user.getMobile())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"需先绑定手机号才可以下单");
        }
        Product product = this.productPlusDao.getById(qo.getProductId());
        if(Objects.isNull(product) || !product.getDataStatus() || !product.getShelvesStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"商品信息已失效");
        }
        //校验库存
        if(product.getServiceNum() - qo.getProductNum() < 0){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"「服务已售罄」");
        }
        if(product.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"商品价格不能少于0.01");
        }
        businessService.getSjInfo(qo.getBusinessId());
        Admin admin = this.adminPlusDao.getById(qo.getAdminId());
        if(Objects.isNull(admin) || !admin.getDataStatus() || !admin.getUserStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"咨询师信息已失效");
        }
        //2.生成订单
        BigDecimal orderTotal = product.getPrice().multiply(BigDecimal.valueOf(qo.getProductNum()));//订单金额
        String orderSn = genPoOrderSn(admin.getAdminId());//订单号
        Orders order = new Orders().setOrderSn(orderSn).setStatus(OrderStatusEnum.NOT_PAY.getCode())
                .setPullUid(qo.getPullUid()).setFacilitateUid(qo.getFacilitateUid())//促成人、拉新人
                .setAdminId(admin.getAdminId()).setBusinessId(qo.getBusinessId()).setAdminMobile(admin.getMobile())
                .setOrderAmount(orderTotal).setPaidAmount(BigDecimal.ZERO).setSurplusAmount(orderTotal)//支付金额、已支付金额、剩余支付金额
                .setThisPayment(BigDecimal.ZERO).setFullPay(Boolean.TRUE)//默认全款支付、分期才会有本次支付金额
                .setAdminName(admin.getTrueName()).setUserId(userId).setUserName(user.getNickName()).setUserMobile(user.getMobile())
                .setPayStatus(OrderPayStatusEnum.NOT_PAY.getCode()).setPayment(1).setOpenId(user.getOpenId());
        //如果是实物 设置收货人信息
        if(ProductTypeEnum.GOODS.getValue().equals(product.getProductType())){
            if(StringUtil.isEmpty(user.getShippingAddress())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请在个人中心完善收货地址信息");
            }
            order.setShippingAddress(user.getShippingAddress());
            order.setShippingName(user.getNickName());
            order.setShippingPhone(user.getMobile());
        }
        this.orderPlusDao.save(order);
        //3.生成订单明细
        OrderDetail detail = new OrderDetail().setOrderSn(orderSn).setProductId(product.getId()).setProductName(product.getProductName())
                .setPrice(product.getPrice()).setQuantity(qo.getProductNum()).setImg(product.getImg());
        this.detailPlusDao.save(detail);
        //生成记录
        this.clickService.saveLog(orderSn,PromotionTypeEnum.CREATE_ORDER.getValue(),"",userId,qo.getBusinessId());
        BeanUtils.copyProperties(order,resVo);
        BeanUtils.copyProperties(detail,resVo);
        //4.返回
        return resVo;
    }


    /**
     * 根据订单号查询订单
     * @param orderSn
     * @return
     */
    public Orders getByOrderSn(String orderSn){
        return this.orderPlusDao.lambdaQuery().eq(Orders::getOrderSn,orderSn).last(" limit 1 ").one();
    }


    /**
     * 生成订单号
     * 字母PO+咨询师id+年月日+随机数
     * @param adminId
     * @return
     */
    public String genPoOrderSn(String adminId) {
        String id = String.format("%06d", RandomUtils.nextInt(0, 1000000));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyMMdd");
        String date = LocalDate.now().format(fmt);
        String orderSn = "PO" + adminId.substring(5) +  date + id;
        Orders byOrderSn = getByOrderSn(orderSn);
        if (Objects.isNull(byOrderSn)) {
            return orderSn;
        }
        return genPoOrderSn(adminId);
    }


    /**
     * 支付回调更改支付状态
     * @param recordId 支付id
     */
    @Async("taskOrder")
    public void changeOrderStatus(String recordId){
        PayRecord payRecord = this.payRecordPlusDao.getById(recordId);
        if(Objects.isNull(payRecord)){
            log.error("------支付回调更改订单状态,未找到支付记录信息recordId:"+recordId);
            return;
        }
        Orders orders = this.getByOrderSn(payRecord.getOrderSn());
        if(Objects.isNull(orders)){
            log.error("------支付回调更改订单状态,未找到订单信息orderSn:"+payRecord.getOrderSn());
            return;
        }
        //校验订单状态
        if(OrderPayStatusEnum.PAY_SUCCESS.getCode().equals(orders.getPayStatus()) ||
                (!orders.getStatus().equals(OrderStatusEnum.NOT_PAY.getCode())
                        && !orders.getStatus().equals(OrderStatusEnum.PART_PAY.getCode())) ){
            log.error("------支付回调更改订单状态,订单状态错误PayRecordId:"+payRecord.getId());
            return;
        }
        //获取订单详情
        OrderDetail detail = this.getOrderDetailByOrderSn(orders.getOrderSn());

        //更改订单状态
        BigDecimal surplusAmount = orders.getSurplusAmount().subtract(payRecord.getAmount());
        BigDecimal paidAmount = orders.getPaidAmount().add(payRecord.getAmount());
        Orders updateOrder = new Orders()
                .setId(orders.getId())
                .setPaidAmount(paidAmount)//更新已支付金额
                .setSurplusAmount(surplusAmount)//更新剩余支付金额
                .setThisPayment(BigDecimal.ZERO);//重置本期支付金额为0 等待下次设置
        //如果第一次支付 记录时间  并且 记录商品售卖次数 、 用户画像
        Boolean firstPay = Boolean.FALSE;
        if(Objects.isNull(orders.getFirstPayTime())){
            firstPay = Boolean.TRUE;
            updateOrder.setFirstPayTime(payRecord.getSuccessTime());
            updateOrder.setFirstPayMoney(payRecord.getAmount());
            if(Objects.nonNull(detail)){
                //变更商品信息：库存、人次、销量
                paymentChangeProduct(detail.getProductId(),detail.getQuantity());
                //用户画像
                this.clickService.saveLog(orders.getOrderSn(),PromotionTypeEnum.FIRST_PAY.getValue(),"",orders.getUserId(),orders.getBusinessId());
            }
        }
        //如果完成支付
        if(surplusAmount.compareTo(BigDecimal.ZERO) == 0 && paidAmount.compareTo(orders.getOrderAmount()) == 0){
            updateOrder.setPaySn(payRecord.getPaySn())
                    .setStatus(OrderStatusEnum.ORDER_SUCCESS.getCode())
                    .setPayStatus(OrderPayStatusEnum.PAY_SUCCESS.getCode())
                    .setPaymentTime(payRecord.getSuccessTime());
            //完成支付调用传单引流校验并成交引流记录
            flyerInfoService.checkAndDeal(orders,detail);
        }else{//部分支付
            updateOrder.setStatus(OrderStatusEnum.PART_PAY.getCode())
                    .setPayStatus(OrderPayStatusEnum.PART_PAY.getCode());
        }
        this.orderPlusDao.updateById(updateOrder);
        //更新商家会员信息
        businessUserService.growMemberPayAmount(firstPay,orders.getBusinessId(),orders.getUserId(),payRecord.getAmount());
    }

    /**
     * 分页查询订单列表(适用前端)
     * @param queryQo
     * @return
     */
    public Page<PageOrdersVo> pageListOrder(FrontPageOrdersQo queryQo){
        Page<PageOrdersVo> resPageVo = new Page<>(queryQo.getPageNum(),queryQo.getPageSize());
        //1.分页条件查询订单列表
        QueryWrapper<Orders> where = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(queryQo.getBusinessId())){
            where.lambda().eq(Orders::getBusinessId,queryQo.getBusinessId());
        }
        if(StringUtils.isNotEmpty(queryQo.getAdminId())){
            where.lambda().eq(Orders::getAdminId,queryQo.getAdminId());
        }
        if(StringUtils.isNotEmpty(queryQo.getUserId())){
            where.lambda().eq(Orders::getUserId,queryQo.getUserId());
        }
        //支付状态单选 (0->未支付，1->部分支付，2->完成支付，3->退款单)
        if(Objects.nonNull(queryQo.getPayStatus())){
            if(queryQo.getPayStatus() == 0){
                where.lambda().eq(Orders::getStatus,1);
            }
            if(queryQo.getPayStatus() == 1){
                where.lambda().eq(Orders::getStatus,2);
            }
            if(queryQo.getPayStatus() == 2){
                where.lambda().eq(Orders::getStatus,4);
            }
            if(queryQo.getPayStatus() == 3){
                where.lambda().gt(Orders::getRefundStatus,0);
            }
        }
        //支付状态多选
        if(CollectionUtils.isNotEmpty(queryQo.getPayStatusList())){
            where.lambda().in(Orders::getPayStatus,queryQo.getPayStatusList());
        }
        where.lambda().orderByDesc(Orders::getCreateTime);
        IPage<Orders> page = this.orderPlusDao.page(new Page<>(queryQo.getPageNum(), queryQo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPageVo);
        if(0 == page.getTotal()){
            return resPageVo;
        }
        //2.处理数据
        List<PageOrdersVo> resList = new ArrayList<>();
        for(Orders order : page.getRecords()){
            PageOrdersVo resVo = new PageOrdersVo();
            BeanUtils.copyProperties(order,resVo);
            //查询咨询师id
            Admin admin = this.adminPlusDao.getById(order.getAdminId());
            if(Objects.isNull(admin)){
                log.error("------订单列表未找到订单咨询师信息,adminId:"+order.getAdminId());
                continue;
            }
            resVo.setAdminMobile(admin.getMobile());
            //获取订单商品信息
            OrderDetail orderDetail = this.getOrderDetailByOrderSn(order.getOrderSn());
            if(Objects.isNull(orderDetail)){
                log.error("------订单列表未找到订单明细信息,orderSn:"+order.getOrderSn());
                continue;
            }
            resVo.setProductName(orderDetail.getProductName());
            resVo.setImg(orderDetail.getImg());
            if(StringUtils.isNotEmpty(orderDetail.getProtocol())){
                resVo.setProtocolStatus(Boolean.TRUE);
            }else{
                resVo.setProtocolStatus(Boolean.FALSE);
            }
            resList.add(resVo);
        }
        resPageVo.setRecords(resList);
        return resPageVo;
    }

    /**
     * 根据订单号查询订单详情
     * @param orderSn
     * @return
     */
    public OrderDetail getOrderDetailByOrderSn(String orderSn){
        return this.detailPlusDao.lambdaQuery().eq(OrderDetail::getOrderSn, orderSn).last(" limit 1 ").one();
    }

    /**
     * 付变更商品信息(销量、服务人次、库存)
     * @param productId
     * @param stockNum
     */
    public void paymentChangeProduct(String productId,Integer stockNum){
        Product one = this.productPlusDao.getById(productId);
        if(Objects.isNull(one)){
            return;
        }
        Product update = new Product()
                .setId(productId)
                .setSaleNum(one.getSaleNum()+1)
                .setUserBuyNum(one.getUserBuyNum()+1)
                .setServiceNum(one.getServiceNum() - stockNum);
        this.productPlusDao.updateById(update);
    }

    /**
     * 取消订单
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String id){
        Orders orders = this.orderPlusDao.getById(id);
        if(Objects.isNull(orders)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到订单信息");
        }
        if(OrderStatusEnum.NOT_PAY.getCode().intValue() != orders.getStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"订单当前状态不可取消");
        }

        Orders updateBean = new Orders();
        updateBean.setId(id).setStatus(OrderStatusEnum.CLOSED.getCode());
        this.orderPlusDao.updateById(updateBean);

        PayRecord payRecord = this.payRecordPlusDao.lambdaQuery().eq(PayRecord::getOrderSn, orders.getOrderSn())
                .last(" limit 1 ").one();
        if(Objects.isNull(payRecord)){
            return;
        }
        if(payRecord.getStatus().intValue() == PayStatus.SUCCESS.getCode()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"订单支付状态错误");
        }
        PayRecord updatePay = new PayRecord().setId(payRecord.getId()).setStatus(PayStatus.CLOSED.getCode());
        this.payRecordPlusDao.updateById(updatePay);
    }


    /**
     * 订单详情(通用)
     * @param orderSn
     * @return
     */
    public OrderDetailVo getOrderDetail(String orderSn){
        OrderDetailVo resVo = new OrderDetailVo();
        //1.获取订单信息
        Orders byOrderSn = this.getByOrderSn(orderSn);
        if(Objects.isNull(byOrderSn)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到订单信息");
        }
        BeanUtils.copyProperties(byOrderSn,resVo);
        //2.获取订单详情信息
        List<OrderItemVo> itemVoList = new ArrayList<>();
        OrderDetail detail = this.getOrderDetailByOrderSn(orderSn);
        if(Objects.isNull(detail)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到订单明细信息");
        }
        OrderItemVo itemVo = new OrderItemVo();
        BeanUtils.copyProperties(detail,itemVo);
        itemVo.setTotalAmount(detail.getPrice().multiply(new BigDecimal(detail.getQuantity())));
        itemVoList.add(itemVo);
        resVo.setItemVoList(itemVoList);
        resVo.setProtocol(detail.getProtocol());
        //3.获取支付记录列表
        List<PayRecord> payRecords = this.payRecordPlusDao.lambdaQuery().eq(PayRecord::getStatus,PayStatus.SUCCESS.getCode())
                .eq(PayRecord::getOrderSn, orderSn).orderByDesc(PayRecord::getCreateTime).list();
        List<PayRecordVo> resList = CollectionUtils.isEmpty(payRecords)
                ? new ArrayList<>():CopyUtil.copyList(payRecords,PayRecordVo.class);
        if(byOrderSn.getFullPay()){
            resVo.setPayRecordVoList(resList);
            return resVo;
        }
        //4.添加记录在最后 如果 有设置本期支付 显示本期支付 否则显示所有尾款
        PayRecordVo lastVo = new PayRecordVo();
        if(byOrderSn.getThisPayment().compareTo(BigDecimal.ZERO) > 0){
            lastVo.setAmount(byOrderSn.getThisPayment());
        }else{
            lastVo.setAmount(byOrderSn.getSurplusAmount());
        }
        //5.获取咨询师名片
        AdminCard card = this.cardService.getCardByAdminId(byOrderSn.getAdminId(), byOrderSn.getBusinessId());
        if(Objects.nonNull(card)){
            resVo.setCardId(card.getId());
        }
        lastVo.setPaySn("").setPayType(1).setStatus(PayStatus.NOTPAY.getCode()).setSuccessTime(null);
        resList.add(lastVo);
        resVo.setPayRecordVoList(resList);
        return resVo;
    }


    /**
     * 管理后台查询订单列表
     * @param queryQo
     * @return
     */
    public Page<PageOrdersVo> queryManagePageOrders(PageManageOrdersQo queryQo){
        return this.ordersSqlMapper.pageManageOrdersList(new Page(queryQo.getPageNum(),queryQo.getPageSize()),queryQo);
    }

    /**
     * 更改订单为分期付款订单
     * @param orderId
     */
    public void changePayType(String orderId){
        Orders orders = this.orderPlusDao.getById(orderId);
        if(Objects.isNull(orders)){
            throw new ApiException("未找到订单数据");
        }
        if(!orders.getStatus().equals(OrderStatusEnum.NOT_PAY.getCode())){
            throw new ApiException("订单当前状态不支持更改为分期付款");
        }
        Orders bean = new Orders().setId(orders.getId()).setFullPay(Boolean.FALSE);
        this.orderPlusDao.updateById(bean);
    }

    /**
     * 设置本次支付金额
     * @param qo
     */
    public void setThisPayment(OrderChangePriceQo qo){
        Orders orders = this.orderPlusDao.getById(qo.getOrderId());
        if(Objects.isNull(orders)){
            throw new ApiException("未找到订单数据");
        }
        if(orders.getFullPay()){
            throw new ApiException("订单为全额支付方式,请先设置为分期支付");
        }
        if(orders.getStatus().equals(OrderStatusEnum.CLOSED.getCode())
            || orders.getStatus().equals(OrderStatusEnum.ORDER_SUCCESS.getCode())){
            throw new ApiException("订单状态错误");
        }
        if(qo.getThisPayment().compareTo(orders.getSurplusAmount()) >= 0){
            throw new ApiException("本次支付金额足够支付订单尾款金额，无需再分期");
        }
        Orders bean = new Orders().setId(orders.getId()).setThisPayment(qo.getThisPayment());
        this.orderPlusDao.updateById(bean);
    }

    /**
     * 查询商家订单列表
     * @param qo
     * @return
     */
    public Page<DataCountTradeOrderVo> pageSjOrderList(TradeOrderListQo qo){
        Page<DataCountTradeOrderVo> resPage = new Page<>();
        QueryWrapper<Orders> where = new QueryWrapper<>();
        if(StringUtil.notEmpty(qo.getBusinessId())){
            where.lambda().eq(Orders::getBusinessId,qo.getBusinessId());
        }
        if(StringUtil.notEmpty(qo.getCreateStart())){
            where.lambda().ge(Orders::getCreateTime,qo.getCreateStart());
        }
        if(StringUtil.notEmpty(qo.getCreateEnd())){
            where.lambda().le(Orders::getCreateTime,qo.getCreateEnd());
        }
        IPage<Orders> page = this.orderPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        resPage.setRecords(CopyUtil.copyList(page.getRecords(),DataCountTradeOrderVo.class));
        return resPage;
    }

    /**
     * 校验退款并返回订单金额
     * @param refundsQo
     * @return
     */
    public Orders checkOrderRefund(RefundsQo refundsQo){
        //1.查询订单信息
        Orders orders = this.getByOrderSn(refundsQo.getOrderSn());
        if(Objects.isNull(orders)){
            throw new ApiException("未找到订单");
        }
        //订单只允许退一次款
        if(orders.getRefundStatus() != 0 ){
            throw new ApiException("该订单已经退款过一次");
        }
        //如果订单不是微信支付的
        if(orders.getPayment() != 1){
            throw new ApiException("该订单非微信支付订单");
        }
        //校验订单是否可以退款：部分支付、已支付、已完成(待结算)
        if(orders.getStatus().equals(OrderStatusEnum.CLOSED.getCode())
                || orders.getStatus().equals(OrderStatusEnum.NOT_PAY.getCode())
                || orders.getStatus().equals(OrderStatusEnum.ORDER_SETTLEMENT.getCode())){
            throw new ApiException("订单当前状态不支持退款");
        }
        //部分退款
        if(refundsQo.getRefundType() == 1){
            //退款金额必填 且 大于0
            if(Objects.isNull(refundsQo.getRefundAmount())
                    || BigDecimal.ZERO.compareTo(refundsQo.getRefundAmount()) >= 0){
                throw new ApiException("部分退款必须输入退款金额，且金额大于0");
            }
            //退款金额不能大于订单已支付金额
            if(refundsQo.getRefundAmount().compareTo(orders.getPaidAmount()) > 0 ){
                throw new ApiException("退款金额不能大于订单已支付金额");
            }
        }
        //全部退款 设置已支付金额为退款金额
        if(refundsQo.getRefundType() == 2){
            refundsQo.setRefundAmount(orders.getPaidAmount());
        }
        return orders;
    }

    /**
     * 退款申请(成功)变更订单信息
     * @param changeOrderDto
     */
    public void changeOrderRefundStatus(RefundChangeOrderDto changeOrderDto){
        //1.查询订单
        Orders byOrderSn = getByOrderSn(changeOrderDto.getOrderSn());
        if(Objects.isNull(byOrderSn)){
            log.error("----未找到订单----");
            return;
        }
        //2.变更订单（状态、已支付金额）
        Orders updateBean = new Orders();
        updateBean.setId(byOrderSn.getId());
        updateBean.setRefundStatus(changeOrderDto.getRefundStatus());
        //退款中(申请退款)
        if(changeOrderDto.getRefundStatus() == 1){
            updateBean.setRefundAmount(changeOrderDto.getRefundAmount());
            updateBean.setRefundSn(changeOrderDto.getRefundSnStr());
            updateBean.setRefundType(changeOrderDto.getRefundType());
            this.orderPlusDao.updateById(updateBean);
        }
        //退款成功
        if(changeOrderDto.getRefundStatus() == 2){
            updateBean.setRefundTime(changeOrderDto.getSuccessTime());
            //累计已支付 = 累计已支付 - 退款金额
            updateBean.setPaidAmount(byOrderSn.getPaidAmount().subtract(byOrderSn.getRefundAmount()));
            //如果是全额退款 则 订单为[取消]，如部分退款，订单为[完成]
            if(updateBean.getPaidAmount().compareTo(BigDecimal.ZERO) == 0){
                updateBean.setStatus(OrderStatusEnum.CLOSED.getCode());
            }else{
                updateBean.setStatus(OrderStatusEnum.ORDER_SUCCESS.getCode());
            }
            this.orderPlusDao.updateById(updateBean);
        }
    }

    /**
     * 分页查询退款订单列表
     * @param queryQo
     * @return
     */
    public Page<PageRefundOrderVo> pageRefundList(PageRefundOrderQo queryQo){
        return this.ordersSqlMapper.pageRefundOrdersList(new Page<>(queryQo.getPageNum(),queryQo.getPageSize()),queryQo);
    }

    /**
     * 获取退款单详情
     * @param orderSn
     * @return
     */
    public RefundDetailVo getRefundDetailVo(String orderSn){
        RefundDetailVo resVo = new RefundDetailVo();
        //获取订单信息
        Orders byOrderSn = this.getByOrderSn(orderSn);
        if(Objects.isNull(byOrderSn)){
            throw new ApiException("未找到订单信息");
        }
        BeanUtils.copyProperties(byOrderSn,resVo);
        //2.获取订单商品信息
        List<OrderItemVo> itemVoList = new ArrayList<>();
        OrderDetail detail = this.getOrderDetailByOrderSn(orderSn);
        if(Objects.nonNull(detail)){
            OrderItemVo itemVo = new OrderItemVo();
            BeanUtils.copyProperties(detail,itemVo);
            itemVo.setTotalAmount(detail.getPrice().multiply(new BigDecimal(detail.getQuantity())));
            itemVoList.add(itemVo);
            resVo.setItemVoList(itemVoList);
        }
        //3.获取退款记录
        List<RefundsRecord> list = this.refundsRecordPlusDao.lambdaQuery()
                .eq(RefundsRecord::getOrderSn, orderSn).orderByAsc(RefundsRecord::getCreateTime).list();
        if(CollectionUtils.isNotEmpty(list)){
            resVo.setRecordVoList(CopyUtil.copyList(list,RefundRecordVo.class));
        }
        return resVo;
    }

    /**
     * 手动完成订单
     * @param orderSn
     */
    public void handDoneOrder(String orderSn){
        Orders orders = this.getByOrderSn(orderSn);
        if(Objects.isNull(orders)){
            throw new ApiException("未找到订单");
        }
        if(orders.getStatus() < OrderStatusEnum.PART_PAY.getCode()
                || orders.getStatus() > OrderStatusEnum.PAY_SUCCESS.getCode()){
            throw new ApiException("订单当前状态不支持手动完成");
        }
        this.orderPlusDao.updateById(
                new Orders()
                .setId(orders.getId())
                .setStatus(OrderStatusEnum.ORDER_SUCCESS.getCode())
        );
    }

    /**
     * 结算订单
     * @param orderId
     */
    public void settlementOrder(String orderId){
        this.orderPlusDao.updateById(
                new Orders()
                .setId(orderId)
                .setStatus(OrderStatusEnum.ORDER_SETTLEMENT.getCode())
        );
    }

    /**
     * 取消24小时以上未支付订单
     */
    @RedisLock(constantKey = "closedNoPayOrder",prefix ="byx:scheduled:closed:noPayOrder",isWaitForLock = false)
    public void closedNoPayOrder(){
        log.info("-----------开始取消24小时以上待支付订单任务------------------");
        //获取24小时之前的时间
        Date executeTime = DateUtil.localDateTimeToDate(LocalDateTime.now().minusHours(24));
        //获取超24小时未支付订单
        List<Orders> list = this.orderPlusDao.lambdaQuery().eq(Orders::getStatus, OrderStatusEnum.NOT_PAY.getCode())
                .le(Orders::getCreateTime, executeTime).list();
        if(CollectionUtils.isEmpty(list)){
            log.info("-----------取消24小时以上待支付订单任务，没有数据而结束------------------");
            return;
        }
        //取消订单
        List<String> ids = list.stream().map(Orders::getId).collect(Collectors.toList());
        this.orderPlusDao.lambdaUpdate().in(Orders::getId,ids)
                .set(Orders::getStatus,OrderStatusEnum.CLOSED.getCode()).update();
        log.info("-----------取消24小时以上待支付订单任务完成：{}------------------",ids.size());
    }


}
