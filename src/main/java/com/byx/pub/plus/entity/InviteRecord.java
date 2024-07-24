package com.byx.pub.plus.entity;

import java.math.BigDecimal;
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
 * 邀请记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InviteRecord对象", description="邀请记录表")
public class InviteRecord extends Model<InviteRecord> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 数据状态(true:有效,false:删除)
     */
    @ApiModelProperty(value = "数据状态(true:有效,false:删除)")
    private Boolean dataStatus;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
