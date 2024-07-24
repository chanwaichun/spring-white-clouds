package com.byx.pub.bean.crop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jamin
 * @Date 2023/2/3 11:58
 */
@Data
public class ReceiveDto {
    @ApiModelProperty("企业微信后台，开发者设置的token")
    private String token;

    @ApiModelProperty("企业微信后台，开发者设置的EncodingAESKey")
    private String encodingAesKey;

    @ApiModelProperty("不同场景含有不同，企业应用的回调：corpid,第三方事件的回调：suiteid,个人主体的第三方应用的回调：receiveId为空字符串")
    private String receiveId;
}
