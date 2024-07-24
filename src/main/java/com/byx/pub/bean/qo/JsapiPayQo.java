package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * JSAPI支付下单Qo
 * @author Jump
 */
@Data
public class JsapiPayQo {
    /**
     * 用户id(前端不必传)
     */
    @ApiModelProperty(value = "用户id(前端不必传)")
    private String userId;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @NotNull(message = "订单号不能为空")
    @Size(max = 32, message = "订单号不能超过32个字符")
    private String orderSn;

    /**
     * 支付类型(1:全款支付，2:支付本期金额，3:支付剩余尾款)
     */
    @ApiModelProperty(value = "支付类型(1:全款支付，2:支付本期金额，3:支付剩余尾款)")
    @NotNull(message = "支付类型不能为空")
    @Range(min = 1,max = 3,message = "请选择正确的支付类型")
    private Integer payType;

}
