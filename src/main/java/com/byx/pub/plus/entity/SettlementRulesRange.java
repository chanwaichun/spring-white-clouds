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
 * 规则范围表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SettlementRulesRange对象", description="规则范围表")
public class SettlementRulesRange extends Model<SettlementRulesRange> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 更新者
     */
    @TableField(value = "updator", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updator;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 数据状态(true:有效,false:删除)
     */
    @ApiModelProperty(value = "数据状态(true:有效,false:删除)")
    private Boolean dataStatus;

    /**
     * 规则id
     */
    @ApiModelProperty(value = "规则id")
    private String ruleId;

    /**
     * 结算范围(1：角色，2：个人)
     */
    @ApiModelProperty(value = "结算范围(1：角色，2：个人)")
    private Integer ruleType;

    /**
     * 目标id( 角色id 或 个人uid )
     */
    @ApiModelProperty(value = "目标id( 角色id 或 个人uid )")
    private String targetId;

    /**
     * 目标名称(角色名称 或 个人名称)
     */
    @ApiModelProperty(value = "目标名称(角色名称 或 个人名称)")
    private String targetName;

    /**
     * 分成比例
     */
    @ApiModelProperty(value = "分成比例")
    private BigDecimal shareRate;

    /**
     * 结算类型(1：拉新，2：促成，3：其他)
     */
    @ApiModelProperty(value = "结算类型(1：拉新，2：促成，3：其他)")
    private Integer settlementType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
