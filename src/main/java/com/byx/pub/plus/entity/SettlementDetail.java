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
 * 结算明细表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SettlementDetail对象", description="结算明细表")
public class SettlementDetail extends Model<SettlementDetail> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 创建时间(出单时间)
     */
    @ApiModelProperty(value = "创建时间(出单时间)")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 订单金额(实收)
     */
    @ApiModelProperty(value = "订单金额(实收)")
    private BigDecimal orderAmount;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private String productId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 结算人后台id
     */
    @ApiModelProperty(value = "结算人后台id")
    private String settlerAdminId;

    /**
     * 结算人用户id
     */
    @ApiModelProperty(value = "结算人用户id")
    private String settlerUserId;

    /**
     * 结算人后台名称
     */
    @ApiModelProperty(value = "结算人后台名称")
    private String settlerAdminName;

    /**
     * 结算人用户名称
     */
    @ApiModelProperty(value = "结算人用户名称")
    private String settlerUserName;

    /**
     * 分成类型(1：拉新，2：促成，3：其他)
     */
    @ApiModelProperty(value = "分成类型(1：拉新，2：促成，3：其他)")
    private Integer shareType;

    /**
     * 分成比例
     */
    @ApiModelProperty(value = "分成比例")
    private BigDecimal shareRate;

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算时间")
    private Date settlementDate;

    /**
     * 结算状态(false：待结算，true：已结算)
     */
    @ApiModelProperty(value = "结算状态(false：待结算，true：已结算)")
    private Boolean settlementStatus;

    /**
     * 结算id
     */
    @ApiModelProperty(value = "结算id")
    private String settlementId;

    /**
     * 范围id
     */
    @ApiModelProperty(value = "范围id")
    private String rangeId;

    /**
     * 账单id
     */
    @ApiModelProperty(value = "账单id")
    private String mainId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
