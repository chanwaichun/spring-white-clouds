package com.byx.pub.enums;

import lombok.Getter;

/**
 * 订单状态
 * @author Jump
 * @date 2023/5/29 18:04:47
 */
@Getter
public enum OrderStatusEnum {
    CLOSED(0, "已取消"),
    NOT_PAY(1, "待支付"),
    PART_PAY(2,"部分支付"),
    PAY_SUCCESS(3, "已支付"),
    ORDER_SUCCESS(4, "已完成"),
    ORDER_SETTLEMENT(5, "已结算");

    private Integer code;
    private String desc;

    private OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
