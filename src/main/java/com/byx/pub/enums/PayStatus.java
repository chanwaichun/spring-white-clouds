package com.byx.pub.enums;

import lombok.Getter;

/**
 * 支付状态
 *
 * @program: pub-pay
 * @description: 支付状态
 * @author: Jim
 * @create: 2021-12-10
 */
@Getter
public enum PayStatus {
    NOTPAY(1, "未支付"),
    SUCCESS(2, "支付成功"),
    REFUND(3, "转入退款"),
    CLOSED(4, "已关闭"),
    REVOKED(5, "已撤销"),
    USERPAYING(6, "用户支付中"),
    PAYERROR(7, "支付失败");

    private Integer code;
    private String desc;

    private PayStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
