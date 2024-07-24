package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商家用户画像vo
 * @Author Jump
 * @Date 2023/10/28 12:20
 */
@Data
public class BusinessUserPortraitVo {
    /**
     * 日志关联业务id(订单号、商品id、名片id....)
     */
    @ApiModelProperty(value = "日志关联业务id(订单号、商品id、名片id....)")
    private String pId;

    /**
     * 日志时间
     */
    @ApiModelProperty(value = "日志时间")
    private String logTime;

    /**
     * 活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付、7：名片浏览、8：服务浏览)
     */
    @ApiModelProperty(value = "活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付、7：名片浏览、8：服务浏览)")
    private Integer logType;

    /**
     * 日志文案
     */
    @ApiModelProperty(value = "日志文案")
    private String logText;

}
