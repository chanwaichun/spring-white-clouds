package com.byx.pub.controller.front;

import com.byx.pub.plus.entity.SmsgAuthRecord;
import com.byx.pub.service.WeChatSubscribeMsgService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * "[客户端]-[微信消息订阅]服务Api"
 * @Author Jump
 * @Date 2023/9/2 17:51
 */
@RestController
@RequestMapping("/white/clouds/front/consultant/v1/wechat/msg")
@Api(value = "[客户端]-[微信消息订阅]服务Api",tags = "[客户端]-[微信消息订阅]服务Api")
public class WeChatSubscribeMsgController {
    @Resource
    WeChatSubscribeMsgService msgService;


    /**
     * 新增或修改授权模板信息
     * @param adminId
     * @param tmplId
     * @param alwaysStatus
     * @return
     */
    @GetMapping("/addOrUpdate/auth/templ")
    @ApiOperation(value = "新增或修改授权模板信息")
    public CommonResult<Void> addOrUpdateAuth(
        @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId,
        @ApiParam(value = "模板id",required = true) @RequestParam("tmplId") String tmplId,
        @ApiParam(value = "总是状态(true：是，false：否)",required = true) @RequestParam("alwaysStatus") Boolean alwaysStatus
    ){
        this.msgService.addOrUpdate(adminId,tmplId,alwaysStatus);
        return CommonResult.success();
    }

    /**
     * 获取授权模板信息
     * @param adminId
     * @param tmplId
     * @return
     */
    @GetMapping("/get/auth/templ")
    @ApiOperation(value = "获取授权模板信息")
    public CommonResult<SmsgAuthRecord> getAuthTeml(
            @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId,
            @ApiParam(value = "模板id",required = true) @RequestParam("tmplId") String tmplId
    ){
        return CommonResult.success(this.msgService.getRecordByAdminId(adminId,tmplId));
    }

    /**
     * 测试消息
     * @param orderSn
     * @return
     */
    @GetMapping("/test/msg")
    @ApiOperation(value = "测试消息")
    public CommonResult<Void> testMsg(
            @ApiParam(value = "订单号",required = true) @RequestParam("orderSn") String orderSn
    ){
        this.msgService.sendUserPayOrderMsg(orderSn);
        return CommonResult.success();
    }


}
