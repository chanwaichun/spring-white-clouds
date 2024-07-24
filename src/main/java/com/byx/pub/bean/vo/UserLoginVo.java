package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 客户端登录用户Vo
 * @author Jump
 * @date 2023/5/24 13:51:15
 */
@Accessors(chain = true)
@Data
public class UserLoginVo {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

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
     * 性别(0 未知 1 男 2女)
     */
    @ApiModelProperty(value = "性别(0 未知 1 男 2女)")
    private Integer gender;

    /**
     * 当前角色类型(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)
     */
    @ApiModelProperty(value = "当前角色类型(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)")
    private Integer roleType;

    /**
     * 是否商家:true->是,false->否
     */
    @ApiModelProperty(value = "是否商家:true->是,false->否")
    Boolean isBusiness = Boolean.FALSE;

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String shippingAddress;

}
