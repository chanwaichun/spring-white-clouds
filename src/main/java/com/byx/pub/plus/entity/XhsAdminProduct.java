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
 * 咨询师推送小红书商品推送表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="XhsAdminProduct对象", description="咨询师推送小红书商品推送表")
public class XhsAdminProduct extends Model<XhsAdminProduct> {

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
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String bid;

    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String productId;

    /**
     * 小红书skuId
     */
    @ApiModelProperty(value = "小红书skuId")
    private String xhsSkuId;

    /**
     * 小红书店铺id
     */
    @ApiModelProperty(value = "小红书店铺id")
    private String xhsSellerId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
