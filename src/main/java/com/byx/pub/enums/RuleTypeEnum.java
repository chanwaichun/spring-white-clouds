package com.byx.pub.enums;

import lombok.Getter;

/**
 * @Author Jump
 * @Date 2023/9/20 21:16
 */
@Getter
public enum RuleTypeEnum {
    ROLE_TARGET("角色",1),
    PERSON_TARGET("个人",2);

    private  String name;
    private  Integer value;

    RuleTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

}
