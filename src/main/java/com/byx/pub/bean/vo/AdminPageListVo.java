package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 账户列表vo
 * @author Jump
 * @date 2023/5/7 15:58:03
 */
@Data
public class AdminPageListVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String adminId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 账户(手机号)
     */
    @ApiModelProperty(value = "账户(手机号)")
    private String mobile;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String trueName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 角色Id(1:管理员，2:咨询师)
     */
    @ApiModelProperty(value = "角色Id(1:管理员，2:咨询师)")
    private String roleId;

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
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String shortName;

}
