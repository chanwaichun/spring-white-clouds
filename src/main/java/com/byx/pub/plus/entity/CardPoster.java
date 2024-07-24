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
 * 名片广告表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CardPoster对象", description="名片广告表")
public class CardPoster extends Model<CardPoster> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private String productId;

    /**
     * 广告位图
     */
    @ApiModelProperty(value = "广告位图")
    private String posterUrl;

    /**
     * 广告类型(1：广告商品，2：广告图)
     */
    @ApiModelProperty(value = "广告类型(1：广告商品，2：广告图)")
    private Integer posterType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
