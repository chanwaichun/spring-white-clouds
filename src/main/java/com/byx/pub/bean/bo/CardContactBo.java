package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/31 21:11
 */
@Data
public class CardContactBo {
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String icon;

    /**
     * 联系方式类型(1：抖音，2：手机，3：企业微信，4：微信，5：小红书，6：微信视频号，7：哔哩哔哩)
     */
    @ApiModelProperty(value = "联系方式类型(1：抖音，2：手机，3：企业微信，4：微信，5：小红书，6：微信视频号，7：哔哩哔哩)")
    private Integer contactType;
    /**
     * 主要联系(true：是，false：否)
     */
    @ApiModelProperty(value = "主要联系(true：是，false：否)")
    private Boolean mainStatus;
    /**
     * 文本(手机号)
     */
    @ApiModelProperty(value = "文本(号码类)")
    private String textRemark;

}
