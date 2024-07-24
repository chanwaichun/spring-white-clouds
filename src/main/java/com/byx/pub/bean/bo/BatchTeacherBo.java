package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/17 17:39:34
 */
@Data
public class BatchTeacherBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    String cardId;

    /**
     * 推荐老师列表
     */
    @ApiModelProperty(value = "推荐老师列表")
    private List<SaveCardShareTeacherBo> teacherBoList;
}
