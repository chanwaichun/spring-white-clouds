package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2024/1/17 1:22
 */
@Data
public class DecryptedReceiverVo {

    private String decryptedData;

    private String dataTag;

    private String encryptedData;

    private String errorCode;

}
