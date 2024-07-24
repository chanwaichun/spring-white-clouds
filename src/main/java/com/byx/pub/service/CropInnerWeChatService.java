package com.byx.pub.service;

/**
 * @Author Jump
 * @Date 2023/8/13 23:51
 */
public interface CropInnerWeChatService {

    /**
     * 生成企业微信二维码(内部)
     * @return
     */
    String genInnerCropQr(String businessId);

    /**
     * 企微授权回调
     * @param state
     * @param code
     * @return
     */
    String cropCallbackUrl(String state,String code);
}
