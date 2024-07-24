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
 * 传单转发表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FlyerRelay对象", description="传单转发表")
public class FlyerRelay extends Model<FlyerRelay> {

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
     * 更新者
     */
    @TableField(value = "updator", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updator;

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
     * 数据状态(true:有效，false:无效)
     */
    @ApiModelProperty(value = "数据状态(true:有效，false:无效)")
    private Boolean dataStatus;

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
     * 分享人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家助教，7：商家运营，8：合伙人，0：消费者)
     */
    @ApiModelProperty(value = "分享人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家助教，7：商家运营，8：合伙人，0：消费者)")
    private Integer shareRid;

    /**
     * 分享人uid
     */
    @ApiModelProperty(value = "分享人uid")
    private String shareUid;

    /**
     * 分享人姓名
     */
    @ApiModelProperty(value = "分享人姓名")
    private String shareName;

    /**
     * 转发人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家助教，7：商家运营，8：合伙人，0：消费者)
     */
    @ApiModelProperty(value = "转发人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家助教，7：商家运营，8：合伙人，0：消费者)")
    private Integer relayRid;

    /**
     * 转发人商家id
     */
    @ApiModelProperty(value = "转发人商家id")
    private String relayBid;

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
     * 供应商角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家助教，7：商家运营，8：合伙人，0：消费者)
     */
    @ApiModelProperty(value = "供应商角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家助教，7：商家运营，8：合伙人，0：消费者)")
    private Integer supplierRid;

    /**
     * 供应商商家id
     */
    @ApiModelProperty(value = "供应商商家id")
    private String supplierBid;

    /**
     * 供应商uid
     */
    @ApiModelProperty(value = "供应商uid")
    private String supplierUid;

    /**
     * 供应商昵称
     */
    @ApiModelProperty(value = "供应商昵称")
    private String supplierName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
