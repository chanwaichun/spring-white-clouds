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
 * 打卡记录
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SignInRecord对象", description="打卡记录")
public class SignInRecord extends Model<SignInRecord> {

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
     * 打卡id
     */
    @ApiModelProperty(value = "打卡id")
    private String signInId;

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

    /**
     * 打卡人id
     */
    @ApiModelProperty(value = "打卡人id")
    private String userId;

    /**
     * 打卡人昵称
     */
    @ApiModelProperty(value = "打卡人昵称")
    private String nickName;

    /**
     * 打卡人头像
     */
    @ApiModelProperty(value = "打卡人头像")
    private String userImg;

    /**
     * 第几天打卡
     */
    @ApiModelProperty(value = "第几天打卡")
    private Integer count;

    /**
     * 已坚持的天数
     */
    @ApiModelProperty(value = "已坚持的天数")
    private Integer total;

    /**
     * 打卡的文字内容
     */
    @ApiModelProperty(value = "打卡的文字内容")
    private String content;

    /**
     * 是否置顶 true=是 false=否
     */
    @ApiModelProperty(value = "是否置顶 true=是 false=否")
    private Boolean isTop;

    /**
     * 置顶时间
     */
    @ApiModelProperty(value = "置顶时间")
    private Date topTime;

    /**
     * 打卡时间
     */
    @ApiModelProperty(value = "打卡时间")
    private Date signInTime;

    /**
     * 是否为补卡 true=是 false=否
     */
    @ApiModelProperty(value = "是否为补卡 true=是 false=否")
    private Boolean isComplete;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
