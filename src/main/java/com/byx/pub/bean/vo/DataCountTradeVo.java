package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/8/17 22:00
 */
@Data
public class DataCountTradeVo {

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private String payAmount;

    /**
     * 支付订单数
     */
    @ApiModelProperty(value = "支付订单数")
    private String payOrderNum;

    /**
     * 支付买家数
     */
    @ApiModelProperty(value = "支付买家数")
    private String payUserNum;

    /**
     * 客单价
     */
    @ApiModelProperty(value = "客单价")
    private String pct;

    /**
     * 下单未付款金额
     */
    @ApiModelProperty(value = "下单未付款金额")
    private String noPayOrderAmount;

    /**
     * 下单未付款订单数
     */
    @ApiModelProperty(value = "下单未付款订单数")
    private String noPayOrderNum;


}
