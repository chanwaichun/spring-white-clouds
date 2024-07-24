package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/9 23:08
 */
@Data
public class RefundChangeOrderDto {
    /**
     * 原订单号
     */
    private String orderSn;

    /**
     * 退款流订单号(逗号隔开)
     */
    private String refundSnStr;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款状态(0->无退款，1->退款中，2->完成退款)
     */
    private Integer refundStatus;

    /**
     * 成功时间
     */
    private Date successTime;

    /**
     * 退款类型(1->部分退款，2->全额退款)
     */
    private Integer refundType;

}
