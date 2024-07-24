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
 * 客户组表规则表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MemberGroupRule对象", description="客户组表规则表")
public class MemberGroupRule extends Model<MemberGroupRule> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    private String groupId;

    /**
     * 用户行为(1：订单状态，2：电子传单，3：购买服务)
     */
    @ApiModelProperty(value = "用户行为(1：订单状态，2：电子传单，3：购买服务)")
    private Integer ruleType;

    /**
     * 行为值(订单状态：0：取消，1：待支付，2：支付定金，4：已完成)，传单id，服务id
     */
    @ApiModelProperty(value = "行为值(订单状态：0：取消，1：待支付，2：支付定金，4：已完成)，传单id，服务id")
    private String ruleValue;

    /**
     * 行为文案(状态名称，传单名称，服务名称)
     */
    @ApiModelProperty(value = "行为文案(状态名称，传单名称，服务名称)")
    private String ruleTxt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
