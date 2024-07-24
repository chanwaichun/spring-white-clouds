package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Jump
 * @date 2023/8/18 14:53:33
 */
@Data
public class SaveRulesRangeBo {
    /**
     * 结算类型(1：拉新，2：促成，3：其他)
     */
    @ApiModelProperty(value = "结算类型(1：拉新，2：促成，3：其他)")
    @Range(min = 1,max = 3,message = "请选择正确的结算类型")
    private Integer settlementType;

    /**
     * 结算范围(1：角色，2：个人)
     */
    @ApiModelProperty(value = "结算范围(1：角色，2：个人)")
    @Range(min = 1,max = 2,message = "请选择正确的结算范围")
    private Integer ruleType;

    /**
     * 目标id( 角色id 或 咨询师id )
     */
    @ApiModelProperty(value = "目标id( 角色id 或 咨询师id )")
    @NotEmpty(message = "请选择结算目标")
    private String targetId;

    /**
     * 目标名称(角色名称 或 个人名称)
     */
    @ApiModelProperty(value = "目标名称(角色名称 或 个人名称)")
    @NotEmpty(message = "请选择结算目标")
    private String targetName;

    /**
     * 分成比例
     */
    @ApiModelProperty(value = "分成比例")
    @Digits(integer = 3, fraction = 2, message = "分成比例整数上限是3位，小数上限是2位")
    @DecimalMin(value = "0.01", message = "分成比例不能小于0.01")
    @NotNull(message = "分成比例不能为空")
    private BigDecimal shareRate;


}
