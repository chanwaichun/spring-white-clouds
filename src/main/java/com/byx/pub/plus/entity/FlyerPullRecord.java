package com.byx.pub.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 传单拉新记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FlyerPullRecord对象", description="传单拉新记录表")
public class FlyerPullRecord extends Model<FlyerPullRecord> {

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
     * 分享主键
     */
    @ApiModelProperty(value = "分享主键")
    private String shareId;

    /**
     * 传单id
     */
    @ApiModelProperty(value = "传单id")
    private String flyerId;

    /**
     * 传单名称
     */
    @ApiModelProperty(value = "传单名称")
    private String flyerName;

    /**
     * 分享人uid
     */
    @ApiModelProperty(value = "分享人uid")
    private String shareUid;

    /**
     * 分享人昵称
     */
    @ApiModelProperty(value = "分享人昵称")
    private String shareName;

    /**
     * 受邀人uid
     */
    @ApiModelProperty(value = "受邀人uid")
    @TableField("Invitee_uid")
    private String inviteeUid;

    /**
     * 受邀人昵称
     */
    @ApiModelProperty(value = "受邀人昵称")
    @TableField("Invitee_name")
    private String inviteeName;

    /**
     * 成交状态(true：成交，false：未成交)
     */
    @ApiModelProperty(value = "成交状态(true：成交，false：未成交)")
    private Boolean dealStatus;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
