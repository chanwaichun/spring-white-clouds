package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/16 17:45:37
 */
@Data
public class PageHomeHelpVo {
    /**
     * 我帮他人前三列表
     */
    @ApiModelProperty(value = "我帮他人前三列表")
    private List<PageHomeHelperVo> helpListVos = new ArrayList<>();

    /**
     * 他人帮我前三列表
     */
    @ApiModelProperty(value = "他人帮我前三列表")
    private List<PageHomeHelperVo> beneficiaryVos  = new ArrayList<>();
}
