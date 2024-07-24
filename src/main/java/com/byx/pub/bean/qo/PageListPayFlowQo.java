package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/6/25 21:14
 */
@Data
@Accessors(chain = true)
public class PageListPayFlowQo {

    /**
     * 咨询师id(前端不用传)
     */
    @ApiModelProperty(value = "咨询师id(前端不用传)")
    private String adminId;

    /**
     * 支付流水号(微信规范32位)
     */
    @ApiModelProperty(value = "支付流水号(微信规范32位)")
    private String paySn;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String adminName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
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
