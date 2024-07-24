package com.byx.pub.enums;

/**
 * @author Jump
 * @date 2023/8/14 16:46:53
 */
public interface CropInnerFinalUrl {
    //获取access_token url
    String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    //企业access_token Key
    String ACCESS_TOKEN_KEY = "crop:inner:accessToken";
    //key 缓存时间 7200秒
    Integer HOUR_2 = 2 * 60 * 60;

    //授权用户信息 url
    String CODE_TO_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token=%s&code=%s";



}
