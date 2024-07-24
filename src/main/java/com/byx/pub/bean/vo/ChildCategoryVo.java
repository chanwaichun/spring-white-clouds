package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/3 23:12
 */
@Data
public class ChildCategoryVo {
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
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID")
    private String parentId;
}
