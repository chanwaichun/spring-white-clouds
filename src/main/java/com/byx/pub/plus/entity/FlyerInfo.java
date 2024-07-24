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
 * 电子传单表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FlyerInfo对象", description="电子传单表")
public class FlyerInfo extends Model<FlyerInfo> {

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
     * 传单名称
     */
    @ApiModelProperty(value = "传单名称")
    private String flyerName;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家商家简称
     */
    @ApiModelProperty(value = "商家商家简称")
    private String businessName;

    /**
     * 分享文案
     */
    @ApiModelProperty(value = "分享文案")
    private String shareTitle;

    /**
     * 分享图片
     */
    @ApiModelProperty(value = "分享图片")
    private String shareImg;

    /**
     * 页面内容
     */
    @ApiModelProperty(value = "页面内容")
    private String pageContent;

    /**
     * 跳转按钮状态(true:开，false:关)
     */
    @ApiModelProperty(value = "跳转按钮状态(true:开，false:关)")
    private Boolean jumpButtonStatus;

    /**
     * 按钮名称
     */
    @ApiModelProperty(value = "按钮名称")
    private String buttonName;

    /**
     * 按钮跳转url
     */
    @ApiModelProperty(value = "按钮跳转url")
    private String buttonJumpUrl;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
