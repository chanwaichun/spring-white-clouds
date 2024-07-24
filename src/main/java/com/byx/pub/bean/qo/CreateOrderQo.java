package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 生成订单Qo
 * @author Jump
 * @date 2023/5/24 15:50:46
 */
@Accessors(chain = true)
@Data
public class CreateOrderQo {
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    @NotEmpty(message = "商家id不能为空")
    private String businessId;
    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    @NotEmpty(message = "咨询师id不能为空")
    private String adminId;
    /**
     * 商家商品id
     */
    @ApiModelProperty(value = "商家商品id")
    @NotEmpty(message = "商家商品id不能为空")
    private String productId;

    /**
     * 商品数量(默认1)
     */
    @ApiModelProperty(value = "商品数量(默认1)")
    private Integer productNum = 1;

    /**
     * 拉新人id(userId)
     */
    @ApiModelProperty(value = "拉新人id")
    private String pullUid;

    /**
     * 促成人id(userId)
     */
    @ApiModelProperty(value = "促成人id")
    private String facilitateUid;
}
