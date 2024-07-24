package com.byx.pub.bean.vo;


import com.byx.pub.plus.entity.CardContact;
import com.byx.pub.plus.entity.CardPaper;
import com.byx.pub.plus.entity.CardTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/7/16 22:23
 */
@Data
public class AdminCardVo {
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
     * 商家全称
     */
    @ApiModelProperty(value = "商家全称")
    private String fullName;
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
     * 咨询师靓照
     */
    @ApiModelProperty(value = "咨询师靓照")
    private String adminPhoto;

    /**
     * 微信授权头像
     */
    @ApiModelProperty(value = "微信授权头像")
    private String userImg;

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
     * 文字介绍
     */
    @ApiModelProperty(value = "文字介绍")
    private String textDesc;
    /**
     * 相关认证
     */
    @ApiModelProperty(value = "相关认证")
    private String authImg;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
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
     * 名片二维码
     */
    @ApiModelProperty(value = "名片二维码")
    private String qrCode;
    /**
     * 标签列表
     */
    @ApiModelProperty(value = "标签列表")
    List<CardTag> tags = new ArrayList<>();
    /**
     * 联系方式列表
     */
    @ApiModelProperty(value = "联系方式列表")
    List<CardContact> contacts = new ArrayList<>();
    /**
     * 证书列表
     */
    @ApiModelProperty(value = "证书列表")
    List<CardPaper> papers = new ArrayList<>();

}
