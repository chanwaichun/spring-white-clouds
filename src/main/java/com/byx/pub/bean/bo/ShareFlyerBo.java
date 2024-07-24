package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/10/7 21:27
 */
@Data
@Accessors(chain = true)
public class ShareFlyerBo {

    @ApiModelProperty(value = "当前分享人用户id(前端不传)")
    private String nowUid;

    @NotEmpty(message = "传单id不能为空")
    @ApiModelProperty(value = "传单id")
    private String flyerId;

    @ApiModelProperty(value = "分享类型(1：小程序卡片，2：H5页面，3：二维码)")
    @NotNull(message = "分享类型不能为空")
    private Integer shareType = 1;

    @ApiModelProperty(value = "分享的主键(转发时必传)")
    private String shareId;

    /**
     * 转发主键(有就传(供应商转发的会有))
     */
    @ApiModelProperty(value = "转发主键(有就传(供应商转发的会有))")
    private String relayId;

}
