package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/8/5 22:29
 */
@Data
public class ManageAddOrUpdateCardBo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
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
     * 咨询师id(默认当前)
     */
    @ApiModelProperty(value = "咨询师id(默认当前)")
    @NotEmpty(message = "请选择咨询师")
    private String adminId;
    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String trueName;
    /**
     * 咨询师靓照(证件照)
     */
    @ApiModelProperty(value = "咨询师靓照(证件照)")
    private String adminPhoto;
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
     * 标签集合
     */
    @ApiModelProperty(value = "标签集合")
    List<CardTagBo> tagList = new ArrayList<>();

    /**
     * 联系方式集合
     */
    @ApiModelProperty(value = "联系方式集合")
    List<CardContactBo> contactList = new ArrayList<>();

    /**
     * 证书集合
     */
    @ApiModelProperty(value = "证书集合")
    List<CardPaperBo> paperList = new ArrayList<>();


}
