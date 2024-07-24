package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Jump
 * @date 2023/8/16 15:31:07
 */
@Data
public class AddHelpRecordBo {

    @ApiModelProperty(value = "帮助id(名片id，商品id)")
    private String helpId;

    /**
     * 帮助方式(1：推荐名片，2：分享商品)
     */
    @ApiModelProperty(value = "帮助方式(1：推荐名片，2：分享商品)")
    private Integer helpType;

    /**
     * 帮助者Id(admin表id)
     */
    @ApiModelProperty(value = "帮助者Id(admin表id)")
    @NotEmpty(message = "请传入帮助者id")
    private String helperId;

    /**
     * 被引流用户id
     */
    @ApiModelProperty(value = "被引流用户id")
    @NotEmpty(message = "请传入客户id")
    private String userId;

    /**
     * 受益者Id(admin表id)
     */
    @ApiModelProperty(value = "受益者Id(admin表id)")
    @NotEmpty(message = "请传入受益者Id")
    private String beneficiaryId;
}
