package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/31 21:09
 */
@Data
public class CardTagBo {
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private String tagId;
    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String tagName;
}
