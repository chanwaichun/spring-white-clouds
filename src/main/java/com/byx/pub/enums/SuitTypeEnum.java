package com.byx.pub.enums;

import lombok.Getter;

/**
 * 套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)
 * @Author Jump
 * @Date 2023/7/15 20:10
 */
@Getter
public enum SuitTypeEnum {
    PERSON("个人版",1),
    GROUP("小组版",2),
    TEAM("团队版",3),
    ENTERPRISE("企业版",4),
    ASSOCIATION("社群版",5),
    PARTNER("合伙人版",6);

    private  String name;
    private  Integer value;

    SuitTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }



}
