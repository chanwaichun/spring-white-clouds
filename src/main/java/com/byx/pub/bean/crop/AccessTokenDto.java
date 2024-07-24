package com.byx.pub.bean.crop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jamin
 * @Date 2023/2/3 12:55
 */
@Data
public class AccessTokenDto {
    @ApiModelProperty("凭证")
    private String accessToken;

    @ApiModelProperty("凭证超时时间")
    private String expiresIn;
}
