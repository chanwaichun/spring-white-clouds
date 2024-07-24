package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/18 14:40:40
 */
@Data
public class SaveSettlementRulesBo {
    /**
     * 商家id(前端不传)
     */
    @ApiModelProperty(value = "商家id(前端不传)")
    private String businessId;
    /**
     * 主键(修改必传)
     */
    @ApiModelProperty(value = "主键(修改必传)")
    private String id;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    @Size(min = 1,max = 50,message = "规则名称1-50字")
    private String ruleName;

    /**
     * 结算周期(T+N)
     */
    @ApiModelProperty(value = "结算周期(T+N)")
    @Range(min = 0,max = 30,message = "结算周期范围0-30")
    private Integer settlementCycle;

    /**
     * 生效日期
     */
    @ApiModelProperty(value = "生效日期")
    @NotNull(message = "请选择生效日期")
    private LocalDate effectiveDate;

    /**
     * 结算范围配置列表
     */
    @ApiModelProperty(value = "结算范围配置列表")
    List<SaveRulesRangeBo> rulesRangeBoList;
}
