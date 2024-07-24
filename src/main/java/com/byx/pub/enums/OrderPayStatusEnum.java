package com.byx.pub.enums;

import lombok.Getter;

/**
 * @author Jump
 * @date 2023/6/9 11:51:52
 */
@Getter
public enum OrderPayStatusEnum {

    NOT_PAY(0, "未支付"),
    PART_PAY(1,"部分支付"),
    PAY_SUCCESS(2, "已支付"),;

    private Integer code;
    private String desc;

    private OrderPayStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
