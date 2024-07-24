package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 月账单vo
 * @Author Jump
 * @Date 2023/6/17 10:43
 */
@Data
@Accessors(chain = true)
public class MonthBillDetailVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 账期(2023-01)
     */
    @ApiModelProperty(value = "账期(2023-01)")
    private String billDate;

    /**
     * 订单总额(收到支付金额)
     */
    @ApiModelProperty(value = "订单总额(收到支付金额)")
    private BigDecimal orderAmount;

    /**
     * 实收金额(抽佣后)
     */
    @ApiModelProperty(value = "实收金额(抽佣后)")
    private BigDecimal realIncome;

    /**
     * 订单数
     */
    @ApiModelProperty(value = "订单数")
    private Integer orderNum;

    /**
     * 获客数
     */
    @ApiModelProperty(value = "获客数")
    private Integer customerNum;

    /**
     * 结算状态(true:已结算，false:未结算)
     */
    @ApiModelProperty(value = "结算状态(true:已结算，false:未结算)")
    private Boolean settlementStatus;

    /**
     * 支付订单列表
     */
    @ApiModelProperty(value = "支付订单列表")
    List<PagePayOrderListVo> payOrderList = new ArrayList<>();









}
