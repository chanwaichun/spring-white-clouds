package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Author Jump
 * @Date 2023/8/5 12:50
 */
@Data
public class SaveCardShareProductBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String cardId;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    @NotEmpty(message = "产品id不能为空")
    private String productId;

    /**
     * 类型(1：名片产品，2：分享产品)
     */
    @ApiModelProperty(value = "类型(1：名片产品，2：分享产品)")
    private Integer sellType;

    /**
     * 推荐理由
     */
    @ApiModelProperty(value = "推荐理由")
    @NotEmpty(message = "推荐理由不能为空")
    @Size(max = 300,message = "推荐理由最多300字")
    private String sellReason;
}
