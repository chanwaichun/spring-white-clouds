package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.*;
import com.byx.pub.bean.vo.*;
import com.byx.pub.plus.entity.OrderDetail;
import com.byx.pub.plus.entity.Orders;
import com.byx.pub.plus.entity.PayRecord;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jump
 * @date 2023/5/24 17:13:58
 */
public interface FrontOrderService {

    /**
     * 生成预订单
     * @param qo
     * @param userId
     * @return
     */
    PreOrderVo createOrder(CreateOrderQo qo, String userId);

    /**
     * 根据订单号查询订单
     * @param orderSn
     * @return
     */
    Orders getByOrderSn(String orderSn);

    /**
     * 支付回调更改支付状态
     * @param recordId 支付记录
     */
    void changeOrderStatus(String recordId);

    /**
     * 分页查询订单列表
     * @param queryQo
     * @return
     */
    Page<PageOrdersVo> pageListOrder(FrontPageOrdersQo queryQo);

    /**
     * 取消订单
     * @param id
     */
    void cancelOrder(String id);

    /**
     * 订单详情(通用)
     * @param orderSn
     * @return
     */
    OrderDetailVo getOrderDetail(String orderSn);

    /**
     * 管理后台查询订单列表
     * @param queryQo
     * @return
     */
    Page<PageOrdersVo> queryManagePageOrders(PageManageOrdersQo queryQo);

    /**
     * 更改订单为分期付款订单
     * @param orderId
     */
    void changePayType(String orderId);

    /**
     * 设置本次支付金额
     * @param qo
     */
    void setThisPayment(OrderChangePriceQo qo);

    /**
     * 查询商家订单列表
     * @param qo
     * @return
     */
    Page<DataCountTradeOrderVo> pageSjOrderList(TradeOrderListQo qo);

    /**
     * 根据订单号查询订单详情
     * @param orderSn
     * @return
     */
    OrderDetail getOrderDetailByOrderSn(String orderSn);

    /**
     * 校验退款并返回订单金额
     * @param refundsQo
     * @return
     */
    Orders checkOrderRefund(RefundsQo refundsQo);

    /**
     * 退款成功变更订单信息
     * @param changeOrderDto
     */
    void changeOrderRefundStatus(RefundChangeOrderDto changeOrderDto);

    /**
     * 分页查询退款订单列表
     * @param queryQo
     * @return
     */
    Page<PageRefundOrderVo> pageRefundList(PageRefundOrderQo queryQo);

    /**
     * 获取退款单详情
     * @param orderSn
     * @return
     */
    RefundDetailVo getRefundDetailVo(String orderSn);

    /**
     * 手动完成订单
     * @param orderSn
     */
    void handDoneOrder(String orderSn);

    /**
     * 结算订单
     * @param orderSn
     */
    void settlementOrder(String orderSn);

    /**
     * 取消24小时以上未支付订单
     */
    void closedNoPayOrder();
}
