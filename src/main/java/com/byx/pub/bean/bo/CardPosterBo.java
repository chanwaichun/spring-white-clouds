package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @author Jump
 * @date 2023/8/4 15:11:14
 */
@Data
@Accessors(chain = true)
public class CardPosterBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String cardId;

    /**
     * 广告类型(1：广告商品，2：广告图)
     */
    @ApiModelProperty(value = "广告类型(1：广告商品，2：广告图)")
    private Integer posterType;

    /**
     * 广告产品id
     */
    @ApiModelProperty(value = "广告产品id")
    private String productId;

    /**
     * 广告位路径(图片)
     */
    @ApiModelProperty(value = "广告位路径(图片)")
    private String posterUrl;
}
