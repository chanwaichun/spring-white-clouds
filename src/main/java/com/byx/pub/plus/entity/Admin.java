package com.byx.pub.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;

/**
 * <p>
 * 管理后台用户表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Admin对象", description="管理后台用户表")
public class Admin extends Model<Admin> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "admin_id", type = IdType.ID_WORKER_STR)
    private String adminId;

    /**
     * 创建者
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "updator", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updator;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态")
    private Boolean userStatus;

    /**
     * 账户(手机号)
     */
    @ApiModelProperty(value = "账户(手机号)")
    private String mobile;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String userPassword;

    /**
     * 登录ip
     */
    @ApiModelProperty(value = "登录ip")
    private String loginIp;

    /**
     * openid
     */
    @ApiModelProperty(value = "openid")
    private String openId;

    /**
     * unionId
     */
    @ApiModelProperty(value = "unionId")
    private String unionId;

    /**
     * session_key
     */
    @ApiModelProperty(value = "session_key")
    private String sessionKey;

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
     * 性别(0 未知 1 男 2女)
     */
    @ApiModelProperty(value = "性别(0 未知 1 男 2女)")
    private Integer gender;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String trueName;

    /**
     * 角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)
     */
    @ApiModelProperty(value = "角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)")
    private String roleId;

    /**
     * 数据状态(1->有效、0->无效)
     */
    @ApiModelProperty(value = "数据状态(1->有效、0->无效)")
    private Boolean dataStatus;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 企微用户id
     */
    @ApiModelProperty(value = "企微用户id")
    private String cropOpenUserid;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private String cropId;

    /**
     * 小红书店铺id
     */
    @ApiModelProperty(value = "小红书店铺id")
    private String xhsSellerId;


    @Override
    protected Serializable pkVal() {
        return this.adminId;
    }

}
