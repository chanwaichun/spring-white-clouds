package com.byx.pub.enums;

/**
 * @author Jamin
 * @Date 2023/2/3 12:53
 */
public class CacheKey {
    public static final int t1min = 60;
    public static final int t5min = 5 * 60;
    public static final int t10min = 10 * 60;
    public static final int t30min = 30 * 60;
    public static final int t1hour = 60 * 60;
    //保存流程 6小时
    public static final int t6hour = 6 * 60 * 60;
    public static final int t1Day = 24 * 60 * 60;
    public static final int t1Month = 24 * 60 * 60 * 30;

    public static final String Cache_WeCom_Suite_Ticket = "cache:weCom:suiteTicket";
    public static final String Cache_WeCom_Suite_Access_Token = "cache:weCom:suiteAccessToken";
    public static final String Cache_WeCom_Auth_Code = "cache:weCom:authCode";


    public static final String Cache_WeCom_Access_Token = "cache:weCom:accessToken";
    public static final String Cache_WeCom_JsApi_ticket = "cache:weCom:jsApiTicket";
}
