package com.byx.pub.enums;

import lombok.Getter;

/**
 * @author jump
 * @date 2020/11/11 20:15
 */
@Getter
public enum ClientTypeEnum {

    BYX_MANEGE("管理后台","manage"),
    BYX_FRONT("前端","front");

    private  String name;
    private  String value;

    ClientTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
