package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jump
 * @date 2023/8/4 15:28:54
 */
@Data
public class CardProductPageVo {
    /**
     * 广告主键
     */
    @ApiModelProperty(value = "广告主键")
    private String id;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 广告产品id
     */
    @ApiModelProperty(value = "广告产品id")
    private String productId;

    /**
     * 广告类型(1：广告商品，2：广告图)
     */
    @ApiModelProperty(value = "广告类型(1：广告商品，2：广告图)")
    private Integer posterType;


    /**
     * 广告位路径
     */
    @ApiModelProperty(value = "广告位路径(图片)")
    private String posterUrl;

    /**
     * 产品列表
     */
    @ApiModelProperty(value = "产品列表")
    List<ProductPageListVo> cardProductList = new ArrayList<>();

}
