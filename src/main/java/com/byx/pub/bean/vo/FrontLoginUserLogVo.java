package com.byx.pub.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 登录用户日志vo
 * @author Jump
 * @date 2023/5/23 17:49:30
 */
@Accessors(chain = true)
@Data
public class FrontLoginUserLogVo {

    private String userId;

    private String userType = "0";

    private Boolean newUser = Boolean.FALSE;

    private Date lastLoginTime;


}
