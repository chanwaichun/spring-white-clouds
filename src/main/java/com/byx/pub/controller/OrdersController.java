package com.byx.pub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageManageOrdersQo;
import com.byx.pub.bean.qo.PageRefundOrderQo;
import com.byx.pub.bean.vo.OrderDetailVo;
import com.byx.pub.bean.vo.PageOrdersVo;
import com.byx.pub.bean.vo.PageRefundOrderVo;
import com.byx.pub.bean.vo.RefundDetailVo;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [管理后台]-[订单]管理Api
 * @author Jump
 * @date 2023/6/8 15:14:39
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/orders")
@Api(value = "[管理后台]-[订单]管理Api",tags = "[管理后台]-[订单]管理Api")
public class OrdersController {
    @Resource
    FrontOrderService frontOrderService;


    /**
     * 分页查询订单信息
     * @param businessId
     * @param adminRole
     * @param adminId
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询订单信息")
    public CommonResult<Page<PageOrdersVo>> pageList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(value = "当前用户角色")@RequestHeader(value = "admin-role",required = false) String adminRole,
        @ApiParam(value = "当前用户id")@RequestHeader(value = "admin-id",required = false) String adminId,
        @Validated @RequestBody PageManageOrdersQo qo
    ){
        qo.setBusinessId(businessId);
        if(StringUtil.isSeeSelfData(adminRole)){
            qo.setAdminId(adminId);
        }
        return CommonResult.success(this.frontOrderService.queryManagePageOrders(qo));
    }

    /**
     * 订单详情
     * @param orderSn 订单号
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(value = "订单详情")
    public CommonResult<OrderDetailVo> orderDetail(
            @ApiParam(required = true, value = "订单号") @RequestParam("orderSn") String orderSn
    ){
        return CommonResult.success(frontOrderService.getOrderDetail(orderSn));
    }

    /**
     * 分页查询退款订单信息
     * @param businessId
     * @param adminRole
     * @param adminId
     * @param qo
     * @return
     */
    @PostMapping("/page/refund/list")
    @ApiOperation(value = "分页查询退款订单信息")
    public CommonResult<Page<PageRefundOrderVo>> pageRefundList(
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
            @ApiParam(value = "当前用户角色")@RequestHeader(value = "admin-role",required = false) String adminRole,
            @ApiParam(value = "当前用户id")@RequestHeader(value = "admin-id",required = false) String adminId,
            @Validated @RequestBody PageRefundOrderQo qo
    ){
        qo.setBusinessId(businessId);
        if(StringUtil.isSeeSelfData(adminRole)){
            qo.setAdminId(adminId);
        }
        return CommonResult.success(this.frontOrderService.pageRefundList(qo));
    }


    /**
     * 获取退款单详情
     * @param orderSn 订单号
     * @return
     */
    @GetMapping("/get/refund/detail")
    @ApiOperation(value = "获取退款单详情")
    public CommonResult<RefundDetailVo> getRefundDetail(
        @ApiParam(required = true, value = "订单号") @RequestParam("orderSn") String orderSn
    ){
        return CommonResult.success(this.frontOrderService.getRefundDetailVo(orderSn));
    }


    /**
     * 手动完成订单
     * @param orderSn
     * @return
     */
    @GetMapping("hand/done")
    @ApiOperation(value = "手动完成订单")
    public CommonResult<Void> handDone(
            @ApiParam(required = true, value = "订单号") @RequestParam("orderSn") String orderSn
    ){
        frontOrderService.handDoneOrder(orderSn);
        return CommonResult.success();
    }


}
