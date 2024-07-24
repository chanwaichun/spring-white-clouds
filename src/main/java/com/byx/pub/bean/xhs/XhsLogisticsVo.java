package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2024/1/6 0:37
 */
@Data
public class XhsLogisticsVo {
    /**
     * 物流方案id
     */
    private String planInfoId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 贸易模式 0：内贸 1：保税 2：直邮
     */
    private Integer tradeMode;
    /**
     * 物流方案名称
     */
    private String logisticName;
    /**
     * 发货地国家名称
     */
    private String countryName;
    /**
     * 发货地省份名称
     */
    private String privinceName;
    /**
     * 发货地城市名称
     */
    private String cityName;
    /**
     * 发货地街道信息
     */
    private String street;
    /**
     * 发货地邮政编码
     */
    private String postCode;



}
