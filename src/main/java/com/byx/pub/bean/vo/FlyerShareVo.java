package com.byx.pub.bean.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Jump
 * @Date 2023/10/7 20:56
 */
@Data
@Accessors(chain = true)
public class FlyerShareVo {
    /**
     * 传单id
     */
    @ApiModelProperty(value = "传单id")
    private String flyerId;

    /**
     * 转发主键(用户点击时用，普通用户、他家咨询师转发)
     */
    @ApiModelProperty(value = "转发主键(用户点击时用，普通用户、他家咨询师转发)")
    private String relayId;

    /**
     * 分享主键
     */
    @ApiModelProperty(value = "分享主键")
    private String shareId;

}
