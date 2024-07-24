package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/16 19:32
 */
@Data
public class AdminShortCardVo {
    /**
     * 卡片id
     */
    @ApiModelProperty(value = "卡片id")
    private String cardId;

    /**
     * 咨询师靓照
     */
    @ApiModelProperty(value = "咨询师靓照")
    private String adminPhoto;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String trueName;

}
