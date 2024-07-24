package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Jump
 * @Date 2023/6/17 10:33
 */
@Data
@Accessors(chain = true)
public class PagePayOrderListVo {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private String successTime;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String orderTime;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private String amount;

}
