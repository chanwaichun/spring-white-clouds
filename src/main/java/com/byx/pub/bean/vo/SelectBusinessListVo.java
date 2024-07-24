package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/15 0:18
 */
@Data
public class SelectBusinessListVo {
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;
    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;
}
