package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/10 12:35
 */
@Data
public class PageRefundOrderVo {
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    /**
     * 退款单号(逗号隔开)
     */
    @ApiModelProperty(value = "退款单号(逗号隔开)")
    private String refundSn;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;
    /**
     * 退款时间
     */
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;
    /**
     * 商品图片分号分隔第一张为首图
     */
    @ApiModelProperty(value = "商品图片分号分隔第一张为首图")
    private String img;
    /**
     * 退款状态(0->无退款，1->退款中，2->完成退款)
     */
    @ApiModelProperty(value = "退款状态(0->无退款，1->退款中，2->完成退款)")
    private Integer refundStatus;
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
    /**
     * 本次支付金额
     */
    @ApiModelProperty(value = "本次支付金额")
    private BigDecimal thisPayment;
    /**
     * 剩余支付金额
     */
    @ApiModelProperty(value = "剩余支付金额")
    private BigDecimal surplusAmount;
    /**
     * 累计退款金额
     */
    @ApiModelProperty(value = "累计退款金额")
    private BigDecimal refundAmount;
    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String adminName;
    /**
     * 商家手机
     */
    @ApiModelProperty(value = "商家手机")
    private String adminMobile;
    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;
    /**
     * 下单人手机
     */
    @ApiModelProperty(value = "下单人手机")
    private String userMobile;

}
