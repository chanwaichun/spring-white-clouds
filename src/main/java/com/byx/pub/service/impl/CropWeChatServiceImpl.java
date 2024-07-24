package com.byx.pub.service.impl;



import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.service.CropWeChatService;

import com.byx.pub.service.RedisService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;


/**
 * @author Jump
 * @date 2023/8/2 11:33:14
 */
@Service
@Slf4j
public class CropWeChatServiceImpl implements CropWeChatService {

    @Value("${CorpWeChat.callbackUrl}")
    private String cropCallbackUrl;
    @Value("${CorpWeChat.login.suiteId}")
    private String suiteId;
    @Value("${CorpWeChat.corpId}")
    private String corpId;



    @Resource
    RedisService redisService;

    /**
     * 生成企业微信二维码(第三方)
     * @return
     */
    public String genCropQr(){
        //企业微信生成二维码请求地址
        String qrCodeUrl = "https://login.work.weixin.qq.com/wwlogin/sso/login?" +
                "login_type=ServiceApp" +
                "&appid=%s" +
                "&redirect_uri=%s" +
                "&state=%s";
        //这里这样做是因为多次的使用会导致url变化
        String callBackUrl = cropCallbackUrl;
        try {
            //使用urlEncode对链接进行处理
            callBackUrl = URLEncoder.encode(callBackUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"生成二维码失败");
        }
        //使用一个随机数 防止csrf攻击（跨站请求伪造攻击）
        String state = UUID.randomUUID().toString().replaceAll("-", "");
        //将随机数存储5分钟，用于回调做对比
        redisService.set(state,state,300);
        //向%s位置传递参数值
        return String.format(
                qrCodeUrl,
                suiteId,
                callBackUrl,
                state
        );
    }








}
