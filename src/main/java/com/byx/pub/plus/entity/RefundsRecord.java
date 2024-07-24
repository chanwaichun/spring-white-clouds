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
 * 退款记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RefundsRecord对象", description="退款记录表")
public class RefundsRecord extends Model<RefundsRecord> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
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
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 退款单号(微信)
     */
    @ApiModelProperty(value = "退款单号(微信)")
    private String refundSn;

    /**
     * 退款流水号
     */
    @ApiModelProperty(value = "退款流水号")
    private String refundId;

    /**
     * 支付记录id
     */
    @ApiModelProperty(value = "支付记录id")
    private String payRecordId;

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String mchid;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 退款方式，1：微信，2：支付宝
     */
    @ApiModelProperty(value = "退款方式，1：微信，2：支付宝")
    private Integer refundType;

    /**
     * 退款渠道,ORIGINAL：原路退款,BALANCE：退回到余额
     */
    @ApiModelProperty(value = "退款渠道,ORIGINAL：原路退款,BALANCE：退回到余额")
    private String channel;

    /**
     * 原支付金额
     */
    @ApiModelProperty(value = "原支付金额")
    private BigDecimal amount;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 退款状态，1：退款处理中,2：退款成功,3：退款异常,4:退款关闭
     */
    @ApiModelProperty(value = "退款状态，1：退款处理中,2：退款成功,3：退款异常,4:退款关闭")
    private Integer status;

    /**
     * 请求状态，1：成功，其他：失败
     */
    @ApiModelProperty(value = "请求状态，1：成功，其他：失败")
    private Integer refundsStatus;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private Date successTime;

    /**
     * 退款入账账户
     */
    @ApiModelProperty(value = "退款入账账户")
    private String userReceivedAccount;

    /**
     * 请求数据
     */
    @ApiModelProperty(value = "请求数据")
    private String requestBody;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private String responseBody;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
