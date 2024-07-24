package com.byx.pub.bean.xhs;

import lombok.Data;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/12/29 23:31
 */
@Data
public class XhsOrderDetailVo {
    /**
     * 订单号
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
    private Integer cancelStatus;
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
     * 订单完成时间 单位ms
     */
    private Long finishTime;
    /**
     * 订单实付金额(包含运费和定金) 单位分
     */
    private Integer totalPayAmount;
    /**
     * 订单收货人id
     */
    private String openAddressId;
    /**
     * 支付方式 1：支付宝 2：微信 3：apple 内购 4：apple pay 5：花呗分期 7：支付宝免密支付 8：云闪付 -1：其他
     */
    private Integer paymentType;
    /**
     * 订单sku列表
     */
    List<XhsOrderSkuVo> skuList;

}
