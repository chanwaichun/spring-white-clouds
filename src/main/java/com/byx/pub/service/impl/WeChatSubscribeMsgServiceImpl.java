package com.byx.pub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.SmsgAuthRecordPlusDao;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.HttpUtils;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/9/2 17:38
 */
@Slf4j
@Service
public class WeChatSubscribeMsgServiceImpl implements WeChatSubscribeMsgService {

    @Resource
    SmsgAuthRecordPlusDao smsgAuthRecordPlusDao;
    @Resource
    WeChatApiService weChatApiService;
    @Resource
    FrontOrderService orderService;
    @Resource
    FrontUserService userService;
    @Resource
    ProductService productService;
    @Resource
    AdminCardService cardService;
    @Value("${WeChat.msgTid.placeOrder}")
    private String placeOrderTid;

    /**
     * 新增授权信息
     * @param adminId
     * @param tmplId
     * @param alwaysStatus
     */
    public void addOrUpdate(String adminId,String tmplId,Boolean alwaysStatus){
        SmsgAuthRecord addRecord = new SmsgAuthRecord();
        addRecord.setAdminId(adminId).setTemplateId(tmplId).setAlwaysStatus(alwaysStatus);
        //查询是否存在
        SmsgAuthRecord record = this.getRecordByAdminId(adminId, tmplId);
        if(Objects.isNull(record)){
            this.smsgAuthRecordPlusDao.save(addRecord);
            return;
        }
        addRecord.setId(record.getId());
        this.smsgAuthRecordPlusDao.updateById(addRecord);
    }

    /**
     * 获取咨询师模板授权信息
     * @param adminId
     * @param tmplId
     * @return
     */
    public SmsgAuthRecord getRecordByAdminId(String adminId,String tmplId){
       return this.smsgAuthRecordPlusDao.lambdaQuery()
                .eq(SmsgAuthRecord::getAdminId,adminId)
                .eq(SmsgAuthRecord::getTemplateId,tmplId)
                .eq(SmsgAuthRecord::getDataStatus,Boolean.TRUE).last("limit 1").one();
    }


    /**
     * 用户下单订阅消息推送
     * 商品有关联名片就发关联名片的人，没有就发创建人，都没有就不发了
     * @param orderSn
     */
    @Async
    public void sendUserPayOrderMsg(String orderSn){
        Orders order = orderService.getByOrderSn(orderSn);
        if(Objects.isNull(order)){
            log.error("----支付回调未找到订单信息:{}",orderSn);
            return;
        }
        OrderDetail orderDetail = this.orderService.getOrderDetailByOrderSn(orderSn);
        if(Objects.isNull(orderDetail)){
            return;
        }
        Product product = productService.getProductDbById(orderDetail.getProductId());
        if(Objects.isNull(product)){
            return;
        }
        User user = null;
        //默认是发给商品关联名片人
        if(StringUtil.notEmpty(product.getCardId())){
            AdminCard card = cardService.getAdminCardById(product.getCardId());
            if(Objects.nonNull(card)){
                user = userService.getUserByAdminId(card.getAdminId());
            }
        }
        //如果商品没有关联名片 就发给 创建人
        if(Objects.isNull(user)){
            user = userService.getUserById(product.getCreator());
            if(Objects.isNull(user)){
                return;
            }
        }
        //配置参数
        Map<String,Object> dataMap = new HashMap<>();
        //下单时间
        Map<String,String> map1 = new HashMap<>();
        map1.put("value", DateUtil.dayAndTime(order.getCreateTime()));
        dataMap.put("time3",map1);
        //下单手机号
        Map<String,String> map2 = new HashMap<>();
        map2.put("value", order.getUserMobile());
        dataMap.put("phone_number5",map2);
        //下单商品
        Map<String,String> map3 = new HashMap<>();
        map3.put("value",orderDetail.getProductName());
        dataMap.put("thing2",map3);
        //下单人
        Map<String,String> map4 = new HashMap<>();
        map4.put("value", order.getUserName());
        dataMap.put("thing1",map4);
        //订单号
        Map<String,String> map5 = new HashMap<>();
        map5.put("value", order.getOrderSn());
        dataMap.put("character_string7",map5);
        //跳转页面
        String page = SystemFinalCode.WX_PLACE_ORDER_SMS_PAGE + orderSn;
        //发送消息
        sendNotice(user.getOpenId(),"formal", placeOrderTid,page,dataMap);
    }


    /**
     * 发送微信订阅消息
     * @param openId
     * @param wechatState
     * @param templateId
     * @param page
     * @param data
     * @return
     */
    private Integer sendNotice(String openId, String wechatState, String templateId, String page, Map<String, Object> data) {
        //开始发送信息
        //封装参数
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("touser",openId);
        paramsMap.put("template_id",templateId);
        if(!StringUtil.isEmpty(wechatState)) {
            paramsMap.put("miniprogram_state",wechatState);
        }
        if(!StringUtil.isEmpty(page)) {
            paramsMap.put("page",page);
        }
        paramsMap.put("lang","zh_CN");
        paramsMap.put("data",data);
        String token = this.weChatApiService.getWeChatAccessToken();
        //发送
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+token;
        String res = "";
        try {
            res = HttpUtils.requestPostBody(url, JSONObject.toJSONString(paramsMap),null);
        } catch (ApiException e) {
            log.info("订阅消息发送失败，openid：{}",openId);
        }
        Map<String, Object> returnMap = StringUtil.stringToMap(res);
        Integer retcode = (Integer) returnMap.get("errcode");
        if (retcode != null && retcode.compareTo(0) > 0) {
            String errmsg = (String) returnMap.get("errmsg");
            log.info("模板发送失败" + returnMap.get("errcode") + errmsg);
        }
        return retcode;
    }

}
