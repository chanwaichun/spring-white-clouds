package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/10 12:49
 */
@Data
public class PageRefundOrderQo {
    /**
     * 商家id(前端不用传)
     */
    @ApiModelProperty(value = "商家id(前端不用传)")
    private String businessId;
    /**
     * 咨询师id(前端不用传)
     */
    @ApiModelProperty(value = "咨询师id(前端不用传)")
    private String adminId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;
    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String adminName;
    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;

    /**
     * 下单手机
     */
    @ApiModelProperty(value = "下单手机")
    private String userMobile;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

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
