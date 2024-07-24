package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/4 14:15:00
 */
@Data
public class SaveCardPaperBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    String cardId;

    /**
     * 证书集合
     */
    @ApiModelProperty(value = "证书集合")
    List<CardPaperBo> paperList = new ArrayList<>();
}
