package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Jump
 * @date 2023/5/7 14:22:35
 */
@Data
public class AddOrUpdateAdminQo {
    /**
     * 账户id(新增不需,修改必需)
     */
    @ApiModelProperty(value = "账户id(新增不需,修改必需)")
    private String adminId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名不能为空")
    @Length(max = 20,message = "姓名最多20个字符")
    private String trueName;

    /**
     * 帐号(手机号)
     */
    @ApiModelProperty(value = "帐号(手机号)")
    @NotEmpty(message = "帐号不能为空")
    @Length(max = 11,message = "帐号最多11个字符")
    private String mobile;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @Length(max = 50,message = "密码最多50个字符")
    private String password;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)
     */
    @ApiModelProperty(value = "角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)")
    @NotEmpty(message = "请选择角色")
    @Pattern(regexp = "(1|2|3|4|5|6|7|8)", message = "请规范选择角色")
    private String roleId;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String userImg;

}
