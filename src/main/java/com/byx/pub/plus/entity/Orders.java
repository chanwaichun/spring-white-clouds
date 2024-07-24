package com.byx.pub.plus.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;

/**
 * <p>
 * 订单主表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Orders对象", description="订单主表")
public class Orders extends Model<Orders> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 创建者
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 更新者
     */
    @TableField(value = "updator", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String orderSn;

    /**
     * 订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成(完全支付、手动完成 = 待结算)，5->已结算)
     */
    @ApiModelProperty(value = "订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成(完全支付、手动完成 = 待结算)，5->已结算)")
    private Integer status;

    /**
     * 支付状态(0->未支付，1->部分支付，2->已支付)
     */
    @ApiModelProperty(value = "支付状态(0->未支付，1->部分支付，2->已支付)")
    private Integer payStatus;

    /**
     * 支付方式(1->微信支付，2->支付宝)
     */
    @ApiModelProperty(value = "支付方式(1->微信支付，2->支付宝)")
    private Integer payment;

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
     * 退款单号(逗号隔开)
     */
    @ApiModelProperty(value = "退款单号(逗号隔开)")
    private String refundSn;

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
     * 完成支付流水号
     */
    @ApiModelProperty(value = "完成支付流水号")
    private String paySn;

    /**
     * 订单完成时间
     */
    @ApiModelProperty(value = "订单完成时间")
    private Date paymentTime;

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

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
     * 下单人openid
     */
    @ApiModelProperty(value = "下单人openid")
    private String openId;

    /**
     * 第一次支付时间
     */
    @ApiModelProperty(value = "第一次支付时间")
    private Date firstPayTime;

    /**
     * 首付金额
     */
    @ApiModelProperty(value = "首付金额")
    private BigDecimal firstPayMoney;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String shippingAddress;

    /**
     * 收货手机
     */
    @ApiModelProperty(value = "收货手机")
    private String shippingPhone;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String shippingName;

    /**
     * 拉新人id
     */
    @ApiModelProperty(value = "拉新人id")
    private String pullUid;

    /**
     * 促成人id
     */
    @ApiModelProperty(value = "促成人id")
    private String facilitateUid;

    /**
     * 小红书订单id
     */
    @ApiModelProperty(value = "小红书订单id")
    private String xhsOrderId;

    /**
     * 小红书收货人id
     */
    @ApiModelProperty(value = "小红书收货人id")
    private String xhsAddressId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
