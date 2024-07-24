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
 * 客户表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="客户表")
public class User extends Model<User> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

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
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

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
     * 角色类型(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)
     */
    @ApiModelProperty(value = "角色类型(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)")
    private Integer roleId;

    /**
     * 后台id
     */
    @ApiModelProperty(value = "后台id")
    private String adminId;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

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
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址")
    private String shippingAddress;

    /**
     * 邀请状态(false：未校验，true：已校验)
     */
    @ApiModelProperty(value = "邀请状态(false：未校验，true：已校验)")
    private Boolean inviteStatus;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
