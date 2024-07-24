package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/21 20:55
 */
@Data
public class PageSettlementDetailListVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;
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
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;
    /**
     * 创建时间(出单时间)
     */
    @ApiModelProperty(value = "创建时间(出单时间)")
    private String createTime;

}
