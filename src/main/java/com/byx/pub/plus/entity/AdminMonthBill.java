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
 * 咨询师月结表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AdminMonthBill对象", description="咨询师月结表")
public class AdminMonthBill extends Model<AdminMonthBill> {

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
     * 数据状态(true:有效，false:无效)
     */
    @ApiModelProperty(value = "数据状态(true:有效，false:无效)")
    private Boolean dataStatus;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

    /**
     * 账期(2023-01)
     */
    @ApiModelProperty(value = "账期(2023-01)")
    private String billDate;

    /**
     * 订单总额(收到支付金额)
     */
    @ApiModelProperty(value = "订单总额(收到支付金额)")
    private BigDecimal orderAmount;

    /**
     * 实收金额(抽佣后)
     */
    @ApiModelProperty(value = "实收金额(抽佣后)")
    private BigDecimal realIncome;

    /**
     * 结算比例
     */
    @ApiModelProperty(value = "结算比例")
    private BigDecimal settlementRatio;

    /**
     * 平台佣金
     */
    @ApiModelProperty(value = "平台佣金")
    private BigDecimal commission;

    /**
     * 订单数
     */
    @ApiModelProperty(value = "订单数")
    private Integer orderNum;

    /**
     * 获客数
     */
    @ApiModelProperty(value = "获客数")
    private Integer customerNum;

    /**
     * 结算状态(true:已结算，false:未结算)
     */
    @ApiModelProperty(value = "结算状态(true:已结算，false:未结算)")
    private Boolean settlementStatus;

    /**
     * 结算人
     */
    @ApiModelProperty(value = "结算人")
    private String settler;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
