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
 * 登录token表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LoginToken对象", description="登录token表")
public class LoginToken extends Model<LoginToken> {

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
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 登录人id
     */
    @ApiModelProperty(value = "登录人id")
    private String loginId;

    /**
     * 登录人token
     */
    @ApiModelProperty(value = "登录人token")
    private String tokenStr;

    /**
     * 返回前端的token
     */
    @ApiModelProperty(value = "返回前端的token")
    private String resStr;

    /**
     * 客户端类型(manage->管理后端,front->前端)
     */
    @ApiModelProperty(value = "客户端类型(manage->管理后端,front->前端)")
    private String clientType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
