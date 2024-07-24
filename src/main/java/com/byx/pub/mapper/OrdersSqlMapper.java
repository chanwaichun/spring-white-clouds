package com.byx.pub.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageListPayFlowQo;
import com.byx.pub.bean.qo.PageManageOrdersQo;
import com.byx.pub.bean.qo.PagePersonSettlementQo;
import com.byx.pub.bean.qo.PageRefundOrderQo;
import com.byx.pub.bean.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单自定义sql查询
 * @author Jump
 * @date 2023/6/8 15:04:51
 */
public interface OrdersSqlMapper {

    /**
     * 查询管理后台订单列表
     * @param page
     * @param queryQo
     * @return
     */
    Page<PageOrdersVo> pageManageOrdersList(Page page,@Param(value = "queryQo") PageManageOrdersQo queryQo);

    /**
     * 分页查询退款订单列表
     * @param page
     * @param queryQo
     * @return
     */
    Page<PageRefundOrderVo> pageRefundOrdersList(Page page, @Param(value = "queryQo") PageRefundOrderQo queryQo);

    /**
     * 查询总收益
     * @param businessId
     * @param adminId
     * @return
     */
    BigDecimal queryTotalIncome(@Param(value = "businessId") String businessId,@Param(value = "adminId") String adminId);

    /**
     * 查询一段时间内收益
     * @param businessId
     * @param adminId
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryMonthIncome(
            @Param(value = "businessId") String businessId,
            @Param(value = "adminId") String adminId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);

    /**
     * 查询一段时间内获客
     * @param businessId
     * @param adminId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryMonthCustomer(
            @Param(value = "businessId") String businessId,
            @Param(value = "adminId") String adminId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);

    /**
     * 查询当月订单数(第一次支付)
     * @param businessId
     * @param adminId
     * @param monthDate
     * @return
     */
    Integer queryMonthOrders(
            @Param(value = "businessId") String businessId,
            @Param(value = "adminId") String adminId,
            @Param(value = "monthDate") String monthDate);


    /**
     * 查询某一段时间内的订单数(支付、去重)
     * @param adminId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryOrderNum(
            @Param(value = "adminId") String adminId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime
    );

    /**
     * 查询某一段时间内 支付订单记录
     * @param adminId
     * @param startTime
     * @param endTime
     * @return
     */
    List<PagePayOrderListVo> queryPayOrderList(
            @Param(value = "adminId") String adminId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime
    );

    /**
     * 查询待支付订单数量
     * @param adminId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryNotWcOrderNum(
            @Param(value = "adminId") String adminId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime
    );

    /**
     * 查询支付过的订单数
     * @param adminId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryPayOrderNum(
            @Param(value = "adminId") String adminId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime
    );

    /**
     * 分页条件查询支付流水
     * @param page
     * @param qo
     * @return
     */
    Page<ConsultantIncomeVo> queryPayFlowList(Page page, @Param(value = "qo") PageListPayFlowQo qo);

    /**
     * 根据商品查询订单
     * @param productId
     * @return
     */
    @Select(" SELECT o.id FROM orders AS o LEFT JOIN order_detail AS od ON od.order_sn = o.order_sn WHERE od.product_id = #{productId}  AND o.`status` > 1 ")
    List<String> queryOrderByProduct(@Param(value = "productId") String productId);

    /**
     * 查询用户下过单的咨询师列表
     * @param userId
     * @return
     */
    @Select(" SELECT id AS cardId,admin_photo AS adminPhoto,true_name AS trueName FROM admin_card where admin_id in " +
            "(select admin_id from orders where user_id = #{userId} AND pay_status > 0 ORDER BY update_time DESC) ")
    List<AdminShortCardVo> queryUserByOrderAdminList(@Param(value = "userId") String userId);


    /**
     * 查询商家某段时间内收入
     * @param businessId
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryPayAmountBySj(
            @Param(value = "businessId") String businessId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);

    /**
     * 查询商家某段时间内订单数
     * @param businessId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryOrderNumBySj(
            @Param(value = "businessId") String businessId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);

    /**
     * 查询商家某段时间内支付用户数
     * @param businessId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryPayUserNumBySj(
            @Param(value = "businessId") String businessId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);


    /**
     * 查询商家某段时间内待支付金额
     * @param businessId
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal queryNoPayOrderAmountBySj(
            @Param(value = "businessId") String businessId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);

    /**
     * 查询商家某段时间内未支付订单数
     * @param businessId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer queryNoPayOrderNumBySj(
            @Param(value = "businessId") String businessId,
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);


    /**
     * 分页查询个人结算在服务的结算
     * @param page
     * @param queryQo
     * @return
     */
    Page<PagePersonSettlementVo> pagePersonSettlementList(Page page,@Param(value = "queryQo") PagePersonSettlementQo queryQo);

    /**
     * 合计订单结算金额
     * @param mainId
     * @param orderSn
     * @return
     */
    @Select(" select IFNULL(sum(settlement_amount),0) AS sumAmount from settlement_detail where main_id = #{mainId}  AND order_sn =#{orderSn} ")
    BigDecimal sumSettlementAmountByOrderSn(
            @Param(value = "mainId") String mainId,
            @Param(value = "orderSn") String orderSn
    );
}
