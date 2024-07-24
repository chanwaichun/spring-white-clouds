package com.byx.pub.bean.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author Jump
 * @Date 2023/9/26 22:22
 */
@Data
public class ImportChangeDetailExcelBo {
    /**
     * 主键
     */
    private String id;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 商家名称
     */
    private String businessName;
    /**
     * 结算人后台名称
     */
    private String settlerAdminName;
    /**
     * 分成类型(1：拉新，2：促成，3：其他)
     */
    private String shareType;
    /**
     * 结算金额
     */
    private BigDecimal settlementAmount;
    /**
     * 创建时间(出单时间)
     */
    private String createTime;
}
