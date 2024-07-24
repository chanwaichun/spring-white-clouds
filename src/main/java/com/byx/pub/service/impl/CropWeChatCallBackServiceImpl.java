package com.byx.pub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.byx.pub.bean.crop.*;
import com.byx.pub.enums.CacheKey;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.CropPermanentCodePlusDao;
import com.byx.pub.plus.entity.CropPermanentCode;
import com.byx.pub.service.CropWeChatCallBackService;
import com.byx.pub.service.RedisService;
import com.byx.pub.util.AESUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author Jump
 * @date 2023/8/7 15:52:06
 */
@Service
@Slf4j
public class CropWeChatCallBackServiceImpl implements CropWeChatCallBackService {

    @Resource
    private RedisService redisService;
    @Resource
    CropPermanentCodePlusDao permanentCodePlusDao;
    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Value("${CorpWeChat.corpId}")
    private String corpId;
    //第三方应用参数
    @Value("${CorpWeChat.suite.suiteId}")
    private String suiteId;
    @Value("${CorpWeChat.suite.secret}")
    private String suiteSecret;
    @Value("${CorpWeChat.suite.token}")
    private String suiteToken;
    @Value("${CorpWeChat.suite.encodingAESKey}")
    private String suiteEncodingAESKey;

    /**
     * 获取第三方应用凭证（suite_access_token）
     *  参考文献：https://developer.work.weixin.qq.com/document/10975#%E8%AE%BE%E7%BD%AE%E6%8E%88%E6%9D%83%E9%85%8D%E7%BD%AE
     */
    private String weCom_suite_accessToken = "https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token";
    /**
     * 获取企业永久授权码
     */
    private String weCom_permanent_accessToken = "https://qyapi.weixin.qq.com/cgi-bin/service/get_permanent_code?suite_access_token=";
    /**
     * 获取企业access_token
     */
    private String weCom_corp_accessToken = "https://qyapi.weixin.qq.com/cgi-bin/service/get_corp_token?suite_access_token=";
    /**
     * 获取企业微信用户信息
     */
    private String weCom_oauth_userinfo = "https://qyapi.weixin.qq.com/cgi-bin/service/auth/getuserinfo3rd";


    /**
     * 获取第三方应用凭证
     * @param suiteTicket
     * @return
     */
    public AccessTokenDto getSuiteAccessToken(String suiteTicket) {
        SuiteTokenDto suiteTokenDto = new SuiteTokenDto();
        suiteTokenDto.setSuite_id(suiteId);
        suiteTokenDto.setSuite_secret(suiteSecret);
        suiteTokenDto.setSuite_ticket(suiteTicket);
        String jsonStr = restTemplate.postForObject(weCom_suite_accessToken, suiteTokenDto, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if (!StringUtil.cropResStatus(jsonObject)) {
            throw new ApiException("应用凭证获取失败，" + jsonObject.getString("errmsg"));
        }
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setAccessToken(jsonObject.getString("suite_access_token"));
        accessTokenDto.setExpiresIn(jsonObject.getString("expires_in"));
        return accessTokenDto;
    }

    /**
     * 验证URL
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timeStamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param echoStr      随机串，对应URL参数的echostr
     * @return 解密之后的echostr
     */
    public String weComVerify(String msgSignature, String timeStamp, String nonce, String echoStr) {
        String signature = AESUtil.getSHA1(suiteToken, timeStamp, nonce, echoStr);
        if (!signature.equals(msgSignature)) {
            log.error("企业微信签名验证错误");
            return null;
        }
        return AESUtil.weComDecrypt(echoStr, suiteEncodingAESKey, corpId);
    }

    /**
     * 企业微信推送的ticket(实际有效期为30分钟)
     * @param msgSignature
     * @param timeStamp
     * @param nonce
     * @param in
     * @return
     */
    public String weComCommand(String msgSignature, String timeStamp, String nonce, InputStream in) {
        log.info("----进入到企业微信推送ticket-----");
        ReceiveDto receive = new ReceiveDto();
        receive.setReceiveId(suiteId);
        receive.setToken(suiteToken);
        receive.setEncodingAesKey(suiteEncodingAESKey);
        try {
            log.info("----开始读取输入流-----");
            // 密文，对应POST请求的数据
            String postData = "";
            //1.获取加密的请求消息：使用输入流获得加密请求消息postData
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //作为输出字符串的临时串，用于判断是否读取完毕
            String tempStr = "";
            while (null != (tempStr = reader.readLine())) {
                postData += tempStr;
            }
            //解密消息
            log.info("----加密消息内容-----msgSignature："+msgSignature);
            log.info("----加密消息内容-----timeStamp："+timeStamp);
            log.info("----加密消息内容-----nonce："+nonce);
            log.info("----加密消息内容-----postData："+postData);
            String msg = weComDecryptMsg(msgSignature, timeStamp, nonce, postData, receive);
            log.info("----解密消息结果：-----"+msg);
            JSONObject json = JSONObject.parseObject(msg);
            String suiteTicket = json.getString("SuiteTicket");
            // 临时授权码,用于获取企业的永久授权码
            String authCode = json.getString("AuthCode");
            log.info("----suiteTicket:"+suiteTicket+",authCode:"+authCode);
            //suiteToken 存放redis
           /* if (suiteTicket != null) {
                redisService.set(CacheKey.Cache_WeCom_Suite_Ticket, suiteTicket, CacheKey.t10min);
            } else {
                suiteTicket = redisService.get(CacheKey.Cache_WeCom_Suite_Ticket);
            }
            //获取第三方应用凭证
            String suiteAccessToken = redisService.get(CacheKey.Cache_WeCom_Suite_Access_Token);
            if (suiteAccessToken == null) {
                AccessTokenDto tokenDto = getSuiteAccessToken(suiteTicket);
                suiteAccessToken = tokenDto.getAccessToken();
                redisService.set(CacheKey.Cache_WeCom_Suite_Access_Token, suiteAccessToken, CacheKey.t1hour);
            }
            //查插永久授权码
            if (authCode != null) {
                savePermanentCode(authCode, suiteAccessToken);
            }*/
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密企业微信消息
     * @param msgSignature
     * @param timeStamp
     * @param nonce
     * @param postData
     * @param receive
     * @return
     */
    public String weComDecryptMsg(String msgSignature, String timeStamp, String nonce, String postData, ReceiveDto receive) {
        // 提取密文
        Object[] encrypt;
        if (postData.contains("<xml>")) {
            encrypt = AESUtil.extractXML(postData);
        } else {
            encrypt = AESUtil.extractJSON(postData);
        }

        // 验证安全签名
        String signature = AESUtil.getSHA1(receive.getToken(), timeStamp, nonce, encrypt[1].toString());
        if (!signature.equals(msgSignature)) {
            log.error("签名验证错误");
            return null;
        }

        // 解密
        String result = AESUtil.weComDecrypt(encrypt[1].toString(), receive.getEncodingAesKey(), receive.getReceiveId());
        return result;
    }

    /**
     * 授权，通过code换取用户信息
     * @param code
     * @param state
     * @return
     */
    public OauthDto oauth(String code, String state) {
        log.info("-----进入到扫码授权回调函数-----");
        //校验state 防止攻击
        if(Objects.isNull(redisService.get(state))){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"二维码超时,请刷新重试");
        }
        redisService.del(state);
        //获取第三方token
        String suiteTicket = redisService.get(CacheKey.Cache_WeCom_Suite_Ticket);
        AccessTokenDto tokenDto = getSuiteAccessToken(suiteTicket);
        String suiteAccessToken = tokenDto.getAccessToken();
        redisService.set(CacheKey.Cache_WeCom_Suite_Access_Token, suiteAccessToken, CacheKey.t1hour);
        log.info("-----重新用ticket获取得到suiteAccessToken ："+suiteAccessToken);

        //获取企业微信用户信息
        String userInfoStr = getWeComUserInfo(code, suiteAccessToken);
        /*JSONObject jsonObject = JSONObject.parseObject(userInfoStr);
        String corpId = jsonObject.getString("corpid");
        String openUserid = jsonObject.getString("open_userid");*/
        OauthDto oauthDto = new OauthDto();
        //oauthDto.setOpenUserId(openUserid);
        //oauthDto.setAuthCorpId(corpId);
        oauthDto.setResStr(userInfoStr);
        return oauthDto;
    }

    /**
     * 获取企业微信用户信息
     * @param code
     * @param suiteAccessToken
     * @return
     */
    public String getWeComUserInfo(String code, String suiteAccessToken) {
        log.info("-----通过企业微信接口，code："+code);
        log.info("-----通过企业微信接口，suiteAccessToken："+suiteAccessToken);
        String url = weCom_oauth_userinfo + "?suite_access_token=" + suiteAccessToken + "&code=" + code;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        log.info("-----企业微信接口返回："+responseEntity);
        return responseEntity.getBody();
    }

    /**
     * 获取企业AccessToken
     * @param authCorpId
     * @return
     */
    public String getAccessToken(String authCorpId) {
      /*  String accessToken = CacheManager.get(CacheKey.Cache_WeCom_Access_Token + authCorpId);
        if (accessToken == null) {
            // todo 缓存中没有，先到数据库中拿，数据库中的已过期，则通过永久码permanentCode，应用凭证suiteAccessToken获取
            if (accessToken != null) {
                CacheManager.set(CacheKey.Cache_WeCom_Access_Token + authCorpId, accessToken, CacheKey.t1hour); // 新获取到的accessToken有效期是2小时
            }
        }
        return accessToken;*/
        return "";
    }


    /**
     * 保存企业永久码
     * @param authCode
     * @param suiteAccessToken
     */
    private void savePermanentCode(String authCode, String suiteAccessToken) {
        log.info("----进入到保存企业永久授权码环节-----authCode："+authCode+",suiteAccessToken:"+suiteAccessToken);
        //1.缓存临时授权码
        redisService.set(CacheKey.Cache_WeCom_Auth_Code + suiteId, authCode, CacheKey.t5min);
        //2.调用接口获取永久授权码
        String permanentCodeStr = getPermanentCode(authCode, suiteAccessToken);
        JSONObject permanentCodeJson = JSONObject.parseObject(permanentCodeStr);
        String permanentCode = permanentCodeJson.getString("permanent_code");
        String corpInfoStr = permanentCodeJson.getString("auth_corp_info");
        String accessToken = permanentCodeJson.getString("access_token");
        JSONObject corpInfoJson = JSONObject.parseObject(corpInfoStr);
        String authCorpId = corpInfoJson.getString("corpid");
        //3.查插数据库
        CropPermanentCode cropPermanentCode = this.permanentCodePlusDao.lambdaQuery()
                .eq(CropPermanentCode::getCropId, authCorpId).last("limit 1").one();
        if(Objects.isNull(cropPermanentCode)){
            cropPermanentCode = new CropPermanentCode();
            cropPermanentCode.setCropId(authCorpId);
            cropPermanentCode.setPermanentCode(permanentCode);
            cropPermanentCode.setAuthCorpInfo(corpInfoStr);
            cropPermanentCode.setAccessToken(accessToken);
            this.permanentCodePlusDao.save(cropPermanentCode);
            return;
        }
        cropPermanentCode.setCropId(authCorpId);
        cropPermanentCode.setPermanentCode(permanentCode);
        cropPermanentCode.setAuthCorpInfo(corpInfoStr);
        cropPermanentCode.setAccessToken(accessToken);
        this.permanentCodePlusDao.updateById(cropPermanentCode);
    }

    /**
     * 获取企业永久授权码
     * authCode 临时授权码
     */
    private String getPermanentCode(String authCode, String suiteToken) {
        PermanentCodeDto permanentCodeDto = new PermanentCodeDto();
        permanentCodeDto.setAuth_code(authCode);
        String url = weCom_permanent_accessToken + suiteToken;
        return restTemplate.postForObject(url, permanentCodeDto, String.class);
    }


}
