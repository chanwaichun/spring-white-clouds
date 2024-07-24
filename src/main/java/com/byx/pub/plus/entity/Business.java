package com.byx.pub.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.time.LocalDate;
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
 * 商家表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Business对象", description="商家表")
public class Business extends Model<Business> {

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
     * 数据状态(true:有效,false:删除)
     */
    @ApiModelProperty(value = "数据状态(true:有效,false:删除)")
    private Boolean dataStatus;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String businessCode;

    /**
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String shortName;

    /**
     * 商家全称
     */
    @ApiModelProperty(value = "商家全称")
    private String fullName;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String telephone;

    /**
     * 商家性质(1：个人，2：机构)
     */
    @ApiModelProperty(value = "商家性质(1：个人，2：机构)")
    private Integer businessType;

    /**
     * 套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)
     */
    @ApiModelProperty(value = "套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)")
    private Integer suitType;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    /**
     * 企业微信corpid
     */
    @ApiModelProperty(value = "企业微信corpid")
    private String corpId;

    /**
     * 企业微信Secret
     */
    @ApiModelProperty(value = "企业微信Secret")
    private String secretSn;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
