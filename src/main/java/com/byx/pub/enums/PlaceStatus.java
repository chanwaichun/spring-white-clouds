package com.byx.pub.enums;

import lombok.Getter;

/**
 * 下单状态
 *
 * @program: pub-pay
 * @description: 下单状态
 * @author: Jim
 * @create: 2021-12-14
 */
@Getter
public enum PlaceStatus {
    SUCESS(1, "下单成功");

    private Integer code;
    private String desc;

    private PlaceStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
