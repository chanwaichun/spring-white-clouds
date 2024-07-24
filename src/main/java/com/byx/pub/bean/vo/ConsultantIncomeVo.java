package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 咨询师收入流水vo
 * @Author Jump
 * @Date 2023/6/25 21:00
 */
@Data
@Accessors(chain = true)
public class ConsultantIncomeVo {

    /**
     * 支付流水号(微信规范32位)
     */
    @ApiModelProperty(value = "支付流水号(微信规范32位)")
    private String paySn;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String adminName;

    /**
     * 支付类型
     */
    @ApiModelProperty(value = "支付类型")
    private String payment;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private String amount;

    /**
     * 完成支付时间
     */
    @ApiModelProperty(value = "完成支付时间")
    private String successTime;




}


