package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jump
 * @date 2023/6/8 11:37:06
 */
@Data
public class PageBusinessUserVo {

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 累计消费金额
     */
    @ApiModelProperty(value = "累计消费金额")
    private BigDecimal totalAmount;

    /**
     * 累计订单数
     */
    @ApiModelProperty(value = "累计订单数")
    private Integer totalOrderNum;

    /**
     * 最近消费时间
     */
    @ApiModelProperty(value = "最近消费时间")
    private Date updateTime;

    /**
     * 用户建号时间
     */
    @ApiModelProperty(value = "用户建号时间")
    private Date userCreateTime;

    /**
     * 小红书会员(0：否，1：是)
     */
    @ApiModelProperty(value = "小红书会员(0：否，1：是)")
    private Boolean xhsStatus;

}
