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
 * 小红书授权token表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="XhsAuthToken对象", description="小红书授权token表")
public class XhsAuthToken extends Model<XhsAuthToken> {

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
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String sellerId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String sellerName;

    /**
     * token
     */
    @ApiModelProperty(value = "token")
    private String accessToken;

    /**
     * 过期时间戳
     */
    @ApiModelProperty(value = "过期时间戳")
    private String tokenExpires;

    /**
     * 刷新token
     */
    @ApiModelProperty(value = "刷新token")
    private String refreshToken;

    /**
     * 刷新token过期时间戳
     */
    @ApiModelProperty(value = "刷新token过期时间戳")
    private String refreshExpires;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
