package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 传单成交明细Vo
 * @author Jump
 * @date 2023/12/15 11:29:08
 */
@Data
public class FlyerDealDetailVo {
    /**
     * 成交订单号
     */
    @ApiModelProperty(value = "成交订单号")
    private String orderSn;

    /**
     * 成交商品
     */
    @ApiModelProperty(value = "成交商品")
    private String orderProduct;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String relaySjName;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String relayName;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String inviteeName;

    /**
     * 下单人手机
     */
    @ApiModelProperty(value = "下单人手机")
    private String inviteePhone;

    /**
     * 成交时间
     */
    @ApiModelProperty(value = "成交时间")
    private String dealTime;

}
