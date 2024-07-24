package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/8/3 23:35
 */
@Data
@Accessors(chain = true)
public class CardProductBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String cardId;

    /**
     * 产品id列表
     */
    @ApiModelProperty(value = "产品id列表")
    private List<String> productIdList;


}
