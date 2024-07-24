package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录用户VO
 * @Author: jump
 * @Date: 2023-05-11  22:48
 */
@Data
public class LoginAdminVo {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id(为空表示未绑定手机)")
    private String adminId;

    /**
     * 账户(手机号)
     */
    @ApiModelProperty(value = "账户(手机号)")
    private String mobile;

    /**
     * 性别(0 未知 1 男 2女)
     */
    @ApiModelProperty(value = "性别(0 未知 1 男 2女)")
    private Integer gender;

    /**
     * 角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人,0:未授权用户)
     */
    @ApiModelProperty(value = "角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0:未授权用户)")
    private String roleId;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String trueName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;
    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;


}
