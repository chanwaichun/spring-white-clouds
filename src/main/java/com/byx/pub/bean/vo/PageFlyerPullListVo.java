package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/10/7 22:11
 */
@Data
public class PageFlyerPullListVo {

    /**
     * 分享人uid(小程序id)
     */
    @ApiModelProperty(value = "分享人uid(小程序id)")
    private String userId;

    /**
     * 分享人头像
     */
    @ApiModelProperty(value = "分享人头像")
    private String img;

    /**
     * 分享人真实姓名
     */
    @ApiModelProperty(value = "分享人真实姓名")
    private String trueName;

    /**
     * 分享人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)
     */
    @ApiModelProperty(value = "分享人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)")
    private Integer roleId;

    /**
     * 拉新人数
     */
    @ApiModelProperty(value = "拉新人数")
    private Integer pullNum;

    /**
     * 成交人数
     */
    @ApiModelProperty(value = "成交人数")
    private Integer dealNum;

}
