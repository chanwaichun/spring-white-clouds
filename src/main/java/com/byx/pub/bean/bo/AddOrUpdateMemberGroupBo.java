package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * @Author Jump
 * @Date 2023/10/22 11:49
 */
@Data
@Accessors(chain = true)
public class AddOrUpdateMemberGroupBo {
    /**
     * 主键(修改传)
     */
    @ApiModelProperty(value = "主键(修改传)")
    private String id;
    /**
     * 商家id(前端不用传)
     */
    @ApiModelProperty(value = "商家id(前端不用传)")
    private String businessId;

    /**
     * 用户组名称
     */
    @ApiModelProperty(value = "用户组名称")
    @NotEmpty(message = "请填写用户组名称")
    @Size(max = 30,message = "用户组名称最大30个字符")
    private String groupName;

    /**
     * 规则状态(true：启用，false：停用)
     */
    @ApiModelProperty(value = "规则状态(true：启用，false：停用)")
    @NotNull(message = "请选用户规则")
    private Boolean ruleStatus;

    /**
     * 规则列表
     */
    @ApiModelProperty(value = "规则列表")
    private List<AddOrUpdateMemberRuleBo> ruleBoList;
}
