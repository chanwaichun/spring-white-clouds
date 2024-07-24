package com.byx.pub.bean.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jump
 * @date 2023/8/4 17:14:32
 */
@Data
public class PageAdminCardVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

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

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String trueName;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 咨询师靓照(证件照)
     */
    @ApiModelProperty(value = "咨询师靓照(证件照)")
    private String adminPhoto;

    /**
     * 证书审核状态(1：待审核，2：不通过，3：已审核)
     */
    @ApiModelProperty(value = "证书审核状态(1：待审核，2：不通过，3：已审核)")
    private Integer paperStatus;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private Long province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private Long city;

    /**
     * 区/县
     */
    @ApiModelProperty(value = "区/县")
    private Long area;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 个人介绍
     */
    @ApiModelProperty(value = "个人介绍")
    private String adminDesc;

    /**
     * 语音介绍(地址)
     */
    @ApiModelProperty(value = "语音介绍(地址)")
    private String voiceDesc;

    /**
     * 导师介绍
     */
    @ApiModelProperty(value = "导师介绍")
    private String textDesc;

    /**
     * 购买用户数
     */
    @ApiModelProperty(value = "购买用户数")
    private Integer buyUserNum;

    /**
     * 服务时长
     */
    @ApiModelProperty(value = "服务时长")
    private BigDecimal serviceTimes;

    /**
     * 微信授权头像
     */
    @ApiModelProperty(value = "微信授权头像")
    private String userImg;
}
