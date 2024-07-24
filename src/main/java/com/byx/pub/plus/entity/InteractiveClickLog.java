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
 * 互动点击日志表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InteractiveClickLog对象", description="互动点击日志表")
public class InteractiveClickLog extends Model<InteractiveClickLog> {

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
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private String promotionId;

    /**
     * 活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付、7：名片浏览、8：服务浏览)
     */
    @ApiModelProperty(value = "活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付、7：名片浏览、8：服务浏览)")
    private Integer promotionType;

    /**
     * 分享人uid
     */
    @ApiModelProperty(value = "分享人uid")
    private String shareUid;

    /**
     * 点击人uid
     */
    @ApiModelProperty(value = "点击人uid")
    private String clickUid;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String bid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
