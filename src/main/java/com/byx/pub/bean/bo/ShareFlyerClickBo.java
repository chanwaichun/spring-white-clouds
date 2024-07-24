package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author Jump
 * @Date 2023/10/7 21:44
 */
@Data
public class ShareFlyerClickBo {
    /**
     * 分享主键
     */
    @ApiModelProperty(value = "分享主键")
    @NotEmpty(message = "分享主键不能为空")
    private String shareId;

    /**
     * 转发id
     */
    @ApiModelProperty(value = "转发id(用户、非商家咨询师特有)")
    private String relayId;

    /**
     * 受邀人uid
     */
    @ApiModelProperty(value = "受邀人uid")
    @NotEmpty(message = "受邀人uid不能为空")
    private String inviteeUid;

}
