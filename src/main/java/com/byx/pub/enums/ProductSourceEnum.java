package com.byx.pub.enums;

import lombok.Getter;

/**
 * 商品来源枚举
 * @author Jump
 * @date 2023/5/19 10:36:39
 */
@Getter
public enum ProductSourceEnum {
    PRODUCT_BASE("总部商品","PRODUCT_BASE"),
    PRODUCT_ADMIN("咨询师商品","PRODUCT_ADMIN");

    private  String name;
    private  String value;

    ProductSourceEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
