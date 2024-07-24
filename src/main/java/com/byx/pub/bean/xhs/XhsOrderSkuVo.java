package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/12/29 23:31
 */
@Data
public class XhsOrderSkuVo {
    /**
     * 商品id
     */
    private String skuId;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品图片url
     */
    private String skuImage;
    /**
     * 商品数量
     */
    private Integer skuQuantity;
    /**
     * 总支付金额（考虑总件数）商品总实付 单位分
     */
    private Integer totalPaidAmount;
}
