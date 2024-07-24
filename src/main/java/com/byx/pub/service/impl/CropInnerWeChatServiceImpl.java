package com.byx.pub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.byx.pub.enums.CropInnerFinalUrl;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.BusinessCropConfigPlusDao;
import com.byx.pub.plus.entity.BusinessCropConfig;
import com.byx.pub.service.CropInnerWeChatService;
import com.byx.pub.service.RedisService;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author Jump
 * @Date 2023/8/13 23:51
 */
@Service
@Slf4j
public class CropInnerWeChatServiceImpl implements CropInnerWeChatService {
    @Resource
    BusinessCropConfigPlusDao cropConfigPlusDao;
    @Resource
    RedisService redisService;
    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;




    /**
     * 生成企业微信二维码(内部-企微打开链接授权)
     * @return
     */
    public String genInnerCropQr(String businessId){
        BusinessCropConfig cropConfig = getCropConfig(businessId);
        //企业微信生成二维码请求地址
        String qrCodeUrl = "https://login.work.weixin.qq.com/wwlogin/sso/login?" +
                "login_type=CorpApp" +
                "&appid=%s" +
                "&agentid=%s" +
                "&redirect_uri=%s" +
                "&state=%s";
        //这里这样做是因为多次的使用会导致url变化
        String callBackUrl = "https://white-clouds-dev.dongliuxinli.com/white/clouds/manage/v1/crop/inner/wechat/crop/callback";
        try {
            //使用urlEncode对链接进行处理
            callBackUrl = URLEncoder.encode(callBackUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"生成二维码失败");
        }
        //向%s位置传递参数值
        return String.format(
                qrCodeUrl,
                cropConfig.getCropId(),
                cropConfig.getAgentId(),
                callBackUrl,
                businessId
        );
    }

    /**
     * 获取商家AccessToken
     * @param sjId
     * @return
     */
    public String getAccessTokenBySj(String sjId){
        //1.从缓存拿
        if(redisService.hasKey(CropInnerFinalUrl.ACCESS_TOKEN_KEY+sjId)){
            return redisService.get(CropInnerFinalUrl.ACCESS_TOKEN_KEY + sjId);
        }
        //2.从企微拿
        BusinessCropConfig cropConfig = getCropConfig(sjId);
        //向%s位置传递参数值
        String reqUrl = String.format(CropInnerFinalUrl.ACCESS_TOKEN_URL, cropConfig.getCropId(), cropConfig.getSecret());
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(reqUrl, String.class);
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
        if(!StringUtil.cropResStatus(jsonObject)){
            throw new ApiException("获取企业accessToken失败，msg:" + jsonObject.getString("errmsg"));
        }
        String accessToken = jsonObject.getString("access_token");
        redisService.set(CropInnerFinalUrl.ACCESS_TOKEN_KEY + sjId,accessToken,CropInnerFinalUrl.HOUR_2);
        return accessToken;
    }


    /**
     * 获取商家配置
     * @param sjId
     * @return
     */
    public BusinessCropConfig getCropConfig(String sjId){
        if(StringUtil.isEmpty(sjId)){
            sjId = SystemFinalCode.ONE_VALUE.toString();
        }
        BusinessCropConfig config = this.cropConfigPlusDao.lambdaQuery()
                .eq(BusinessCropConfig::getBusinessId, sjId).last("limit 1").one();
        if(Objects.isNull(config)){
            throw new ApiException("未找到商家配置");
        }
        return config;
    }

    /**
     * 企微授权回调
     * @param state
     * @param code
     * @return
     */
    public String cropCallbackUrl(String state,String code){
        //获取企业accessToken
        String accessToken = this.getAccessTokenBySj(state);
        //获取用户信息
        String reqUrl = String.format(CropInnerFinalUrl.CODE_TO_USER_URL, accessToken, code);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(reqUrl, String.class);
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
        if(!StringUtil.cropResStatus(jsonObject)){
            throw new ApiException("获取用户信息失败，msg:" + jsonObject.getString("errmsg"));
        }
        String userId = jsonObject.getString("userid");
        return userId;
    }


    /**
     * 生成企业微信二维码(内部-企微打开链接授权)
     * @return

    public String genInnerCropQr(){
    BusinessCropConfig cropConfig = getCropConfig("");
    //企业微信生成二维码请求地址
    String qrCodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
    "appid=%s" +
    "&redirect_uri=%s" +
    "&response_type=code" +
    "&scope=snsapi_base" +
    "&state=%s"+
    "&agentid=%s" +
    "#wechat_redirect";
    //这里这样做是因为多次的使用会导致url变化
    String callBackUrl = "https://white-clouds-dev.dongliuxinli.com/white/clouds/manage/v1/crop/inner/wechat/crop/callback";
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
    cropConfig.getCropId(),
    callBackUrl,
    state,
    cropConfig.getAgentId()
    );
    }*/
}
