package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/17 17:32:55
 */
@Data
public class BatchFileBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    String cardId;

    /**
     * 分享列表
     */
    @ApiModelProperty(value = "分享列表")
    private List<SaveShareFileBo> fileBoList;


}
