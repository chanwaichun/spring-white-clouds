package com.byx.pub.service.impl;



import com.alibaba.fastjson.JSONObject;
import com.byx.pub.bean.vo.WeChatUserVo;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.enums.WeChatUrlConstants;
import com.byx.pub.exception.ApiException;
import com.byx.pub.service.RedisService;
import com.byx.pub.service.WeChatApiService;
import com.byx.pub.util.AesCbcUtil;
import com.byx.pub.util.HttpUtils;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 *微信相关服务api
 * @Author: jump
 * @Date: 2023-05-11  21:43
 */
@Service
@Slf4j
public class WeChatApiServiceImpl implements WeChatApiService {
    //网站端
    @Value("${WeChat.web.appid}")
    private String webWeChatAppId;
    @Value("${WeChat.web.secret}")
    private String webWeChatAppSecret;
    @Value("${WeChat.web.callbackUrl}")
    private String webCallbackUrl;
    //小程序端
    @Value("${WeChat.AppId}")
    private String weChatAppleId;
    @Value("${WeChat.AppSecret}")
    private String weChatAppSecret;
    @Value("${spring.servlet.multipart.location}")
    String locationStr;
    @Resource
    RedisService redisService;


    /**
     * 拼接生成微信二维码url
     * @return
     */
    public String genQrConnect(){
        //微信生成二维码请求地址
        String qrCodeUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String callBack = webCallbackUrl;
        try {
            callBack = URLEncoder.encode(webCallbackUrl,"UTF-8");//使用urlEncode对链接进行处理
        } catch (UnsupportedEncodingException e) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"生成二维码失败");
        }
        String state = UUID.randomUUID().toString().replaceAll("-", "");//使用一个随机数 防止csrf攻击（跨站请求伪造攻击）
        redisService.set(state,state,300);//将随机数存储5分钟，用于回调做对比
        //向%s位置传递参数值
        return String.format(//给前端重定向到拼接好的地址里面
                qrCodeUrl,
                webWeChatAppId,
                callBack,
                state
        );
    }


    /**
     * 使用微信回调code获取用户信息
     * @param code
     * @param state
     * @return
     */
    public WeChatUserVo getWeChatUserInfo(String code, String state){
        //校验state 防止攻击
        if(Objects.isNull(redisService.get(state))){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"二维码超时,请刷新重试");
        }
        //验完就删除key
        redisService.del(state);
        WeChatUserVo resVo = new WeChatUserVo();
        //code参数：临时票据，随机字符串，类似于手机验证码
        //state参数：生成二维码传递state值
        //1 请求微信固定地址，得到access_token和openid
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        //拼接参数
        baseAccessTokenUrl = String.format(
                baseAccessTokenUrl,
                this.webWeChatAppId,
                this.webWeChatAppSecret,
                code
        );

        //请求微信api
        String accessTokenResult = HttpUtils.requestGet(baseAccessTokenUrl,null,null);
        log.info("-------accessTokenResult: "+accessTokenResult);
        //得到access_token和openid
        Map<String, Object> returnMap = StringUtil.stringToMap(accessTokenResult);
        String accessToken = (String)returnMap.get("access_token");
        String openid = (String)returnMap.get("openid");
        resVo.setOpenId(openid);
        String unionId = (String)returnMap.get("unionid");
        resVo.setUnionId(unionId);
        Integer sex = (Integer)returnMap.get("sex");
        resVo.setUserSex(sex);


        //2 拿着acess_token和openid再去请求微信固定地址，得到扫描人信息
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        baseUserInfoUrl = String.format(
                baseUserInfoUrl,
                accessToken,
                openid
        );
        // 请求地址
        String userInfoResult = HttpUtils.requestGet(baseUserInfoUrl,null,null);
        // 解析响应
        Map<String, Object> userInfoMap =  StringUtil.stringToMap(userInfoResult);
        String nickName = (String)userInfoMap.get("nickname");
        String userImg = (String)userInfoMap.get("headimgurl");
        resVo.setNickName(nickName);
        resVo.setUserImg(userImg);
        return resVo;
    }

    /**
     * 获取微信小程序全局accessToken
     * @return
     */
    public String getWeChatAccessToken(){
        //1.查看redis有没
        if(redisService.hasKey(SystemFinalCode.WECHAT_ACCESS_TOKEN_KEY)){
            return redisService.get(SystemFinalCode.WECHAT_ACCESS_TOKEN_KEY);
        }
        //2.通过接口获取 并 缓存
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        String reqUrl = String.format(url, weChatAppleId, weChatAppSecret);
        String result = HttpUtils.requestGet(reqUrl,null,null);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String accessToken = jsonObject.getString("access_token");
        if(StringUtil.isEmpty(accessToken)){
            throw new ApiException("获取微信小程序accessToken失败");
        }
        redisService.set(SystemFinalCode.WECHAT_ACCESS_TOKEN_KEY,accessToken,SystemFinalCode.WECHAT_ACCESS_TOKEN_MIN);
        return accessToken;
    }

    /**
     * 生成拉新二维码
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
     * @param inviteUserId
     * @return
     */
    public String createQrCode(String inviteUserId){
        //获取AccessToken
        String accessToken = getWeChatAccessToken();
        //获取二维码url
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        //组装参数
        Map<String, Object> paraMap = new HashMap<>();
        //二维码携带参数 不超过32位 参数类型必须是字符串 !#$&'()*+,/:;=?@-._~
        paraMap.put("scene","&inviteUid="+inviteUserId);
        //二维码跳转页面
        paraMap.put("page", SystemFinalCode.INVITE_PAGE);
        //要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
        paraMap.put("env_version", "release");
        //执行post 获取数据流
        return HttpUtils.requestPostBody(url, JSONObject.toJSONString(paraMap), null);
    }

    /**
     * 生成拉新二维码
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
     * @param inviteUserId
     * @return
     */
    public byte[] createQrCodeByByte(String inviteUserId){
        //获取AccessToken
        String accessToken = getWeChatAccessToken();
        //获取二维码url
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        //组装参数
        Map<String, Object> paraMap = new HashMap<>();
        //二维码携带参数 不超过32位 参数类型必须是字符串 !#$&'()*+,/:;=?@-._~
        paraMap.put("scene","&inviteUid="+inviteUserId);
        //二维码跳转页面
        paraMap.put("page", SystemFinalCode.INVITE_PAGE);
        //要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
        paraMap.put("env_version", "release");
        //执行post 获取数据流
        return HttpUtils.reqPostBodyByByte(url, JSONObject.toJSONString(paraMap), null);
    }

    /**
     * 生成微信二维码并上传(通用)
     * @param page
     * @param scene
     * @return
     * @throws Exception
     */
    public String createQrCodeAndUpload(String page,String scene) throws Exception {
        StringBuffer info = new StringBuffer("https://api.weixin.qq.com/wxa/getwxacodeunlimit?");
        String accessToken = getWeChatAccessToken();
        URL url = new URL(info.append("access_token=").append(accessToken).toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
        JSONObject paramJson = new JSONObject();
        if(StringUtil.notEmpty(page)){
            paramJson.put("page", page);
        }
        paramJson.put("scene", scene);
        paramJson.put("env_version", "release");
        printWriter.write(paramJson.toString());
        // flush输出流的缓冲
        printWriter.flush();
        //开始获取数据
        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        //获取文件名 并 重命名
        String oldName = RandomUtils.nextInt(0, 9999) + ".jpg";
        String fileName = StringUtil.getFileReName(Objects.requireNonNull(oldName));
        //文件存储路径地址
        String destFileName = this.locationStr + File.separator + "productUploadImg" + File.separator + fileName;
        File file = new File(destFileName);
        file.getParentFile().mkdirs();
        OutputStream os = new FileOutputStream(file);
        int len;
        byte[] arr = new byte[1024];
        while ((len = bis.read(arr)) != -1) {
            os.write(arr, 0, len);
            os.flush();
        }
        os.close();
        return fileName;
    }




/**===================================================客户端=============================================================**/


    /**
     * 通过code获取用户openId
     * @param code
     * @return
     */
    public Map<String, Object> weChatAuth(String code){
        String url = WeChatUrlConstants.CODE_2_SESSION;
        url = String.format(url, weChatAppleId, weChatAppSecret, code);
        String responseBody = HttpUtils.requestGet(url, null, null);
        Map<String, Object> returnMap = StringUtil.stringToMap(responseBody);
        Integer errCode = (Integer) returnMap.get("errcode");
        if (errCode != null) {
            throw new ApiException(ResultCode.FAILED.getCode(),"获取用户openId信息失败");
        }
        return returnMap;
    }

    /**
     * 微信授权解密手机号
     * @param iv
     * @param encryptedData
     * @param sessionKey
     * @return
     * @throws Exception
     */
    public String getWxMobile(String iv, String encryptedData,String sessionKey){
        try {
            byte[] resultByte = AesCbcUtil.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if (Objects.isNull(resultByte)) {
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(), "手机号授权失败");
            }
            String userInfo = new String(resultByte, "UTF-8");
            log.info("手机号授权数据为" + userInfo);
            Map<String, Object> map = StringUtil.stringToMap(userInfo);
            //获取手机号码
            return String.valueOf(map.get("phoneNumber"));
        }catch (Exception e){
            e.printStackTrace();
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(), "手机号授权失败");
        }
    }





}
