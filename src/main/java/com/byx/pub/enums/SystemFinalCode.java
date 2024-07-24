package com.byx.pub.enums;

import java.math.BigDecimal;

/**
 * 系统常量值
 * @Author Jump
 * @Date 2023/7/15 20:20
 */
public interface SystemFinalCode {
    /**
     * 平台商家id
     */
    String PLATFORM_BUSINESS_ID = "";
    /**
     * 平台商家名称
     */
    String PLATFORM_BUSINESS_NAME = "东流心理平台";
    /**
     * 0的字符
     */
    String ZERO_STR = "0";
    /**
     * 0的数值
     */
    Integer ZERO_VALUE = 0;
    /**
     * 1的数值
     */
    Integer ONE_VALUE = 1;
    /**
     * 10的数值
     */
    Integer TEN_VALUE = 10;
    /**
     * 20的数值
     */
    Integer TWENTY_VALUE = 20;
    /**
     * 100的数值
     */
    Integer HUNDRED_VALUE = 100;
    /**
     * 一级值
     */
    Integer MENU_LEVEL_1_VALUE = 1;
    /**
     * 二级值
     */
    Integer MENU_LEVEL_2_VALUE = 2;
    /**
     * 企业微信内部AccessToken KEY
     */
    String CORP_INNER_TOKEN = "CORP_INNER_TOKEN";
    /**
     * 第三方应用SuiteTicket缓存时间(秒)
     */
    Integer REDIS_SUITE_TICKET =  1800;
    /**
     * BigDecimal 100
     */
    BigDecimal ONE_HUNDRED = new BigDecimal(100);


    /**
     * 微信全局唯一accessToken
     */
    String WECHAT_ACCESS_TOKEN_KEY = "weChat_Access_Token";
    /**
     * 微信全局唯一accessToken缓存时间(秒)
     */
    Integer WECHAT_ACCESS_TOKEN_MIN =  7100;

    /**
     * 邀请函新跳转页面
     */
    String INVITE_PAGE = "pages/product/detail/index";

    /**
     * 商品详情页
     */
    String PRODUCT_DETAIL_PAGE = "pages/product/detail/index";

    /**
     * 名片详情页
     */
    String CARD_DETAIL_PAGE = "pagesA/businessCard/renovation/index";

    /**
     * 邀请函名片id
     */
    String INVITE_CARD_ID = "1";


    /**
     * 微信下单订阅消息模板id
     */
    String WX_PLACE_ORDER_SMS_TID = "kgnZ1O2tU9WXA7UWeztB-3acKXXZQwhjnJomnQE8t0I";
    /**
     * 微信下单订阅消息跳转页面
     */
    String WX_PLACE_ORDER_SMS_PAGE = "pages/order/detail/index?orderSn=";

    /**
     * 成功字符串
     */
    String SUCCESS_STR = "SUCCESS";

}
