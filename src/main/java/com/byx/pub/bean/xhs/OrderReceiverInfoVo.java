package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/12/30 0:05
 */
@Data
public class OrderReceiverInfoVo {
    /**
     * 收件人姓名
     */
    private String receiverName;
    /**
     * 收件人电话
     */
    private String receiverPhone;
    /**
     * 省份
     */
    private String receiverProvinceName;
    /**
     * 城市
     */
    private String receiverCityName;
    /**
     * 区县
     */
    private String receiverDistrictName;
    /**
     * 镇/街道
     */
    private String receiverTownName;
    /**
     * 收件人详细地址
     */
    private String receiverAddress;


}
