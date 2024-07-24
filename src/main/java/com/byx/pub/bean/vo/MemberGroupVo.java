package com.byx.pub.bean.vo;

import com.byx.pub.bean.bo.AddOrUpdateMemberRuleBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/10/22 12:45
 */
@Data
public class MemberGroupVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户组名称
     */
    @ApiModelProperty(value = "用户组名称")
    private String groupName;

    /**
     * 规则状态(true：启用，false：停用)
     */
    @ApiModelProperty(value = "规则状态(true：启用，false：停用)")
    private Boolean ruleStatus;

    /**
     * 用户数
     */
    @ApiModelProperty(value = "用户数")
    private Integer memberNum;

    /**
     * 用户组更新状态(1：更新中，2：更新完成)
     */
    @ApiModelProperty(value = "用户组更新状态(1：更新中，2：更新完成)")
    private Integer updateStatus;

    /**
     * 规则列表
     */
    @ApiModelProperty(value = "规则列表")
    private List<AddOrUpdateMemberRuleBo> ruleBoList;

}
