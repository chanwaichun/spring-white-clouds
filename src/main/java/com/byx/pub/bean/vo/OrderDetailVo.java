package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单详情vo
 * @author Jump
 * @date 2023/5/31 15:08:43
 */
@ApiModel(value = "订单详情vo")
@Accessors(chain = true)
@Data
public class OrderDetailVo {

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private String id;

    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String orderSn;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;
    /**
     * 下单人id
     */
    @ApiModelProperty(value = "下单人id")
    private String userId;
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
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String adminName;

    /**
     * 咨询师手机
     */
    @ApiModelProperty(value = "咨询师手机")
    private String adminMobile;

    /**
     * 咨询师头像
     */
    @ApiModelProperty(value = "咨询师头像")
    private String adminImg;

    /**
     * 支付类型:true 全额支付，false 分期支付
     */
    @ApiModelProperty(value = "支付类型:true 全额支付，false 分期支付")
    private Boolean fullPay;

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
     * 剩余支付金额
     */
    @ApiModelProperty(value = "剩余支付金额")
    private BigDecimal surplusAmount;

    /**
     * 本次支付金额
     */
    @ApiModelProperty(value = "本次支付金额")
    private BigDecimal thisPayment;

    /**
     * 订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)
     */
    @ApiModelProperty(value = "订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)")
    private Integer status;

    /**
     * 退款状态(0->无退款，1->退款中，2->完成退款)
     */
    @ApiModelProperty(value = "退款状态(0->无退款，1->退款中，2->完成退款)")
    private Integer refundStatus;

    /**
     * 退款类型(1：部分退款，2：全额退款)
     */
    @ApiModelProperty(value = "退款类型(1：部分退款，2：全额退款)")
    private Integer refundType;

    /**
     * 退款时间
     */
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    /**
     * 累计退款金额
     */
    @ApiModelProperty(value = "累计退款金额")
    private BigDecimal refundAmount;

    /**
     * 支付记录列表
     */
    @ApiModelProperty(value = "支付记录列表")
    private List<PayRecordVo> payRecordVoList = new ArrayList<>();

    /**
     * 订单商品列表
     */
    @ApiModelProperty(value = "订单商品列表")
    private List<OrderItemVo> itemVoList = new ArrayList<>();

    /**
     * 协议图片
     */
    @ApiModelProperty(value = "协议图片")
    private String protocol;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 小红书订单id
     */
    @ApiModelProperty(value = "小红书订单id")
    private String xhsOrderId;
}
