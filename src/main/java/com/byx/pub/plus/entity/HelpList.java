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
 * 互帮表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HelpList对象", description="互帮表")
public class HelpList extends Model<HelpList> {

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
     * 数据状态(true:有效，false:无效)
     */
    @ApiModelProperty(value = "数据状态(true:有效，false:无效)")
    private Boolean dataStatus;

    /**
     * 帮助者Id(admin表id)
     */
    @ApiModelProperty(value = "帮助者Id(admin表id)")
    private String helperId;

    /**
     * 帮助者名称
     */
    @ApiModelProperty(value = "帮助者名称")
    private String helper;

    /**
     * 帮助者头像
     */
    @ApiModelProperty(value = "帮助者头像")
    private String helperImg;

    /**
     * 帮助TA人数
     */
    @ApiModelProperty(value = "帮助TA人数")
    private Integer helpTo;

    /**
     * 受益者Id(admin表id)
     */
    @ApiModelProperty(value = "受益者Id(admin表id)")
    private String beneficiaryId;

    /**
     * 受益者名称
     */
    @ApiModelProperty(value = "受益者名称")
    private String beneficiary;

    /**
     * 受益者头像
     */
    @ApiModelProperty(value = "受益者头像")
    private String beneficiaryImg;

    /**
     * TA帮我的数量
     */
    @ApiModelProperty(value = "TA帮我的数量")
    private Integer toHelp;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
