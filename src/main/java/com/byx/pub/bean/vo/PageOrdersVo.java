package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分页查询订单数据vo
 * @author Jump
 * @date 2023/5/29 17:51:09
 */
@Data
@Accessors(chain = true)
public class PageOrdersVo {

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
     * 退款类型(1：部分退款，2：全额退款)
     */
    @ApiModelProperty(value = "退款类型(1：部分退款，2：全额退款)")
    private Integer refundType;
    /**
     * 退款状态(0->无退款，1->退款中，2->完成退款)
     */
    @ApiModelProperty(value = "退款状态(0->无退款，1->退款中，2->完成退款)")
    private Integer refundStatus;
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
    /**
     * 协议状态
     */
    @ApiModelProperty(value = "协议状态(true->有，false->无)")
    private Boolean protocolStatus;

    /**
     * 小红书订单id
     */
    @ApiModelProperty(value = "小红书订单id")
    private String xhsOrderId;

}
