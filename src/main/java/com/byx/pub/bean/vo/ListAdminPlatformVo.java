package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author Jump
 * @Date 2023/12/26 21:36
 */
@Data
public class ListAdminPlatformVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String adminId;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String trueName;
    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String nickName;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;
    /**
     * 角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)
     */
    @ApiModelProperty(value = "角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)")
    private String roleId;
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;
    /**
     * 小红书店铺id(有值表示已绑定)
     */
    @ApiModelProperty(value = "小红书店铺id(有值表示已绑定)")
    private String xhsSellerId;

}
