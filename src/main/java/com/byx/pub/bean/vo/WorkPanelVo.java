package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作面板vo
 * @Author Jump
 * @Date 2023/6/14 21:16
 */
@Data
@Accessors(chain = true)
public class WorkPanelVo {

    /**
     * 总收入
     */
    @ApiModelProperty(value = "总收入")
    private BigDecimal totalIncome;

    /**
     * 本月收入
     */
    @ApiModelProperty(value = "本月收入")
    private BigDecimal monthIncome;

    /**
     * 本月获客
     */
    @ApiModelProperty(value = "本月获客")
    private Integer monthCustomer;

    /**
     * 本月订单
     */
    @ApiModelProperty(value = "本月订单")
    private Integer monthOrders;

    /**
     * 订单列表
     */
    List<PageOrdersVo> orderList = new ArrayList<>();

}
