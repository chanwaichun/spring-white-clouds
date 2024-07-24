package com.byx.pub.bean.qo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author Jump
 * @Date 2023/9/9 13:38
 */
@Data
@Accessors(chain = true)
public class WxRefundDto {
    /**
     * 支付记录表id
     */
    private String payRecordId;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 商家订单号(微信)
     */
    private String outTradeNo;
    /**
     * 退款单号
     */
    private String refundSn;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

}
