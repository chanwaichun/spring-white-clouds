package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/8/18 0:03
 */
@Data
public class DataCountTradeOrderVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String orderSn;
    /**
     * 订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)
     */
    @ApiModelProperty(value = "订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)")
    private Integer status;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;
    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private Date paymentTime;
    /**
     * 支付状态(0->未支付，1->部分支付，2->已支付)
     */
    @ApiModelProperty(value = "支付状态(0->未支付，1->部分支付，2->已支付)")
    private Integer payStatus;
    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;
    /**
     * 已支付金额
     */
    @ApiModelProperty(value = "已支付金额")
    private BigDecimal paidAmount;
}
