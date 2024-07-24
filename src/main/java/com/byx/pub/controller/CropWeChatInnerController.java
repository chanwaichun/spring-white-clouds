package com.byx.pub.controller;

import com.byx.pub.service.CropInnerWeChatService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * [管理后台]-[内部]-[企业微信]管理Api
 * @Author Jump
 * @Date 2023/8/3 21:17
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/crop/inner/wechat")
@Api(value = "[管理后台]-[内部]-[企业微信]管理Api",tags = "[管理后台]-[内部]-[企业微信]管理Api")
public class CropWeChatInnerController {
    @Resource
    CropInnerWeChatService innerWeChatService;

    /**
     * 获取内部企微登录二维码
     * @return
     */
    @GetMapping("/login/qrCode")
    @ApiOperation(value = "获取内部企微登录二维码")
    public CommonResult<String> getLoginCropQrCode(
        @ApiParam(required = true, value = "商家id") @RequestParam(value = "businessId",required = false) String businessId
    ){
        return CommonResult.success(innerWeChatService.genInnerCropQr(businessId));
    }

    @ApiOperation("授权回调")
    @GetMapping("/crop/callback")
    @ResponseBody
    public CommonResult<Void> cropInnerAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String token = this.innerWeChatService.cropCallbackUrl(state,code);
        response.sendRedirect("https://wwww.baidu.com" +"?token="+token);
        /*//未授权 重定向到登录页(未授权)
        if(StringUtils.isEmpty(token)){
            response.sendRedirect(redirectPage);
        }else{
            response.sendRedirect(redirectPage +"?token="+token);
        }*/
        return CommonResult.success();
    }












}
