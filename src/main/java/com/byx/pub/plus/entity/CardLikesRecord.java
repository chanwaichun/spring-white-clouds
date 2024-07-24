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
 * 名片点赞记录表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CardLikesRecord对象", description="名片点赞记录表")
public class CardLikesRecord extends Model<CardLikesRecord> {

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
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 点赞人id
     */
    @ApiModelProperty(value = "点赞人id")
    private String likesUid;

    /**
     * 点赞人昵称
     */
    @ApiModelProperty(value = "点赞人昵称")
    private String likesName;

    /**
     * 点赞人头像
     */
    @ApiModelProperty(value = "点赞人头像")
    private String likesImg;

    /**
     * 被赞人id
     */
    @ApiModelProperty(value = "被赞人id")
    private String likedUid;

    /**
     * 被赞人昵称
     */
    @ApiModelProperty(value = "被赞人昵称")
    private String likedName;

    /**
     * 被赞人头像
     */
    @ApiModelProperty(value = "被赞人头像")
    private String likedImg;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
