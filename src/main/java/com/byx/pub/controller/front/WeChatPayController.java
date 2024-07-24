package com.byx.pub.controller.front;

import com.byx.pub.bean.qo.JsapiPayQo;
import com.byx.pub.bean.qo.RefundsQo;
import com.byx.pub.bean.vo.WxJspaiPayVo;
import com.byx.pub.service.WxPayService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * [客户端]-[微信支付、退款]服务Api
 * @author Jump
 * @date 2023/5/26 16:16:50
 */
@RestController
@RequestMapping("/white/clouds/front/user/v1/wx/pay")
@Api(value = "[客户端]-[微信支付、退款]服务Api",tags = "[客户端]-[微信支付、退款]服务Api")
public class WeChatPayController {
    @Resource
    WxPayService wxPayService;


    /**
     * jsapi下单
     * @param userId 请求头用户id
     * @param jsapiPayQo 请求体
     * @return
     * @throws Exception
     */
    @PostMapping("/jaspi")
    @ApiOperation(value = "jsapi下单")
    public CommonResult<WxJspaiPayVo> jsapi(
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
        @Valid @RequestBody JsapiPayQo jsapiPayQo
    ) throws Exception {
        jsapiPayQo.setUserId(userId);
        return wxPayService.jsApiPay(jsapiPayQo);
    }

    /**
     * 微信支付结果查询
     * @param orderSn 订单号
     * @return
     * @throws Exception
     */
    @GetMapping("/query")
    @ApiOperation(value = "微信支付结果查询")
    public CommonResult<Boolean> query(
        @ApiParam(required = true, value = "订单号") @RequestParam("orderSn") String orderSn
    ) throws Exception {
        return CommonResult.success(this.wxPayService.queryJsApiPay(orderSn));
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

}
