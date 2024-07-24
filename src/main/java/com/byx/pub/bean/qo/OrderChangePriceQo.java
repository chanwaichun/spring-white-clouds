package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author Jump
 * @date 2023/6/9 17:34:01
 */
@Data
public class OrderChangePriceQo {
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @NotNull(message = "订单id不能为空")
    @Size(max = 32, message = "订单id不能超过32个字符")
    private String orderId;


    @ApiModelProperty(value = "本期支付金额")
    @DecimalMin(value = "0.01", message = "本期支付金额不能小于0.01")
    @NotNull(message = "请填写本期支付金额")
    @Digits(integer = 8, fraction = 2, message = "本期支付金额整数上限是8位，小数上限是2位")
    private BigDecimal thisPayment;
}
