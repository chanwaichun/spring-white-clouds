package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @Author Jump
 * @Date 2023/10/7 22:15
 */
@Data
public class PageFlyerPullListQo {
    /**
     * 分享id(前端不用传)
     */
    @ApiModelProperty(value = "分享id(前端不用传)")
    private String shareId;

    /**
     * 成交状态(前端不用传)
     */
    @ApiModelProperty(value = "成交状态(前端不用传)")
    private Boolean dealStatus;

    /**
     * 传单id
     */
    @ApiModelProperty(value = "传单id")
    @NotEmpty(message = "请传入传单id")
    private String flyerId;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

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
