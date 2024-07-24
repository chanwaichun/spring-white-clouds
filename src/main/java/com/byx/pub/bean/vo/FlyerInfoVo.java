package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author Jump
 * @Date 2023/10/7 20:30
 */
@Data
public class FlyerInfoVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

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
     * 商家商家简称
     */
    @ApiModelProperty(value = "商家商家简称")
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
     * 页面内容
     */
    @ApiModelProperty(value = "页面内容")
    private String pageContent;

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
