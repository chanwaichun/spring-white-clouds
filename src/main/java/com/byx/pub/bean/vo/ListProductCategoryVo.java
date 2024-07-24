package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/6/27 21:37
 */
@Data
@Accessors(chain = true)
public class ListProductCategoryVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 类目级数(1:一级，2:二级)
     */
    @ApiModelProperty(value = "类目级数(1:一级，2:二级)")
    private Integer level;
    /**
     * 类目名称(20)
     */
    @ApiModelProperty(value = "类目名称")
    private String title;
    /**
     * 子集类目列表
     */
    @ApiModelProperty(value = "子集类目列表")
    List<ChildCategoryVo> childCategoryVoList = new ArrayList<>();
}
