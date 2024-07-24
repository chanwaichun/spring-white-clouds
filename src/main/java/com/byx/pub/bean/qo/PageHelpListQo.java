package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/8/16 22:46
 */
@Data
public class PageHelpListQo {

    /**
     * 当前咨询id(前端不用传)
     */
    @ApiModelProperty(value = "当前咨询id(前端不用传)")
    private String adminId;
    /**
     * 查询类型(1：人助我，2：我助人)
     */
    @ApiModelProperty(value = "查询类型(1：人助我，2：我助人)")
    private Integer helpType = 1;
    /**
     * 排序类型(1：给你引流，2：给他引流)
     */
    @ApiModelProperty(value = "排序类型(1：给你引流，2：给他引流)")
    private Integer orderByType = 1;
    /**
     * 排序规则(1：正序，2：倒序)
     */
    @ApiModelProperty(value = "排序规则(1：正序，2：倒序)")
    private Integer orderByRule = 1;
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
