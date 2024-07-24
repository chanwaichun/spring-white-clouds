package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2024/1/17 0:52
 */
@Data
public class BaseInfoBo {
    /**
     * 标签，订单场景为packageId（具体订单号）
     */
    private String dataTag;

    /**
     * 密文数据
     */
    private String encryptedData;

}
