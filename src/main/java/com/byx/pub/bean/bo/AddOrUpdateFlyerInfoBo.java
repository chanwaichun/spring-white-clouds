package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Jump
 * @date 2023/10/7 18:20:39
 */
@Data
public class AddOrUpdateFlyerInfoBo {

    /**
     * 主键(修改必传)
     */
    private String id;

    /**
     * 传单名称
     */
    @ApiModelProperty(value = "传单名称")
    @NotEmpty(message = "请输入传单名称")
    @Size(max = 30,message = "传单名称最多30字")
    private String flyerName;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    @NotEmpty(message = "请输入商家id")
    private String businessId;

    /**
     * 商家商家简称
     */
    @ApiModelProperty(value = "商家商家简称")
    @NotEmpty(message = "请输入商家简称")
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
    @NotEmpty(message = "请输入页面内容")
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
    @Length(max = 20,message = "按钮名称最大20个字符")
    private String buttonName;

    /**
     * 按钮跳转url
     */
    @ApiModelProperty(value = "按钮跳转url")
    @Length(max = 255,message = "按钮链接最大255个字符")
    private String buttonJumpUrl;

}
