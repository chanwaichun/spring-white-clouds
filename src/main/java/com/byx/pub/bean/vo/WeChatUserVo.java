package com.byx.pub.bean.vo;

import lombok.Data;

/**
 * 微信用户信息
 * @Author: jump
 * @Date: 2023-05-11  23:18
 */
@Data
public class WeChatUserVo {

    private String openId;

    private String unionId;

    private String nickName;

    private String userImg;

    private Integer userSex;
}
