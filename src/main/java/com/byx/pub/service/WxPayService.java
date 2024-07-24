package com.byx.pub.service;


import com.byx.pub.bean.qo.JsapiPayQo;
import com.byx.pub.bean.qo.RefundsQo;
import com.byx.pub.bean.vo.WxJspaiPayVo;
import com.byx.pub.util.CommonResult;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

/**
 * 微信支付接口
 */
public interface WxPayService {

    /**
     * jsapi下单
     * @param jsapiPayQo 请求体
     * @return
     * @throws Exception
     */
    CommonResult<WxJspaiPayVo> jsApiPay(JsapiPayQo jsapiPayQo) throws Exception;

    /**
     * 支付结果查询
     * @param orderSn 订单号
     * @return
     * @throws Exception
     */
    Boolean queryJsApiPay(String orderSn) throws Exception;


    /**
     * 获取微信API3的HttpClient
     * @return
     * @throws Exception
     */
    CloseableHttpClient getClient() throws Exception;

    /**
     * 微信支付回调
     * @param requestBodyString
     * @return
     * @throws Exception
     */
    Map<String, String> notify(String requestBodyString) throws Exception;

    /**
     * 生成退款单号
     * 字母RF+咨询师id+年月日+随机数
     * @param adminId
     * @return
     */
    String genRfOrderSn(String adminId);

    /**
     * 微信分批次退款
     * @param refundsQo
     * @throws Exception
     */
    void splitRefundReq(RefundsQo refundsQo) throws Exception;

    /**
     * 微信退款回调
     * @param requestBodyString
     * @return
     * @throws Exception
     */
    Map<String, String> refundNotify(String requestBodyString) throws Exception;
}
