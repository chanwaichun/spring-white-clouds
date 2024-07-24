package com.byx.pub.enums;

import lombok.Getter;

/**
 * 退款状态
 *
 * @program: pub-pay
 * @description: 退款状态
 * @author: Jim
 * @create: 2021-12-16
 */
@Getter
public enum RefundStatus {
    PROCESSING(1), // 退款处理中
    SUCCESS(2), // 退款成功
    ABNORMAL(3), //退款异常
    CLOSED(4); // 退款关闭

    private final int value;

    RefundStatus(int value) {
        this.value = value;
    }
}
