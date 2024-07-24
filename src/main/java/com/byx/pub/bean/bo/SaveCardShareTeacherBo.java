package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Jump
 * @date 2023/8/4 17:41:23
 */
@Data
public class SaveCardShareTeacherBo {

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String cardId;

    /**
     * 老师名片id
     */
    @ApiModelProperty(value = "老师名片id")
    @NotEmpty(message = "老师名片id不能为空")
    private String adminCardId;

    /**
     * 推荐理由
     */
    @ApiModelProperty(value = "推荐理由")
    @NotEmpty(message = "推荐理由不能为空")
    @Size(max = 300,message = "推荐理由最多300字")
    private String sellReason;

}
