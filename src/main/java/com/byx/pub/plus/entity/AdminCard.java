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
 * 咨询师名片表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AdminCard对象", description="咨询师名片表")
public class AdminCard extends Model<AdminCard> {

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
     * 数据状态(true:有效,false:删除)
     */
    @ApiModelProperty(value = "数据状态(true:有效,false:删除)")
    private Boolean dataStatus;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家全称
     */
    @ApiModelProperty(value = "商家全称")
    private String fullName;

    /**
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String shortName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

    /**
     * 咨询师名称(同步admin表)
     */
    @ApiModelProperty(value = "咨询师名称(同步admin表)")
    private String trueName;

    /**
     * 咨询师靓照(证件照)
     */
    @ApiModelProperty(value = "咨询师靓照(证件照)")
    private String adminPhoto;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private Long province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private Long city;

    /**
     * 区/县
     */
    @ApiModelProperty(value = "区/县")
    private Long area;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 个人介绍
     */
    @ApiModelProperty(value = "个人介绍")
    private String adminDesc;

    /**
     * 语音介绍(地址)
     */
    @ApiModelProperty(value = "语音介绍(地址)")
    private String voiceDesc;

    /**
     * 导师介绍
     */
    @ApiModelProperty(value = "导师介绍")
    private String textDesc;

    /**
     * 购买用户数
     */
    @ApiModelProperty(value = "购买用户数")
    private Integer buyUserNum;

    /**
     * 服务时长
     */
    @ApiModelProperty(value = "服务时长")
    private BigDecimal serviceTimes;

    /**
     * 微信授权头像
     */
    @ApiModelProperty(value = "微信授权头像")
    private String userImg;

    /**
     * 名片二维码
     */
    @ApiModelProperty(value = "名片二维码")
    private String qrCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
