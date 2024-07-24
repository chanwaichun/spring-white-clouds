package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/11 20:20
 */
@Data
public class RefundRecordVo {

    /**
     * 退款单号
     */
    @ApiModelProperty(value = "退款单号")
    private String refundSn;

    /**
     * 发起退款时间
     */
    @ApiModelProperty(value = "发起退款时间")
    private Date createTime;

    /**
     * 退款流水号
     */
    @ApiModelProperty(value = "退款流水号")
    private String refundId;

    /**
     * 退款方式，1：微信，2：支付宝
     */
    @ApiModelProperty(value = "退款方式，1：微信，2：支付宝")
    private Integer refundType;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private Date successTime;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 退款状态，1：退款处理中,2：退款成功,3：退款异常,4:退款关闭
     */
    @ApiModelProperty(value = "退款状态，1：退款处理中,2：退款成功,3：退款异常,4:退款关闭")
    private Integer status;

}
