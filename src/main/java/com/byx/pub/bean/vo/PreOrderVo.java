package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 预订单\待支付订单Vo
 * @author Jump
 * @date 2023/5/24 16:17:31
 */
@Accessors(chain = true)
@Data
public class PreOrderVo {

    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String orderSn;

    /**
     * 订单状态(0->已取消，1->待支付，2->已支付，3->已完成)
     */
    @ApiModelProperty(value = "订单状态(0->已取消，1->待支付，2->已支付，3->已完成)")
    private Integer status;

    /**
     * 是否支付(0->未支付，1->已支付)
     */
    @ApiModelProperty(value = "是否支付(0->未支付，1->已支付)")
    private Boolean payStatus;

    /**
     * 支付方式(1->微信支付，2->支付宝)
     */
    @ApiModelProperty(value = "支付方式(1->微信支付，2->支付宝)")
    private Integer payment;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String adminName;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;

    /**
     * 下单手机
     */
    @ApiModelProperty(value = "下单手机")
    private String userMobile;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

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
     * 协议图片
     */
    @ApiModelProperty(value = "协议图片")
    private String protocol;

}
