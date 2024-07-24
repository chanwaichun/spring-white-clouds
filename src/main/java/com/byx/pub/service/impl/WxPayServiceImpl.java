package com.byx.pub.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.byx.pub.bean.qo.JsapiPayQo;
import com.byx.pub.bean.qo.RefundChangeOrderDto;
import com.byx.pub.bean.qo.RefundsQo;
import com.byx.pub.bean.qo.WxRefundDto;
import com.byx.pub.bean.vo.SelectOrderVo;
import com.byx.pub.bean.vo.WxJspaiPayVo;
import com.byx.pub.bean.vo.notify.NotifyVo;
import com.byx.pub.bean.vo.refund.RefundVo;
import com.byx.pub.bean.vo.refundnotify.RefundOrderNotifyVo;
import com.byx.pub.enums.*;
import com.byx.pub.exception.ApiException;
import com.byx.pub.filter.RedisLock;
import com.byx.pub.plus.dao.PayRecordPlusDao;
import com.byx.pub.plus.dao.RefundsRecordPlusDao;
import com.byx.pub.plus.entity.Orders;
import com.byx.pub.plus.entity.PayRecord;
import com.byx.pub.plus.entity.RefundsRecord;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.service.WeChatSubscribeMsgService;
import com.byx.pub.service.WxPayService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.WxUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 微信支付接口实现
 *
 * @program: pub-pay
 * @description: 微信支付接口实现
 * @author: Jim
 * @create: 2021-12-07
 */
@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    @Value("${WeChat.AppId}")
    private String appid;
    @Value("${WeChat.privateKey}")
    private String privateKey;
    @Value("${WeChat.mchId}")
    private String mchId;
    @Value("${WeChat.mchSerialNo}")
    private String mchSerialNo;
    @Value("${WeChat.apiV3Key}")
    private String apiV3Key;
    @Value("${WeChat.notifyUrl}")
    private String notifyUrl;
    @Value("${WeChat.refundnotifyUrl}")
    private String refundNotifyUrl;
    @Resource
    PayRecordPlusDao payRecordPlusDao;
    @Resource
    FrontOrderService orderService;
    @Resource
    WeChatSubscribeMsgService msgService;
    @Resource
    RefundsRecordPlusDao refundsRecordPlusDao;

    /**
     * jsapi下单
     * @param jsapiPayQo 请求体
     * @return
     * @throws Exception
     */
    @RedisLock(key = "#jsapiPayQo.orderSn",isWaitForLock = false)
    public CommonResult<WxJspaiPayVo> jsApiPay(JsapiPayQo jsapiPayQo) throws Exception {
        //下单
        JSONObject jsonObject = placeOrder(jsapiPayQo);
        log.info("------jsapi下单返回:"+jsonObject.toString());
        if (jsonObject.containsKey("prepay_id")) {
            // app调起支付参数
            WxJspaiPayVo wxJspaiPayVo = WxUtil.WxJsApiTuneUp(jsonObject.getStr("prepay_id"),appid,getPrivateKey());
            return CommonResult.success(wxJspaiPayVo);
        } else {
            return CommonResult.failed(jsonObject.getStr("message"));
        }
    }


    /**
     * jsapi下单
     * @param jsapiPayQo
     * @return
     * @throws Exception
     */
    JSONObject placeOrder(JsapiPayQo jsapiPayQo) throws Exception {
        // 校验是否已支付 且 关闭待支付订单 且 获取支付金额
        BigDecimal payAmount = checkAndClosedOrder(jsapiPayQo);
        if(payAmount.compareTo(BigDecimal.ZERO) == 0){
            throw new ApiException("支付金额不能为0");
        }
        //获取订单信息
        Orders orders = this.orderService.getByOrderSn(jsapiPayQo.getOrderSn());
        // 商家支付订单号：订单系统订单号+6位随机数
        String outTradeNo = jsapiPayQo.getOrderSn()+String.format("%03d", RandomUtils.nextInt(0, 999));
        // 保存支付记录
        PayRecord payRecord = new PayRecord();
        payRecord.setOrderSn(jsapiPayQo.getOrderSn()).setOutTradeNo(outTradeNo)
                .setUserId(jsapiPayQo.getUserId()).setBusinessId(orders.getBusinessId())
        .setDescription(WeChatUrlConstants.DESCRIPTION).setOpenid(orders.getOpenId()).setProductsAmount(orders.getOrderAmount());
        //处理支付金额(全额、分期)
        payRecord.setAmount(payAmount);
        payRecordPlusDao.save(payRecord);
        //设置请求url
        CloseableHttpClient httpClient = getClient();
        HttpPost httpPost = new HttpPost(WeChatUrlConstants.PAY_V3_JSAPI);

        // 设置请求头
        httpPost.addHeader(WeChatUrlConstants.ACCEPT, WeChatUrlConstants.ACCEPT_VALUE);
        httpPost.addHeader(WeChatUrlConstants.CONTENT_TYPE,WeChatUrlConstants.CONTENT_TYPE_VALUE);

        // 设置下单参数
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        // 支付金额 转化成 分
        BigDecimal total = payRecord.getAmount().multiply(new BigDecimal(100));
        if (total.intValue() > Integer.MAX_VALUE) {
            throw new ApiException("订单支付金额不能大于21,474,836.47元");
        }
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid",mchId)
                .put("appid", appid)
                .put("description", WeChatUrlConstants.DESCRIPTION)
                .put("notify_url", notifyUrl)
                .put("out_trade_no", payRecord.getOutTradeNo())
                .put("attach",payRecord.getId());
        //设置金额
        rootNode.putObject("amount")
                .put("total", total.intValue());
        //设置下单人openid
        rootNode.putObject("payer")
                .put("openid",orders.getOpenId());
        /*//分账设置(不分账)
        rootNode.putObject("settle_info")
                .put("profit_sharing", Boolean.FALSE);*/
        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString(WeChatUrlConstants.ENCODING_VALUE), WeChatUrlConstants.ENCODING_VALUE));

        // 请求微信下单接口
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 返回数据
        String bodyAsString = EntityUtils.toString(response.getEntity());

        JSONObject jsonObject = JSONUtil.parseObj(bodyAsString);

        PayRecord record = new PayRecord();
        record.setId(payRecord.getId())
                .setRequestBody(rootNode.toString())
                .setResponseBody(jsonObject.toString());
        if (jsonObject.containsKey("prepay_id") || jsonObject.containsKey("code_url")) {
            record.setPlaceStatus(PlaceStatus.SUCESS.getCode());
        }
        payRecordPlusDao.updateById(record);// 更新支付记录
        return  jsonObject;
    }


    /**
     * 校验且关闭订单
     * @param jsapiPayQo
     */
    public BigDecimal checkAndClosedOrder(JsapiPayQo jsapiPayQo){
        BigDecimal payAmount = BigDecimal.ZERO;
        //1.查询订单是否已完成支付
        Orders orders = orderService.getByOrderSn(jsapiPayQo.getOrderSn());
        if (Objects.isNull(orders)) {
            throw new ApiException("订单号异常");
        }
        if(orders.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ApiException("订单金额必须大于0元");
        }
        if (OrderStatusEnum.ORDER_SUCCESS.getCode().equals(orders.getStatus())
                || orders.getOrderAmount().compareTo(orders.getPaidAmount()) == 0
                || PayStatus.SUCCESS.getCode().equals(orders.getPayStatus())) {
            throw new ApiException("订单已经支付成功");
        }
        //校验订单支付方式 和 设置支付金额
        if(PayTypeEnum.PAY_ALL.getCode().equals(jsapiPayQo.getPayType())){
            if(!orders.getFullPay()){
                throw new ApiException("订单为分期支付订单");
            }
            payAmount = orders.getOrderAmount();
        }else if(PayTypeEnum.PAY_THIS.getCode().equals(jsapiPayQo.getPayType())){
            if(orders.getFullPay()){
                throw new ApiException("订单为全额支付订单");
            }
            if(orders.getThisPayment().compareTo(BigDecimal.ZERO) == 0){
                throw new ApiException("请设置本期支付金额");
            }
            payAmount = orders.getThisPayment();
        }else if(PayTypeEnum.PAY_SURPLUS.getCode().equals(jsapiPayQo.getPayType())){
            if(orders.getFullPay()){
                throw new ApiException("订单为全额支付订单");
            }
            if(orders.getSurplusAmount().compareTo(BigDecimal.ZERO) == 0){
                throw new ApiException("剩余支付金额为0");
            }
            payAmount = orders.getSurplusAmount();
        }else{
            throw new ApiException("请选择正确的支付方式");
        }
        //2.关闭待支付订单
        PayRecord updateBean = new PayRecord().setStatus(PayStatus.CLOSED.getCode());
        LambdaQueryWrapper<PayRecord> updateWhere = new LambdaQueryWrapper<>();
        updateWhere.eq(PayRecord::getPlaceStatus, PlaceStatus.SUCESS.getCode())
                .eq(PayRecord::getOrderSn,orders).eq(PayRecord::getStatus,PayStatus.NOTPAY.getCode());
        this.payRecordPlusDao.update(updateBean,updateWhere);
        return payAmount;
    }

    /**
     * 获取微信API3的HttpClient
     * @return
     * @throws Exception
     */
    @Override
    public CloseableHttpClient getClient() throws Exception {
        // 商户私钥
        PrivateKey merchantPrivateKey = getPrivateKey();
        // 使用定时更新的签名验证器，不需要传入证书
        ScheduledUpdateCertificatesVerifier verifier = new ScheduledUpdateCertificatesVerifier(
                new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),
                apiV3Key.getBytes(StandardCharsets.UTF_8));

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier));

        return builder.build();
    }

    /**
     * 获取私钥
     * @return
     * @throws Exception
     */
    PrivateKey getPrivateKey() throws Exception {
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes(WeChatUrlConstants.ENCODING_VALUE)));
        return  merchantPrivateKey;
    }

    /**
     * 微信支付回调
     * @param requestBodyString
     * @return
     * @throws Exception
     */
    public Map<String, String> notify(String requestBodyString) throws Exception {
        Map<String, String> map = new HashMap<>(12);
        // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
        String plainText = WxUtil.verifyNotify(requestBodyString, apiV3Key);
        log.info("--------支付回调验证签名结果:"+plainText);
        NotifyVo notifyVo = JSONUtil.parseObj(plainText).toBean(NotifyVo.class);
        if (StrUtil.isNotEmpty(plainText)) {
            // 更新支付记录、订单状态、发送订阅消息
            updateNotify(notifyVo);
            map.put("code", "SUCCESS");
            map.put("message", "SUCCESS");
        } else {
            map.put("code", "ERROR");
            map.put("message", "签名错误");
        }
        return map;
    }

    /**
     * 更新支付记录
     * @param notifyVo
     */
    void updateNotify(NotifyVo notifyVo) {
        if (Objects.nonNull(notifyVo) && StringUtils.isNotEmpty(notifyVo.getOut_trade_no()) && notifyVo.getTrade_state().equals("SUCCESS")) {
            //查询支付记录
            PayRecord payRecord = payRecordPlusDao.getById(notifyVo.getAttach());
            if (Objects.isNull(payRecord)) {
                log.error("-------支付回调单号：{}找到数据",notifyVo.getOut_trade_no());
                return;
            }
            //处理幂等性
            if(payRecord.getStatus().equals(PayStatus.SUCCESS.getCode())){
                return;
            }
            //更改数据
            PayRecord record = new PayRecord();
            record.setId(payRecord.getId())
                    .setPaySn(notifyVo.getTransaction_id())
                    .setOpenid(notifyVo.getPayer().getOpenid())
                    .setSuccessTime(DateUtil.parse(notifyVo.getSuccess_time(),"yyyy-MM-dd'T'HH:mm:ssXXX"))//数据库已设置时区
                    .setStatus(PayStatus.SUCCESS.getCode())
                    .setNotifyBody(JSONUtil.toJsonStr(notifyVo));
            payRecordPlusDao.updateById(record);
            //更新订单状态
            this.orderService.changeOrderStatus(payRecord.getId());
            //给咨询师推送订阅消息
            msgService.sendUserPayOrderMsg(payRecord.getOrderSn());
        } else {
            log.warn(notifyVo.toString());
        }
    }


    /**
     * 支付结果查询
     * @param orderSn 订单号
     * @return
     * @throws Exception
     */
    public Boolean queryJsApiPay(String orderSn) throws Exception {
        //1.查询出 当前订单 待支付、已支付 的支付记录
        LambdaQueryWrapper<PayRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayRecord::getOrderSn,orderSn)
                .eq(PayRecord::getPlaceStatus,PlaceStatus.SUCESS.getCode())
                .and(wrapper1->wrapper1.eq(PayRecord::getStatus,PayStatus.NOTPAY.getCode())
                        .or(warpper2->warpper2.eq(PayRecord::getStatus,PayStatus.SUCCESS.getCode())));
        List<PayRecord> list = payRecordPlusDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) { // 没有支付记录
            return  Boolean.FALSE;
        }
        //2.循环查询是否有支付成功的纪录(回调)
        for (PayRecord payRecord: list) {
            if (payRecord.getStatus().intValue() == PayStatus.SUCCESS.getCode().intValue()) {
                return Boolean.TRUE;
            }else{
                orderSn = payRecord.getOutTradeNo();
            }
        }
        //3.向微信发起查询
        StringBuilder uri = new StringBuilder(WeChatUrlConstants.PAY_V3_QUERY_OUT);//设置url
        uri.append(orderSn).append("?mchid=").append(mchId);//设置参数
        log.info("query url:{}",uri.toString());
        CloseableHttpClient httpClient = getClient();
        HttpGet httpGet = new HttpGet(uri.toString());
        httpGet.addHeader(WeChatUrlConstants.ACCEPT, WeChatUrlConstants.ACCEPT_VALUE);//设置请求头
        CloseableHttpResponse response = httpClient.execute(httpGet);//发起请求
        String bodyAsString = EntityUtils.toString(response.getEntity());
        log.info(bodyAsString);
        JSONObject jsonObject = JSONUtil.parseObj(bodyAsString);
        if (jsonObject.containsKey("code")) {
            log.error("-----------------支付查询结果错误："+jsonObject.getStr("message"));
            return Boolean.FALSE;
        } else {
            SelectOrderVo selectOrderVo = JSONUtil.parseObj(bodyAsString).toBean(SelectOrderVo.class);
            if (selectOrderVo.getTrade_state().equals("SUCCESS")) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }


    /**
     * 微信分批次退款
     * @param refundsQo
     * @throws Exception
     */
    @RedisLock(key = "#refundsQo.orderSn",isWaitForLock = false)
    public void splitRefundReq(RefundsQo refundsQo) throws Exception {
        //校验并设置退款金额
        this.orderService.checkOrderRefund(refundsQo);
        //获取订单支付记录明细
        List<Integer> refundStatusList = new ArrayList<>(2);
        refundStatusList.add(1);refundStatusList.add(2);
        List<PayRecord> list = payRecordPlusDao.lambdaQuery()
                .eq(PayRecord::getOrderSn, refundsQo.getOrderSn())
                .eq(PayRecord::getStatus, PayStatus.SUCCESS.getCode())
                .in(PayRecord::getRefundStatus, refundStatusList)
                .orderByDesc(PayRecord::getAmount).list();
        if(CollectionUtils.isEmpty(list)){
            throw new ApiException("订单无可退款金额，请联系管理员");
        }
        //循环拆分\退款
        List<String> refundSnList = new ArrayList<>();
        BigDecimal refundAmount = refundsQo.getRefundAmount();
        for(PayRecord payRecord : list){
            //创建退款对象
            WxRefundDto refundDto = new WxRefundDto();
            refundDto.setUid(refundsQo.getUid())
                    .setOrderSn(refundsQo.getOrderSn())
                    .setOrderAmount(payRecord.getAmount())
                    .setOrderSn(refundsQo.getOrderSn())
                    .setOutTradeNo(payRecord.getOutTradeNo())
                    .setPayRecordId(payRecord.getId())
                    .setRefundSn(genRfOrderSn(refundsQo.getUid()));
            //先减去已退款金额
            BigDecimal payAmount = payRecord.getAmount().subtract(payRecord.getRefundAmount());
            //如果本次支付金额 大于或等于 退款金额 直接退
            if(payAmount.compareTo(refundAmount) >= 0){
                refundDto.setRefundAmount(refundAmount);
                refundsApply(refundDto);
                refundSnList.add(refundDto.getRefundSn());
                break;
            }
            //如果本质支付金额 小于 退款金额 全额退掉本次 且 扣减退款金额
            refundDto.setRefundAmount(payAmount);
            refundAmount = refundAmount.subtract(payAmount);
            refundsApply(refundDto);
            refundSnList.add(refundDto.getRefundSn());
        }
        //更新订单状态 -- 退款中
        String refundSnStr = StringUtils.join(refundSnList, ",");
        RefundChangeOrderDto changeOrderDto = new RefundChangeOrderDto();
        changeOrderDto.setOrderSn(refundsQo.getOrderSn());
        changeOrderDto.setRefundSnStr(refundSnStr);
        changeOrderDto.setRefundAmount(refundsQo.getRefundAmount());
        changeOrderDto.setRefundStatus(1);
        changeOrderDto.setRefundType(refundsQo.getRefundType());
        orderService.changeOrderRefundStatus(changeOrderDto);
    }


    /**
     * 退款申请
     * @param wxRefundDto
     * @return
     * @throws Exception
     */
    public void refundsApply(WxRefundDto wxRefundDto) throws Exception {
        //构建退款url
        CloseableHttpClient httpClient = getClient();
        HttpPost httpPost = new HttpPost(WeChatUrlConstants.PAY_V3_REFUND);
        // 设置请求头
        httpPost.addHeader(WeChatUrlConstants.ACCEPT, WeChatUrlConstants.ACCEPT_VALUE);
        httpPost.addHeader(WeChatUrlConstants.CONTENT_TYPE,WeChatUrlConstants.CONTENT_TYPE_VALUE);

        // 设置下单参数
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        // 订单金额
        BigDecimal total = wxRefundDto.getOrderAmount().multiply(new BigDecimal(100));
        // 退款金额
        BigDecimal refund = wxRefundDto.getRefundAmount().multiply(new BigDecimal(100));

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode
                .put("out_refund_no", wxRefundDto.getRefundSn())
                .put("out_trade_no", wxRefundDto.getOutTradeNo())
                .put("notify_url", refundNotifyUrl);
        rootNode.putObject("amount")
                .put("total", total.intValue())
                .put("refund", refund.intValue())
                .put("currency", WeChatUrlConstants.CURRENCY);

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString(WeChatUrlConstants.ENCODING_VALUE), WeChatUrlConstants.ENCODING_VALUE));

        // 请求接口
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        log.info(bodyAsString);
        JSONObject jsonObject = JSONUtil.parseObj(bodyAsString);
        if (jsonObject.containsKey("code")) {
            throw new ApiException(jsonObject.getStr("message"));
        }
        RefundVo refundVo = JSONUtil.parseObj(bodyAsString).toBean(RefundVo.class);
        //保存退款记录
        saveRefunds(wxRefundDto,refundVo);
    }


    /**
     * 保存退款记录
     * @param wxRefundDto
     * @param refundVo
     */
    void saveRefunds(WxRefundDto wxRefundDto,RefundVo refundVo) {
        RefundsRecord refundsRecord = new RefundsRecord();
        refundsRecord.setOrderSn(wxRefundDto.getOrderSn())
                .setRefundSn(refundVo.getOut_refund_no())
                .setRefundId(refundVo.getRefund_id())
                .setPayRecordId(wxRefundDto.getPayRecordId())
                .setUserId(wxRefundDto.getUid())
                .setMchid(mchId)
                .setRefundType(1)
                .setRefundAmount(wxRefundDto.getRefundAmount())
                .setAmount(wxRefundDto.getOrderAmount())
                .setRefundsStatus(PlaceStatus.SUCESS.getCode())
                .setStatus(RefundStatus.valueOf(refundVo.getStatus()).getValue())
                .setSuccessTime(refundVo.getSuccess_time()==null?null: DateUtil.parse(refundVo.getSuccess_time(),"yyyy-MM-dd'T'HH:mm:ss'+08:00'"))
                .setUserReceivedAccount(refundVo.getUser_received_account())
                .setChannel(refundVo.getChannel())
                .setRequestBody(JSONUtil.parseObj(wxRefundDto).toString())
                .setResponseBody(JSONUtil.parseObj(refundVo).toString());
        refundsRecordPlusDao.save(refundsRecord);
    }


    /**
     * 微信退款回调
     * @param requestBodyString
     * @return
     * @throws Exception
     */
    public Map<String, String> refundNotify(String requestBodyString) throws Exception {
        log.info("------进入到退款回调，requestBodyString：{}",requestBodyString);
        Map<String, String> map = new HashMap<>(12);
        // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
        String plainText = WxUtil.verifyNotify(requestBodyString, apiV3Key);
        log.info("------进入到退款回调，解密后参数plainText：{}",plainText);
        RefundOrderNotifyVo refundOrderNotifyVo = JSONUtil.parseObj(plainText).toBean(RefundOrderNotifyVo.class);
        if (StrUtil.isNotEmpty(plainText)) {
            // 更新退款记录
            UpdateRefundNotify(refundOrderNotifyVo);
            map.put("code", "SUCCESS");
            map.put("message", "SUCCESS");
        } else {
            map.put("code", "ERROR");
            map.put("message", "签名错误");
        }
        return map;
    }

    /**
     * 更新退款记录
     * @param refundOrderNotifyVo
     */
    void UpdateRefundNotify(RefundOrderNotifyVo refundOrderNotifyVo) {
        RefundStatus refundStatus =  RefundStatus.valueOf(refundOrderNotifyVo.getRefund_status());
        //如果退款成功
        if (refundStatus.equals(RefundStatus.SUCCESS)) {
            //查询退款记录(幂等性处理)
            RefundsRecord refundsRecord = this.refundsRecordPlusDao.lambdaQuery()
                    .eq(RefundsRecord::getRefundSn, refundOrderNotifyVo.getOut_refund_no())
                    .eq(RefundsRecord::getRefundId, refundOrderNotifyVo.getRefund_id())
                    .eq(RefundsRecord::getStatus,RefundStatus.PROCESSING.getValue())
                    .last("limit 1").one();
            if(Objects.nonNull(refundsRecord)){
                //更新退款记录
                RefundsRecord updateBean = new RefundsRecord()
                        .setId(refundsRecord.getId())
                        .setSuccessTime(DateUtil.parse(refundOrderNotifyVo.getSuccess_time(),"yyyy-MM-dd'T'HH:mm:ss'+08:00'"))
                        .setStatus(RefundStatus.SUCCESS.getValue());
                this.refundsRecordPlusDao.updateById(updateBean);
                //更新支付记录
                PayRecord record = new PayRecord()
                        .setId(refundsRecord.getPayRecordId())
                        .setRefundAmount(refundsRecord.getRefundAmount())
                        .setRefundStatus(2);
                if(refundsRecord.getRefundAmount().compareTo(refundsRecord.getAmount()) == 0){
                    record.setRefundStatus(3);
                }
                this.payRecordPlusDao.updateById(record);
                //更新订单状态 -- 退款完成
                RefundChangeOrderDto changeOrderDto = new RefundChangeOrderDto();
                changeOrderDto.setOrderSn(refundsRecord.getOrderSn());
                changeOrderDto.setRefundStatus(2);
                changeOrderDto.setRefundAmount(refundsRecord.getRefundAmount());
                changeOrderDto.setSuccessTime(updateBean.getSuccessTime());
                orderService.changeOrderRefundStatus(changeOrderDto);
            }
        }
    }



    /**
     * 生成退款单号
     * 字母RF+咨询师id+年月日+随机数
     * @param adminId
     * @return
     */
    public String genRfOrderSn(String adminId) {
        String id = String.format("%06d", RandomUtils.nextInt(0, 1000000));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyMMdd");
        String date = LocalDate.now().format(fmt);
        String orderSn = "RF" + adminId.substring(5) +  date + id;
        RefundsRecord byOrderSn = getByRfOrderSn(orderSn);
        if (Objects.isNull(byOrderSn)) {
            return orderSn;
        }
        return genRfOrderSn(adminId);
    }

    /**
     * 根据退款单号查询数据
     * @param rfOrderSn
     * @return
     */
    public RefundsRecord getByRfOrderSn(String rfOrderSn){
        return this.refundsRecordPlusDao.lambdaQuery().eq(RefundsRecord::getRefundSn,rfOrderSn).last("limit 1").one();
    }













}
