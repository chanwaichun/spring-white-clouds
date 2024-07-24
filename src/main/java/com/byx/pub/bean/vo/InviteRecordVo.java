package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
/**
 * @author Jump
 * @date 2023/8/15 17:48:20
 */
@Data
public class InviteRecordVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 邀请函id
     */
    @ApiModelProperty(value = "邀请函id")
    private String inviteId;

    /**
     * 邀请人id
     */
    @ApiModelProperty(value = "邀请人id")
    private String inviteUserId;

    /**
     * 新用户id
     */
    @ApiModelProperty(value = "新用户id")
    private String newUserId;

    /**
     * 新用户openid
     */
    @ApiModelProperty(value = "新用户openid")
    private String openId;

    /**
     * 新用户微信昵称
     */
    @ApiModelProperty(value = "新用户微信昵称")
    private String nickName;

    /**
     * 加入状态(0：未加入，1：已加入，2：通过他人加入)
     */
    @ApiModelProperty(value = "加入状态(0：未加入，1：已加入，2：通过他人加入)")
    private Integer joinStatus;

    /**
     * 预估收入
     */
    @ApiModelProperty(value = "预估收入")
    private BigDecimal income;

}
