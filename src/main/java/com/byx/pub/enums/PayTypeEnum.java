package com.byx.pub.enums;

import lombok.Getter;

/**
 * @Author Jump
 * @Date 2023/6/25 22:17
 */
@Getter
public enum PayTypeEnum {
    PAY_ALL(1, "全款支付"),
    PAY_THIS(2, "本期支付"),
    PAY_SURPLUS(3, "支付尾款");

    private Integer code;
    private String desc;

     PayTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
