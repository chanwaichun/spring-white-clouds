package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jump
 * @date 2023/8/16 17:50:15
 */
@Data
public class PageHomeHelperVo {
    /**
     * Id(admin表id)
     */
    @ApiModelProperty(value = "Id(admin表id)")
    private String helperId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String helper;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String helperImg;

    /**
     * 帮助人数
     */
    @ApiModelProperty(value = "帮助人数")
    private Integer helpTo;
}
