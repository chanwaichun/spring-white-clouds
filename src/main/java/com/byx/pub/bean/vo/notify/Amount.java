package com.byx.pub.bean.vo.notify;

import lombok.Data;

/**
 * 订单金额信息
 * @author Jump
 */
@Data
public class Amount {
    /**
     * 订单总金额，单位为分。
     * 示例值：100
     */
    private int total;
    /**
     * 用户支付金额，单位为分。
     * 示例值：100
     */
    private int payer_total;
    /**
     * CNY：人民币，境内商户号仅支持人民币。
     * 示例值：CNY
     */
    private String currency;
    /**
     * 用户支付币种
     * 示例值：CNY
     */
    private String payer_currency;
}