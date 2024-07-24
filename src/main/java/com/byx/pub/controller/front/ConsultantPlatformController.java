package com.byx.pub.controller.front;

import com.byx.pub.bean.vo.ListAdminPlatformVo;
import com.byx.pub.bean.xhs.XhsPushProductResVo;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.service.PlatformService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * [客户端]-[咨询师]-[跨平台]小红书服务Api
 * @Author Jump
 * @Date 2023/12/26 22:02
 */
@RestController
@RequestMapping("/white/clouds/front/consultant/v1/platform")
@Api(value = "[客户端]-[咨询师]-[跨平台]小红书服务Api",tags = "[客户端]-[咨询师]-[跨平台]小红书服务Api")
public class ConsultantPlatformController {
    @Resource
    PlatformService platformService;

    /**
     * 推送小红书
     * @param adminId 咨询师id
     * @param userRole 咨询师角色
     * @param productId 产品id
     * @return
     */
    @GetMapping("/push/xhs")
    @ApiOperation(value = "推送小红书")
    public CommonResult<XhsPushProductResVo> pushXhs(
            @ApiParam(value = "咨询师id")@RequestHeader(value = "admin-id",required = false) String adminId,
            @ApiParam(value = "咨询师角色")@RequestHeader(value = "user-role",required = false) String userRole,
            @ApiParam(value = "产品id") @RequestParam("productId")String productId
    ){
        if(StringUtil.isPlatformAdmin(userRole)
                || RoleTypeEnum.FRONT_CUSTOM.getValue().equals(userRole)){
            return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"暂不支持推送商品的角色");
        }
        return CommonResult.success(platformService.pushXhsProduct(productId,adminId));
    }

    /**
     * 商家跨平台员工列表
     * @param businessId
     * @return
     */
    @GetMapping("/list/platform")
    @ApiOperation(value = "商家跨平台员工列表")
    public CommonResult<List<ListAdminPlatformVo>> listPlatform(
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId
    ){
        return CommonResult.success(platformService.adminPlatformList(businessId));
    }

    /**
     * 绑定小红书店铺id(错误码602)
     * @param adminId
     * @param xhsSellerId
     * @return
     */
    @GetMapping("/xhs/bind")
    @ApiOperation(value = "绑定小红书店铺id(错误码602提示：绑定成功，小红书店铺未授权，请前往授权)")
    public CommonResult<Void> bindXhsId(
            @ApiParam(required = true, value = "员工id") @RequestParam("adminId") String adminId,
            @ApiParam(required = true, value = "小红书店铺id") @RequestParam("xhsSellerId") String xhsSellerId
    ){
        platformService.bindXhsId(adminId,xhsSellerId);
        return CommonResult.success();
    }










}
