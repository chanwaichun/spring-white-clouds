package com.byx.pub.enums;

import lombok.Getter;

/**
 * @Author Jump
 * @Date 2023/10/8 21:50
 */
@Getter
public enum PromotionTypeEnum {
    CARD_LIKES("名片点赞",1),
    DZ_FLYER("电子传单",2),
    SHARE_CARD("名片分享",3),
    SHARE_GOODS("商品分享",4),
    CREATE_ORDER("创建订单",5),
    FIRST_PAY("定金支付",6),
    BROWSE_CARD("浏览名片",7),
    BROWSE_PRODUCT("浏览商品",8);

    private  String name;
    private  Integer value;

    PromotionTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
