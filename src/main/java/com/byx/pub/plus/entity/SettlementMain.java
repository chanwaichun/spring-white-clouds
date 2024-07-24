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
 * 结算主表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SettlementMain对象", description="结算主表")
public class SettlementMain extends Model<SettlementMain> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 创建时间(出单时间)
     */
    @ApiModelProperty(value = "创建时间(出单时间)")
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
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private String productId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 结算人数
     */
    @ApiModelProperty(value = "结算人数")
    private Integer settlerNum;

    /**
     * 结算订单数
     */
    @ApiModelProperty(value = "结算订单数")
    private Integer orderNum;

    /**
     * 应收金额
     */
    @ApiModelProperty(value = "应收金额")
    private BigDecimal incomeAmount;

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    /**
     * 结算时间
     */
    @ApiModelProperty(value = "结算时间")
    private Date settlementDate;

    /**
     * 结算状态(false：待结算，true：已结算)
     */
    @ApiModelProperty(value = "结算状态(false：待结算，true：已结算)")
    private Boolean settlementStatus;

    /**
     * 账期(如：2023-09-01--2023-09-20)
     */
    @ApiModelProperty(value = "账期(如：2023-09-01--2023-09-20)")
    private String billDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
