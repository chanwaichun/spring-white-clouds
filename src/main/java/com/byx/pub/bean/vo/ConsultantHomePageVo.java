package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 咨询师首页vo
 * @Author Jump
 * @Date 2023/6/17 12:53
 */
@Data
@Accessors(chain = true)
public class ConsultantHomePageVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String adminId;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    /**
     * 结算状态(true:已结算，false:未结算)
     */
    @ApiModelProperty(value = "结算状态(true:已结算，false:未结算)")
    private Boolean settlementStatus;

    /**
     * 本月待支付订单(待支付、部分支付)
     */
    @ApiModelProperty(value = "本月待支付订单(待支付、部分支付)")
    private Integer notCompletedOrder;

    /**
     * 本月订单(部分支付、完成支付)
     */
    @ApiModelProperty(value = "本月订单(部分支付、完成支付)")
    private Integer payOrder;

    /**
     * 本月收入(支付金额)
     */
    @ApiModelProperty(value = "本月收入(支付金额)")
    private BigDecimal payAmount;

    /**
     * 本月客户
     */
    @ApiModelProperty(value = "本月客户")
    private Integer customerNum;

    /**
     * 总代支付订单
     */
    @ApiModelProperty(value = "总代支付订单")
    private Integer totalNotCompletedOrder;

    /**
     * 总订单(部分支付、完成支付)
     */
    @ApiModelProperty(value = "总订单(部分支付、完成支付)")
    private Integer totalPayOrder;

    /**
     * 总收入(支付金额)
     */
    @ApiModelProperty(value = "总收入(支付金额)")
    private BigDecimal totalPayAmount;

    /**
     * 总客户
     */
    @ApiModelProperty(value = "总客户")
    private Integer totalCustomerNum;


}
