package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/10/22 15:25
 */
@Data
public class PageListGroupMemberQo {
    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    @NotEmpty(message = "请传入分组id")
    private String groupId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 进组类型(1：规则，2：白名单)
     */
    @ApiModelProperty(value = "进组类型(1：规则，2：白名单)")
    private Integer inType;

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
