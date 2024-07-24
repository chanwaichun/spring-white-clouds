package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 订单商品VO
 * @author Jump
 * @date 2023/5/31 15:16:20
 */
@Accessors(chain = true)
@Data
@ApiModel(value = "订单商品vo")
public class OrderItemVo {

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    private String productId;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer quantity;

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String img;

    /**
     * 小计
     */
    @ApiModelProperty(value = "小计")
    private BigDecimal totalAmount;


}
