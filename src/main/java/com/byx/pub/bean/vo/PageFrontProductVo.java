package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Jump
 * @date 2023/5/22 17:16:18
 */
@Data
public class PageFrontProductVo {

    /**
     * 主键(关系表id)
     */
    @ApiModelProperty(value = "主键(关系表id)")
    private String id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String productCode;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * 售卖次数
     */
    @ApiModelProperty(value = "售卖次数")
    private Integer saleNum;

    /**
     * 商品图片分号分隔第一张为首图
     */
    @ApiModelProperty(value = "商品图片分号分隔第一张为首图")
    private String img;

    /**
     * 上下架状态(true:上架,false:下架)
     */
    @ApiModelProperty(value = "上下架状态(true:上架,false:下架)")
    private Boolean shelvesStatus;

    /**
     * 商品来源(PRODUCT_BASE:总部商品,PRODUCT_ADMIN:咨询师商品)
     */
    @ApiModelProperty(value = "商品来源(PRODUCT_BASE:总部商品,PRODUCT_ADMIN:咨询师商品)")
    private String productSource;





}
