package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/24 12:46
 */
@Data
public class PageMainSettlementVo {

    /**
     * 分账id
     */
    @ApiModelProperty(value = "分账id")
    private String id;

    /**
     * 创建时间(出单时间)
     */
    @ApiModelProperty(value = "创建时间(出单时间)")
    private Date createTime;

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
     * 结算状态(false：待结算，true：已结算)
     */
    @ApiModelProperty(value = "结算状态(false：待结算，true：已结算)")
    private Boolean settlementStatus;

    /**
     * 结算订单数
     */
    @ApiModelProperty(value = "结算订单数")
    private Integer orderNum;

    /**
     * 结算人数
     */
    @ApiModelProperty(value = "结算人数")
    private Integer settlerNum;

    /**
     * 应收金额
     */
    @ApiModelProperty(value = "应收金额")
    private BigDecimal incomeAmount;

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    /**
     * 结余金额
     */
    @ApiModelProperty(value = "结余金额")
    private BigDecimal balanceAmount;

    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算时间")
    private Date settlementDate;

    /**
     * 账期(如：2023-09-01--2023-09-20)
     */
    @ApiModelProperty(value = "账期(如：2023-09-01--2023-09-20)")
    private String billDate;



}
