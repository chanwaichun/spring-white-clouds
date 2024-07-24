package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author Jump
 * @Date 2023/10/22 15:38
 */
@Data
public class AddWhitelistMemberBo {
    /**
     * 主键(拉黑传)
     */
    @ApiModelProperty(value = "主键(拉黑传)")
    private String id;

    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    @NotEmpty(message = "分组id必传")
    private String groupId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @NotEmpty(message = "用户id必传")
    private String userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

}
