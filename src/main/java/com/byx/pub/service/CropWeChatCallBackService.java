package com.byx.pub.service;

import com.byx.pub.bean.crop.OauthDto;
import com.byx.pub.bean.crop.ReceiveDto;

import java.io.InputStream;

/**
 * @author Jump
 * @date 2023/8/7 15:52:19
 */
public interface CropWeChatCallBackService {


    String weComVerify(String msgSignature, String timeStamp, String nonce, String echoStr);

    String weComCommand(String msgSignature, String timeStamp, String nonce, InputStream in);

    OauthDto oauth(String code, String state);

    /**
     * 获取授权凭证
     */
    String getAccessToken(String authCorpId);


    /**
     * 解密企业微信消息
     * @param msgSignature
     * @param timeStamp
     * @param nonce
     * @param postData
     * @param receive
     * @return
     */
    String weComDecryptMsg(String msgSignature, String timeStamp, String nonce, String postData, ReceiveDto receive);

}
