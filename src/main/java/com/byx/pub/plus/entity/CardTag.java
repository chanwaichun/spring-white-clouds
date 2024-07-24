package com.byx.pub.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 名片标签表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CardTag对象", description="名片标签表")
public class CardTag extends Model<CardTag> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 名片表id
     */
    @ApiModelProperty(value = "名片表id")
    private String cardId;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private String tagId;

    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String tagName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
