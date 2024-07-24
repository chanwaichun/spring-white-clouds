package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/21 22:06
 */
@Data
public class PagePersonSettlementVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 主账单id
     */
    @ApiModelProperty(value = "主账单id")
    private String mainId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;

    /**
     * 结算人后台名称
     */
    @ApiModelProperty(value = "结算人后台名称")
    private String settlerAdminName;

    /**
     * 结算人角色
     */
    @ApiModelProperty(value = "结算人角色")
    private String roleId;

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    /**
     * 创建时间(出单时间)
     */
    @ApiModelProperty(value = "创建时间(出单时间)")
    private Date createTime;

    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算时间")
    private Date settlementDate;

}
