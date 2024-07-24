package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/12/28 23:02
 */
@Data
public class XhsOrderVo {
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单类型 1普通 2定金预售 3全款预售(废弃) 4全款预售(新) 5换货补发
     */
    private Integer orderType;
    /**
     * 订单状态，1已下单待付款 2已支付处理中 3清关中 4待发货 5部分发货 6待收货 7已完成 8已关闭 9已取消 10换货申请中
     */
    private Integer orderStatus;
    /**
     * 售后状态，1无售后 2售后处理中 3售后完成(含取消) 4售后拒绝 5售后关闭 6平台介入中
     */
    private Integer orderAfterSalesStatus;
    /**
     * 申请取消状态，0未申请取消 1取消处理中
     */
    private Long cancelStatus;
    /**
     * 创建时间 单位ms
     */
    private Long createdTime;
    /**
     * 支付时间 单位ms
     */
    private Long paidTime;
    /**
     * 更新时间 单位ms
     */
    private Long updateTime;
    /**
     * 订单发货时间 单位ms
     */
    private Long deliveryTime;
    /**
     * 订单取消时间 单位ms
     */
    private Long cancelTime;
    /**
     * 订单完成时间 单位ms
     */
    private Long finishTime;




}
