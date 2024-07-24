package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/6/9 22:51
 */
@Data
@Accessors(chain = true)
public class PayRecordVo {
    /**
     * 支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    private String paySn;

    /**
     * 支付类型:1->微信
     */
    @ApiModelProperty(value = "支付类型:1->微信")
    private Integer payType;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额")
    private BigDecimal amount;

    /**
     * 完成支付时间
     */
    @ApiModelProperty(value = "完成支付时间")
    private Date successTime;

    /**
     * 交易状态:1->待支付，2->已支付，3->转入退款，4->已关闭，5->已撤销
     */
    @ApiModelProperty(value = "交易状态:1->待支付，2->已支付，3->转入退款，4->已关闭，5->已撤销")
    private Integer status;
}
