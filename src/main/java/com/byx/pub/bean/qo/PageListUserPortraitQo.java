package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/10/28 12:39
 */
@Data
public class PageListUserPortraitQo {
    /**
     * 用户id
     */
    @NotEmpty(message = "请传入用户id")
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 商家id(前端不传)
     */
    @ApiModelProperty(value = "商家id(前端不传)")
    private String bid;

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
