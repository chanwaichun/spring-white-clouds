package com.byx.pub.bean.crop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jamin
 * @Date 2023/2/3 14:51
 */
@Data
public class OauthDto {
    @ApiModelProperty("授权企业id")
    private String authCorpId;

    @ApiModelProperty("企业微信用户唯一标识")
    private String openUserId;

    @ApiModelProperty("企微返回json")
    private String resStr;
}
