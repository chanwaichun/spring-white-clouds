package com.byx.pub.bean.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author Jump
 * @Date 2023/10/22 15:22
 */
@Data
public class PageListGroupMemberVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
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

    /**
     * 进组类型(1：规则，2：白名单)
     */
    @ApiModelProperty(value = "进组类型(1：规则，2：白名单)")
    private Integer inType;

}
