package com.byx.pub.bean.crop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jamin
 * @Date 2023/2/3 12:57
 */
@Data
public class SuiteTokenDto {
    @ApiModelProperty("应用id")
    private String suite_id;

    @ApiModelProperty("应用密钥")
    private String suite_secret;

    @ApiModelProperty("企业微信后台推送的ticket")
    private String suite_ticket;
}
