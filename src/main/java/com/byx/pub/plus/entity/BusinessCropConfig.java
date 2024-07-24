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
 * 商家企业微信配置表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BusinessCropConfig对象", description="商家企业微信配置表")
public class BusinessCropConfig extends Model<BusinessCropConfig> {

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
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private String cropId;

    /**
     * 应用id
     */
    @ApiModelProperty(value = "应用id")
    private String agentId;

    /**
     * 应用Secret
     */
    @ApiModelProperty(value = "应用Secret")
    private String secret;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
