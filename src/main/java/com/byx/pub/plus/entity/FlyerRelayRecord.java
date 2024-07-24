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
 * 转发邀请记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FlyerRelayRecord对象", description="转发邀请记录表")
public class FlyerRelayRecord extends Model<FlyerRelayRecord> {

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
     * 转发主键
     */
    @ApiModelProperty(value = "转发主键")
    private String relayId;

    /**
     * 传单商家id
     */
    @ApiModelProperty(value = "传单商家id")
    private String flyerBid;

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
     * 转发人uid
     */
    @ApiModelProperty(value = "转发人uid")
    private String relayUid;

    /**
     * 转发人昵称
     */
    @ApiModelProperty(value = "转发人昵称")
    private String relayName;

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
     * 成交订单号
     */
    @ApiModelProperty(value = "成交订单号")
    private String orderSn;

    /**
     * 成交商品
     */
    @ApiModelProperty(value = "成交商品")
    private String orderProduct;

    /**
     * 转发商家名称
     */
    @ApiModelProperty(value = "转发商家名称")
    private String relaySjName;

    /**
     * 受邀人手机
     */
    @ApiModelProperty(value = "受邀人手机")
    private String inviteePhone;

    /**
     * 成交时间
     */
    @ApiModelProperty(value = "成交时间")
    private String dealTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
