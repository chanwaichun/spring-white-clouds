package com.byx.pub.bean.vo;


import com.byx.pub.plus.entity.CardPaper;
import com.byx.pub.plus.entity.CardTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/7/16 21:18
 */
@Data
public class ListHotAdminCardVo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;
    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;
    /**
     * 咨询师昵称
     */
    @ApiModelProperty(value = "咨询师昵称")
    private String nickName;
    /**
     * 咨询师真实名字
     */
    @ApiModelProperty(value = "咨询师真实名字")
    private String trueName;
    /**
     * 咨询师靓照
     */
    @ApiModelProperty(value = "咨询师靓照")
    private String adminPhoto;
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
     * 证书列表
     */
    @ApiModelProperty(value = "证书列表")
    List<CardPaper> papers = new ArrayList<>();
}
