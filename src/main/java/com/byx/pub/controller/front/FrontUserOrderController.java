package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.CreateOrderQo;
import com.byx.pub.bean.qo.FrontPageOrdersQo;
import com.byx.pub.bean.vo.OrderDetailVo;
import com.byx.pub.bean.vo.PageOrdersVo;
import com.byx.pub.bean.vo.PreOrderVo;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [客户端]-[用户]-[订单]服务Api
 * @author Jump
 * @date 2023/5/24 16:08:49
 */
@RestController
@RequestMapping("/white/clouds/front/user/v1/order")
@Api(value = "[客户端]-[用户]-[订单]服务Api",tags = "[客户端]-[用户]-[订单]服务Api")
public class FrontUserOrderController {
    @Resource
    FrontOrderService orderService;

    /**
     * 生成待支付订单
     * @param userId
     * @param qo
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "生成待支付订单")
    public CommonResult<PreOrderVo> createOrder(
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
        @Validated @RequestBody CreateOrderQo qo
    ){
        return CommonResult.success(orderService.createOrder(qo,userId));
    }

    /**
     * 分页查询订单列表
     * @param userId 请求头id
     * @param queryQo 请求头
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询订单列表")
    public CommonResult<Page<PageOrdersVo>> pageList(
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
        @Validated @RequestBody FrontPageOrdersQo queryQo
    ){
        queryQo.setUserId(userId);
        return CommonResult.success(orderService.pageListOrder(queryQo));
    }

    /**
     * 取消订单
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/cancel")
    @ApiOperation(value = "取消订单")
    public CommonResult<Void> cancelOrder(
            @ApiParam(required = true, value = "订单id") @RequestParam("orderId") String orderId
    ){
        orderService.cancelOrder(orderId);
        return CommonResult.success();
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
        return CommonResult.success(orderService.getOrderDetail(orderSn));
    }



}
