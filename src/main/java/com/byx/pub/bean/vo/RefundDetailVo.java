package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/9/11 20:39
 */
@Data
public class RefundDetailVo {

    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String orderSn;

    /**
     * 退款时间
     */
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;

    /**
     * 下单手机
     */
    @ApiModelProperty(value = "下单手机")
    private String userMobile;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String adminName;

    /**
     * 咨询师手机
     */
    @ApiModelProperty(value = "咨询师手机")
    private String adminMobile;

    /**
     * 累计退款金额
     */
    @ApiModelProperty(value = "累计退款金额")
    private BigDecimal refundAmount;

    /**
     * 退款状态(0->无退款，1->退款中，2->完成退款)
     */
    @ApiModelProperty(value = "退款状态(0->无退款，1->退款中，2->完成退款)")
    private Integer refundStatus;

    /**
     * 退款类型(1：部分退款，2：全额退款)
     */
    @ApiModelProperty(value = "退款类型(1：部分退款，2：全额退款)")
    private Integer refundType;

    /**
     * 退款记录
     */
    @ApiModelProperty(value = "退款记录")
    private List<RefundRecordVo> recordVoList = new ArrayList<>();

    /**
     * 订单商品列表
     */
    @ApiModelProperty(value = "订单商品列表")
    private List<OrderItemVo> itemVoList = new ArrayList<>();
}
