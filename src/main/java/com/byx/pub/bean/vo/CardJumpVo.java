package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Jump
 * @Date 2023/8/27 20:39
 */
@Data
@Accessors(chain = true)
public class CardJumpVo {
    /**
     * 跳转类型(1：跳名片详情页，2:跳名片列表)
     */
    @ApiModelProperty(value = "跳转类型(1：跳名片详情页，2:跳名片列表)")
    private Integer jumpType;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;


}
