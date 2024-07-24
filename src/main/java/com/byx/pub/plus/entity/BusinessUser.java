package com.byx.pub.plus.entity;

import java.math.BigDecimal;
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
 * 咨询师会员表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BusinessUser对象", description="咨询师会员表")
public class BusinessUser extends Model<BusinessUser> {

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "updator", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updator;

    /**
     * 最近消费时间
     */
    @ApiModelProperty(value = "最近消费时间")
    private Date updateTime;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 咨询师id(只记录第一次)
     */
    @ApiModelProperty(value = "咨询师id(只记录第一次)")
    private String adminId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 累计消费金额
     */
    @ApiModelProperty(value = "累计消费金额")
    private BigDecimal totalAmount;

    /**
     * 累计订单数
     */
    @ApiModelProperty(value = "累计订单数")
    private Integer totalOrderNum;

    /**
     * 用户建号时间
     */
    @ApiModelProperty(value = "用户建号时间")
    private Date userCreateTime;

    /**
     * 用户类型(0：支付订单，1：查看商品，2：查看名片，3：查看电子传单，4：小红书下单)
     */
    @ApiModelProperty(value = "用户类型(0：支付订单，1：查看商品，2：查看名片，3：查看电子传单，4：小红书下单)")
    private Integer userType;

    /**
     * 小红书会员(0：否，1：是)
     */
    @ApiModelProperty(value = "小红书会员(0：否，1：是)")
    private Boolean xhsStatus;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
