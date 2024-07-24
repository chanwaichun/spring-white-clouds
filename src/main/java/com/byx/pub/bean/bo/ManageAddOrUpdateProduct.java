package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/8/5 22:48
 */
@Data
public class ManageAddOrUpdateProduct {

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String id;

    /**
     * 广告产品id
     */
    @ApiModelProperty(value = "广告产品id")
    private String productId;

    /**
     * 广告类型(1：广告商品，2：广告图)
     */
    @ApiModelProperty(value = "广告类型(1：广告商品，2：广告图)")
    private Integer posterType;

    /**
     * 广告位路径
     */
    @ApiModelProperty(value = "广告位路径(图片)")
    private String posterUrl;

    /**
     * 产品id列表
     */
    @ApiModelProperty(value = "产品id列表")
    private List<String> productIdList;

}
