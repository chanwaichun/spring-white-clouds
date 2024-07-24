package com.byx.pub.bean.vo;

import com.byx.pub.plus.entity.CardPaper;
import com.byx.pub.plus.entity.CardTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jump
 * @date 2023/9/27 10:31:45
 */
@Data
public class PageSearchCardVo {
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
     * 推荐人(XXX推荐过他)
     */
    @ApiModelProperty(value = "推荐人(XXX推荐过他)")
    private String reference;
    /**
     * 证书列表
     */
    @ApiModelProperty(value = "证书列表")
    List<CardPaper> papers = new ArrayList<>();
    /**
     * 标签列表
     */
    @ApiModelProperty(value = "标签列表")
    List<CardTag> tagList = new ArrayList<>();
}
