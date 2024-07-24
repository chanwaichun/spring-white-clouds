package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Jump
 * @date 2023/6/14 15:28:34
 */
@Data
public class PageAdminUserQo {

    /**
     * 商家id(前端不用传)
     */
    @ApiModelProperty(value = "商家id(前端不用传)")
    private String businessId;

    /**
     * 用户手机
     */
    @ApiModelProperty(value = "用户手机")
    private String userMobile;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

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
