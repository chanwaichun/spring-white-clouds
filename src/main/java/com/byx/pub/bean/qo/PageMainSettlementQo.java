package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/9/24 12:53
 */
@Data
public class PageMainSettlementQo {
    /**
     * 商家id(请求头)
     */
    @ApiModelProperty(value = "商家id(请求头)")
    private String businessId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 结算状态(true：待结算，true：已结算)
     */
    @ApiModelProperty(value = "结算状态(true：待结算，true：已结算)")
    private Boolean settlementStatus;

    /**
     * 结算开始时间
     */
    @ApiModelProperty(value = "结算开始时间")
    private String startTime;

    /**
     * 结算结束时间
     */
    @ApiModelProperty(value = "结算结束时间")
    private String endTime;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "页面条数最小值为1")
    @NotNull(message = "页面条数不能为空")
    @ApiModelProperty(value = "页面大小", required = true)
    private Integer pageSize;
    /**
     * 页码
     */
    @Min(value = 1, message = "页码最小值为1")
    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

}
