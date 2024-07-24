package com.byx.pub.bean.vo;

import com.byx.pub.bean.bo.SaveShareFileBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/8/5 23:23
 */
@Data
public class ManageCardShareVo {
    /**
     * 分享老师列表
     */
    @ApiModelProperty(value = "分享老师列表")
    List<CardShareTeacherVo> teacherVoList;

    /**
     * 分享产品列表
     */
    @ApiModelProperty(value = "分享产品列表")
    List<ProductPageListVo> productVoList;

    /**
     * 分享资料列表
     */
    @ApiModelProperty(value = "分享资料列表")
    List<SaveShareFileBo> fileVoList;
}
