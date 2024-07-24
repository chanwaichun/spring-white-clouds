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
 * 支付记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PayRecord对象", description="支付记录表")
public class PayRecord extends Model<PayRecord> {

    private static final long serialVersionUID=1L;

    /**
     * 支付记录ID
     */
    @ApiModelProperty(value = "支付记录ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 商家订单号(微信规范32位)
     */
    @ApiModelProperty(value = "商家订单号(微信规范32位)")
    private String outTradeNo;

    /**
     * 支付流水号(微信规范32位)
     */
    @ApiModelProperty(value = "支付流水号(微信规范32位)")
    private String paySn;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * openid
     */
    @ApiModelProperty(value = "openid")
    private String openid;

    /**
     * 支付类型:1->微信
     */
    @ApiModelProperty(value = "支付类型:1->微信")
    private Integer payType;

    /**
     * 交易类型:1->JSAP
     */
    @ApiModelProperty(value = "交易类型:1->JSAP")
    private Integer tradeType;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String description;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal amount;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal productsAmount;

    /**
     * 交易状态:1->待支付，2->已支付，3->转入退款，4->已关闭，5->已撤销
     */
    @ApiModelProperty(value = "交易状态:1->待支付，2->已支付，3->转入退款，4->已关闭，5->已撤销")
    private Integer status;

    /**
     * 下单状态:1->成功，0->：失败
     */
    @ApiModelProperty(value = "下单状态:1->成功，0->：失败")
    private Integer placeStatus;

    /**
     * 完成支付时间
     */
    @ApiModelProperty(value = "完成支付时间")
    private Date successTime;

    /**
     * 附加信息
     */
    @ApiModelProperty(value = "附加信息")
    private String attch;

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

    /**
     * 支付通知数据
     */
    @ApiModelProperty(value = "支付通知数据")
    private String notifyBody;

    /**
     * 退款状态：1->未退款，2->部分退款，3->全额退款
     */
    @ApiModelProperty(value = "退款状态：1->未退款，2->部分退款，3->全额退款")
    private Integer refundStatus;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
