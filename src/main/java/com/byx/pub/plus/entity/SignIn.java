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
 * 打卡表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SignIn对象", description="打卡表")
public class SignIn extends Model<SignIn> {

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
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 每日打开次数状态(true=开启 false=关闭)
     */
    @ApiModelProperty(value = "每日打开次数状态(true=开启 false=关闭)")
    private Boolean dailyStatus;

    /**
     * 每日可打卡次数
     */
    @ApiModelProperty(value = "每日可打卡次数")
    private Integer dailyNum;

    /**
     * 可补卡次数
     */
    @ApiModelProperty(value = "可补卡次数")
    private Integer completeNum;

    /**
     * 参与条件1=任何人可参与  2=需要购买服务参与
     */
    @ApiModelProperty(value = "参与条件1=任何人可参与  2=需要购买服务参与")
    private Integer condition;

    /**
     * 封面图
     */
    @ApiModelProperty(value = "封面图")
    private String coverImg;

    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    private String productId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
