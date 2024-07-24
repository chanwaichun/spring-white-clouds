package com.byx.pub.bean.xhs;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jump
 * @Date 2024/1/6 20:10
 */
@Data
public class XhsPushProductResVo {

    /**
     * 成功信息列表
     */
    @ApiModelProperty(value = "成功店铺列表")
    private List<String> successList = new ArrayList<>();

    /**
     * 失败信息列表
     */
    @ApiModelProperty(value = "失败店铺列表(含失败原因)")
    private List<String> failList = new ArrayList<>();
}
