package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.CreateOrderQo;
import com.byx.pub.bean.qo.FrontPageOrdersQo;
import com.byx.pub.bean.qo.OrderChangePriceQo;
import com.byx.pub.bean.qo.RefundsQo;
import com.byx.pub.bean.vo.OrderDetailVo;
import com.byx.pub.bean.vo.PageOrdersVo;
import com.byx.pub.bean.vo.PreOrderVo;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.service.WxPayService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * [客户端]-[咨询师]-[订单]服务Api
 * @author Jump
 * @date 2023/5/24 16:08:49
 */
@RestController
@RequestMapping("/white/clouds/front/consultant/v1/order")
@Api(value = "[客户端]-[咨询师]-[订单]服务Api",tags = "[客户端]-[咨询师]-[订单]服务Api")
public class ConsultantOrderController {
    @Resource
    FrontOrderService orderService;
    @Resource
    WxPayService wxPayService;


    /**
     * 分页查询订单列表
     * @param businessId
     * @param userRole
     * @param adminId
     * @param queryQo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询订单列表")
    public CommonResult<Page<PageOrdersVo>> pageList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(value = "咨询师角色")@RequestHeader(value = "user-role",required = false) String userRole,
        @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId,
        @Validated @RequestBody FrontPageOrdersQo queryQo
    ){
        queryQo.setBusinessId(businessId);
        if(StringUtil.isSeeSelfData(userRole)){
            queryQo.setAdminId(adminId);
        }
        return CommonResult.success(orderService.pageListOrder(queryQo));
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

    /**
     * 设置订单为分期支付
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/set/payType")
    @ApiOperation(value = "设置订单为分期支付")
    public CommonResult<Void> setOrderPayType(
            @ApiParam(required = true, value = "订单id") @RequestParam("orderId") String orderId
    ){
        this.orderService.changePayType(orderId);
        return CommonResult.success();
    }

    /**
     * 设置分期订单本次支付金额
     * @param qo
     * @return
     */
    @PostMapping("set/thisPayment")
    @ApiOperation(value = "设置分期订单本次支付金额")
    public CommonResult<Void> setPayment(
            @Validated @RequestBody OrderChangePriceQo qo
    ){
        this.orderService.setThisPayment(qo);
        return CommonResult.success();
    }

    /**
     * 微信退款申请
     * @param refundsQo
     * @return
     */
    @PostMapping("/refunds")
    @ApiOperation(value = "微信退款申请")
    CommonResult<Void> refundsApply(
            @ApiParam(value = "咨询师id") @RequestHeader(value = "admin-id",required = false)String adminId,
            @Valid @RequestBody RefundsQo refundsQo
    ) throws Exception {
        if(StringUtil.isEmpty(adminId)){
            return CommonResult.failed("未找到咨询师信息");
        }
        refundsQo.setUid(adminId);
        wxPayService.splitRefundReq(refundsQo);
        return CommonResult.success();
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
        orderService.handDoneOrder(orderSn);
        return CommonResult.success();
    }


}
