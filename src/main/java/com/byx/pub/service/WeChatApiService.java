package com.byx.pub.service;

import com.byx.pub.bean.vo.WeChatUserVo;

import java.util.Map;

/**
 * @Author: jump
 * @Date: 2023-05-11  22:05
 */
public interface WeChatApiService {

    /**
     * 生成微信扫码登录二维码
     * @return
     */
    String genQrConnect();

    /**
     * 使用微信回调code获取用户信息
     * @param code
     * @param state
     * @return
     */
    WeChatUserVo getWeChatUserInfo(String code, String state);

    /**
     * 通过code获取用户openId
     * @param code
     * @return
     */
    Map<String, Object> weChatAuth(String code);

    /**
     * 微信授权解密手机号
     * @param iv
     * @param encryptedData
     * @param sessionKey
     * @return
     * @throws Exception
     */
    String getWxMobile(String iv, String encryptedData,String sessionKey);

    /**
     * 生成拉新二维码
     * @param inviteUserId
     * @return
     */
    String createQrCode(String inviteUserId);

    /**
     * 获取微信小程序全局accessToken
     * @return
     */
    String getWeChatAccessToken();

    /**
     * 生成拉新二维码
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
     * @param inviteUserId
     * @return
     */
    byte[] createQrCodeByByte(String inviteUserId);

    /**
     * 生成微信二维码并上传(通用)
     * @param page
     * @param scene
     * @return
     * @throws Exception
     */
    String createQrCodeAndUpload(String page,String scene) throws Exception ;
}
