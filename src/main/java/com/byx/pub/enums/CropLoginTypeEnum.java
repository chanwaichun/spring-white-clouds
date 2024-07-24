package com.byx.pub.enums;

import lombok.Getter;

/**
 * @Author Jump
 * @Date 2023/8/3 22:32
 */
@Getter
public enum CropLoginTypeEnum {
    BYX_MANEGE("服务商","ServiceApp"),
    BYX_FRONT("前端","CorpApp");

    private  String name;
    private  String value;

    CropLoginTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }



}
