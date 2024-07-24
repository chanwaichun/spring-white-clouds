package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 退款Qo
 *
 * @program: pub-pay
 * @description: 退款Qo
 * @author: Jim
 * @create: 2021-12-15
 */
@Data
public class RefundsQo {
    /**
     * 用户id(前端不必传)
     */
    @ApiModelProperty(value = "用户id(前端不必传)")
    private String uid;
    /**
     * 订单号
     */
    @NotNull(message = "订单号不能为空")
    @Size(max = 32, message = "订单号不能超过32个字符")
    @ApiModelProperty(value = "订单号")
    private String orderSn;

    /**
     * 退款类型
     */
    @NotNull(message = "退款类型不能为空")
    @Range(min = 1,max = 2 ,message = "请传入规范的退款类型")
    @ApiModelProperty(value = "退款类型(1:部分退款，2:全额退款)")
    private Integer refundType;

    /**
     * 退款金额(部分支付必填且大于0)
     */
    @ApiModelProperty(value = "退款金额(部分支付必填且大于0)")
    private BigDecimal refundAmount;
}
