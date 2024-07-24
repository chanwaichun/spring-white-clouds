package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * @Author: jump
 * @Date: 2023-05-23  22:52
 */
@Data
public class UpdateUserInfoQo {
    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    @Length(max =50,message = "昵称最多50个字符")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @Length(max = 255,message = "用户头像最多255个字符")
    private String userImg;

    /**
     * 性别(0 未知 1 男 2女)
     */
    @ApiModelProperty(value = "性别(0 未知 1 男 2女)")
    private Integer gender;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String shippingAddress;
}
