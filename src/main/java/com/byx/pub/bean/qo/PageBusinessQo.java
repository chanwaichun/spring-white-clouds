package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/7/14 22:30
 */
@Data
public class PageBusinessQo {
    /**
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String shortName;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String telephone;

    /**
     * 创建开始时间
     */
    @ApiModelProperty(value = "创建开始时间")
    private String startTime;

    /**
     * 创建结束日期
     */
    @ApiModelProperty(value = "创建结束日期")
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
