package com.byx.pub.enums;

import lombok.Getter;

/**
 * @Author Jump
 * @Date 2023/9/20 21:18
 */
@Getter
public enum SettlementTypeEnum {
    PULL("拉新",1),
    FACILITATE("促成",2);

    private  String name;
    private  Integer value;

    SettlementTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
