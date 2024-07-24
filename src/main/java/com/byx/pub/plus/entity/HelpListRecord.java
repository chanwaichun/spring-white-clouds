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
 * 互帮记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HelpListRecord对象", description="互帮记录表")
public class HelpListRecord extends Model<HelpListRecord> {

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
     * 帮助方式(1：推荐名片，2：分享商品)
     */
    @ApiModelProperty(value = "帮助方式(1：推荐名片，2：分享商品)")
    private Integer helpType;

    /**
     * 帮助者Id(admin表id)
     */
    @ApiModelProperty(value = "帮助者Id(admin表id)")
    private String helperId;

    /**
     * 帮助者名称
     */
    @ApiModelProperty(value = "帮助者名称")
    private String helper;

    /**
     * 被引流用户id
     */
    @ApiModelProperty(value = "被引流用户id")
    private String userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 受益者Id(admin表id)
     */
    @ApiModelProperty(value = "受益者Id(admin表id)")
    private String beneficiaryId;

    /**
     * 受益者名称
     */
    @ApiModelProperty(value = "受益者名称")
    private String beneficiary;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
