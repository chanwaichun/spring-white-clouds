package com.byx.pub.enums;

import lombok.Getter;

/**
 * @Author Jump
 * @Date 2023/7/26 22:04
 */
@Getter
public enum ProductTypeEnum {
    COURSE("课程",1),
    SERVICE("服务",2),
    GOODS("实物",3);

    private  String name;
    private  Integer value;

    ProductTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
