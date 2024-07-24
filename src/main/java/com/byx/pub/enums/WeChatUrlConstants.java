package com.byx.pub.enums;

/**
 * 微信相关接口请求地址
 * @author Jump
 * @date 2023/5/26 13:47:24
 */
public class WeChatUrlConstants {

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_VALUE = "application/json";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String CONTENT_TYPE_VALUE = "application/json; charset=utf-8";
    public static final String ENCODING_VALUE = "UTF-8";
    public static final String DESCRIPTION = "东流心理订单";
    public static final String PACKAGEVALUE = "Sign=WXPay";
    public static final String SIGNTYPE = "RSA";
    public static final String CURRENCY = "CNY";


    //小程序code获取openid
    public final static String CODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    //微信支付v3 jsapi下单
    public final static String PAY_V3_JSAPI = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

    //微信支付v3 通过商户订单号查询订单
    public final static String PAY_V3_QUERY_OUT = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/";

    //微信支付v3 申请退款
    public final static String PAY_V3_REFUND = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

    //微信支付v3 查询单笔退款
    public final static String PAY_V3_QUERY_REFUND = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/%s";

    //微信支付v3 支付通知接口地址
    public final static String PAY_V3_NOTIFY = "https://xxx.com/api/wechatPay/v3/wechatPayNotify";

    //微信支付v3 退款通知接口地址
    public final static String PAY_V3_REFUND_NOTIFY = "https://xxx.com/api/wechatPay/v3/wechatRefundNotify";
}