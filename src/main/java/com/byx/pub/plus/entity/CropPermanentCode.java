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
 * 企业永久授权码表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CropPermanentCode对象", description="企业永久授权码表")
public class CropPermanentCode extends Model<CropPermanentCode> {

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
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private String cropId;

    /**
     * 企业永久授权码
     */
    @ApiModelProperty(value = "企业永久授权码")
    private String permanentCode;

    /**
     * 授权信息json
     */
    @ApiModelProperty(value = "授权信息json")
    private String authCorpInfo;

    /**
     * 企业access_token
     */
    @ApiModelProperty(value = "企业access_token")
    private String accessToken;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
