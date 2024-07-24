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
 * 临时用户表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TempAdmin对象", description="临时用户表")
public class TempAdmin extends Model<TempAdmin> {

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
     * openid
     */
    @ApiModelProperty(value = "openid")
    private String openId;

    /**
     * 用户绑定: 1绑定 0未绑定
     */
    @ApiModelProperty(value = "用户绑定: 1绑定 0未绑定")
    private Boolean bandStatus;

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
     * unionId
     */
    @ApiModelProperty(value = "unionId")
    private String unionId;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
