package com.byx.pub.bean.vo;

import lombok.Data;

/**
 * 供应商转发数据vo
 * @Author Jump
 * @Date 2023/12/15 0:00
 */
@Data
public class SupplierRelayVo {

    /**
     * 传单名称
     */
    private String flyerName;

    /**
     * 传单商家名称
     */
    private String flyerSjName;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 拉新人数
     */
    private String pullNum;

    /**
     * 成交人数
     */
    private String dealNum;



}
