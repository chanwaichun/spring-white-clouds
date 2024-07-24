package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Jump
 * @date 2023/8/18 15:20:44
 */
@Data
public class PageSettlementRulesVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

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
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String shortName;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 结算周期(T+N)
     */
    @ApiModelProperty(value = "结算周期(T+N)")
    private Integer settlementCycle;

}
