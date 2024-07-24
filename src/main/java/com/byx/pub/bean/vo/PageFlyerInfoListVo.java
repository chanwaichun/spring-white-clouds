package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/10/7 20:19
 */
@Data
public class PageFlyerInfoListVo {
    /**
     * 主键
     */
    private String id;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;

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
     * 传单名称
     */
    @ApiModelProperty(value = "传单名称")
    private String flyerName;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String businessName;

    /**
     * 分享文案
     */
    @ApiModelProperty(value = "分享文案")
    private String shareTitle;

    /**
     * 分享图片
     */
    @ApiModelProperty(value = "分享图片")
    private String shareImg;

    /**
     * 跳转按钮状态(true:开，false:关)
     */
    @ApiModelProperty(value = "跳转按钮状态(true:开，false:关)")
    private Boolean jumpButtonStatus;

    /**
     * 按钮名称
     */
    @ApiModelProperty(value = "按钮名称")
    private String buttonName;

    /**
     * 按钮跳转url
     */
    @ApiModelProperty(value = "按钮跳转url")
    private String buttonJumpUrl;
}
