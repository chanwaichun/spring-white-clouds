package com.byx.pub.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信JSAPI下单Vo
 */
@Accessors(chain = true)
@Data
public class WxJspaiPayVo {
    /**
     * appid
     */
    private String appid;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 订单详情扩展字符串
     */
    private String packageValue;
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 签名
     */
    private String paySign;
}
