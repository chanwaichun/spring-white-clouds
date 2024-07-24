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
 * 订单明细表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderDetail对象", description="订单明细表")
public class OrderDetail extends Model<OrderDetail> {

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
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String productId;

    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String productName;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer quantity;

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String img;

    /**
     * 协议图片
     */
    @ApiModelProperty(value = "协议图片")
    private String protocol;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
