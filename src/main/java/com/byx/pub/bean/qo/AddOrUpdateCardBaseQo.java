package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * @Author Jump
 * @Date 2023/7/31 20:47
 */
@Data
@Accessors(chain = true)
public class AddOrUpdateCardBaseQo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 商家id(前端不传)
     */
    @ApiModelProperty(value = "商家id(前端不传)")
    private String businessId;
    /**
     * 商家简称(前端不传)
     */
    @ApiModelProperty(value = "商家简称(前端不传)")
    private String shortName;
    /**
     * 咨询师id(默认当前)
     */
    @ApiModelProperty(value = "咨询师id(默认当前)")
    private String adminId;
    /**
     * 微信授权头像(前端不传)
     */
    @ApiModelProperty(value = "微信授权头像(前端不传)")
    private String userImg;
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
    @NotEmpty(message = "请上传照片")
    private String adminPhoto;
    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    @NotNull(message = "请选择省份")
    private Long province;
    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    @NotNull(message = "请选择城市")
    private Long city;
    /**
     * 区/县
     */
    @ApiModelProperty(value = "区/县")
    @NotNull(message = "请选择区/县")
    private Long area;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotEmpty(message = "请填写手机号")
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

}
