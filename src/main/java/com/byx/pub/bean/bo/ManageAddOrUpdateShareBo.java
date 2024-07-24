package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/8/5 23:03
 */
@Data
public class ManageAddOrUpdateShareBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String id;

    /**
     * 推荐老师列表
     */
    @ApiModelProperty(value = "推荐老师列表")
    private List<SaveCardShareTeacherBo> teacherBoList;

    /**
     * 推荐产品列表
     */
    @ApiModelProperty(value = "推荐产品列表")
    private List<SaveCardShareProductBo> productBoList;

    /**
     * 推荐资料列表
     */
    @ApiModelProperty(value = "推荐资料列表")
    private List<SaveShareFileBo> fileBoList;

}
