package com.byx.pub.bean.vo;

import com.byx.pub.plus.entity.CardTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/4 17:51:44
 */
@Data
public class CardShareTeacherVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 老师名片id
     */
    @ApiModelProperty(value = "老师名片id")
    private String adminCardId;

    /**
     * 老师id
     */
    @ApiModelProperty(value = "老师id")
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
     * 推荐理由
     */
    @ApiModelProperty(value = "推荐理由")
    private String sellReason;

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
     * 商家全称
     */
    @ApiModelProperty(value = "商家全称")
    private String fullName;

    /**
     * 老师名片标签列表
     */
    @ApiModelProperty(value = "老师名片标签列表")
    List<CardTag> tags = new ArrayList<>();


}
