package com.byx.pub.bean.vo.notify;

import lombok.Data;

/**
 * 支付者
 * @author Jump
 */
@Data
public class Payer {
    /**
     * 用户在直连商户appid下的唯一标识。
     * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     */
    private String openid;
}