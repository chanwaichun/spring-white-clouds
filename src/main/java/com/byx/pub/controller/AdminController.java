package com.byx.pub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.crop.OauthDto;
import com.byx.pub.bean.qo.AddOrUpdateAdminQo;
import com.byx.pub.bean.qo.PageListAdminQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.service.*;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.SignatureUtil;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * [管理后台]-[账户]管理Api
 * @author Jump
 * @date 2023/5/7 14:50:28
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/admin")
@Api(value = "[管理后台]-[账户]管理Api",tags = "[管理后台]-[账户]管理Api")
public class AdminController {
    @Resource
    AdminService adminService;
    @Resource
    WeChatApiService weChatApiService;
    @Value("${WeChat.web.redirectPage}")
    private String redirectPage;
    @Resource
    BusinessService businessService;
    @Resource
    CropWeChatService cropWeChatService;

    /**
     * 获取微信登录二维码
     * @return
     */
    @GetMapping("/get/login/crop/qrCode")
    @ApiOperation(value = "获取企业微信登录二维码")
    public CommonResult<String> getLoginCropQrCode(){
        return CommonResult.success(cropWeChatService.genCropQr());
    }

    /**
     * 企业微信授权回调
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation("企业微信授权回调")
    @GetMapping("/crop/callback")
    @ResponseBody
    public CommonResult<Void> cropAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        //String token = this.adminService.cropAuth(code, state);
        //token = token + LocalDateTime.now().toString();
        //response.sendRedirect("https://wwww.baidu.com" +"?token="+token);
        /*//未授权 重定向到登录页(未授权)
        if(StringUtils.isEmpty(token)){
            response.sendRedirect(redirectPage);
        }else{
            response.sendRedirect(redirectPage +"?token="+token);
        }*/
        return CommonResult.success();
    }


    /**
     * 帐号密码登录
     * @param mobile 手机(帐号)
     * @param password 密码
     * @return
     */
    @GetMapping("/account/login")
    @ApiOperation(value = "帐号密码登录")
    public CommonResult<String> accountLogin(
            @ApiParam(required = true, value = "手机(帐号)") @RequestParam("mobile")String mobile,
            @ApiParam(required = true, value = "密码") @RequestParam("password")String password
    ){
        return CommonResult.success(adminService.accountLogin(mobile,password));
    }

    /**
     * 获取微信登录二维码
     * @return
     */
    @GetMapping("/get/login/qrCode")
    @ApiOperation(value = "获取微信登录二维码")
    public CommonResult<String> getLoginQrCode(){
        return CommonResult.success(weChatApiService.genQrConnect());
    }

    /**
     * 扫码登录微信回调接口
     * @param code
     * @param state
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/WeChat/callback")
    @ApiOperation(value = "扫码登录微信回调接口")
    public CommonResult<Void> callback(String code, String state, HttpServletResponse response) throws IOException {
        String token = this.adminService.callBack(code, state);
        //未授权 重定向到登录页(未授权)
        if(StringUtils.isEmpty(token)){
            response.sendRedirect(redirectPage);
        //有授权 重定向到首页
        }else{
            response.sendRedirect(redirectPage +"?token="+token);
        }
        return CommonResult.success();
    }

    /**
     * 获取登录用户信息
     * @param adminId
     * @param adminRole
     * @return
     */
    @GetMapping("/get/info")
    @ApiOperation("获取登录用户信息")
    public CommonResult<LoginAdminVo> getAdminInfo(
        @ApiParam(value = "用户id") @RequestHeader(value = "admin-id",required = false)String adminId,
        @ApiParam(value = "用户角色") @RequestHeader(value = "admin-role",required = false)String adminRole
    ){
        return CommonResult.success(this.adminService.getAdminInfo(adminId,adminRole));
    }

    /**
     * 绑定账户
     * @param xToken
     * @param tempAdminId
     * @param mobile
     * @param password
     * @return
     */
    @GetMapping("/band")
    @ApiOperation(value = "绑定账户")
    public CommonResult<LoginAdminVo> bandAdmin(
            @ApiParam(required = true, value = "用户token") @RequestHeader("x-key")String xToken,
            @ApiParam(value = "用户id") @RequestHeader(value = "admin-id",required = false)String tempAdminId,
            @ApiParam(required = true, value = "绑定手机") @RequestParam("mobile")String mobile,
            @ApiParam(required = true, value = "绑定密码") @RequestParam("password")String password
    ){
        return CommonResult.success(this.adminService.bandAdmin(tempAdminId,mobile,password,xToken));
    }

    /**
     * 通过手机号获取用户信息
     * @param mobile
     * @return
     */
    @GetMapping("/get/info/mobile")
    @ApiOperation(value = "通过手机号获取用户信息")
    public CommonResult<LoginAdminVo> getInfoByMobile(
            @ApiParam(required = true, value = "手机号") @RequestParam("mobile")String mobile
    ){
        return CommonResult.success(this.adminService.getLoginAdminInfoByMobile(mobile));
    }


    /**
     * 新增或修改账户
     * @param adminRole 用户角色
     * @param qo 请求头
     * @return
     */
    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "新增账户")
    public CommonResult<Void> addOrUpdateAdmin(
            @ApiParam(value = "用户角色") @RequestHeader(value = "admin-role",required = false)String adminRole,
            @ApiParam(value = "用户商家") @RequestHeader(value = "business-id",required = false)String businessId,
            @RequestBody @Validated AddOrUpdateAdminQo qo
    ){
        //TODO 创建成员 需要同步 到 对应 的 企业微信成员
        //如果是平台人员或者商家管理员才可以操作
        if(!StringUtil.isAdminServiceRole(adminRole)){
            return CommonResult.failed("暂未对您开放该权限");
        }
        //如果是商家管理员 1.只能添加商家角色(不能是平台角色) 2.只能添加自己的帐号
        if(RoleTypeEnum.SJ_MANEGE.getValue().equals(adminRole)){
            //1.只能添加商家角色(不能是平台角色)
            if(RoleTypeEnum.BYX_MANEGE.getValue().equals(adminRole)
                    || RoleTypeEnum.BYX_OPERATE.getValue().equals(adminRole)){
                return CommonResult.failed("商家管不能提交平台角色");
            }
            // 2.只能添加自己的帐号
            if(!businessId.equals(qo.getBusinessId())){
                return CommonResult.failed("只能操作本团队的帐号");
            }
        }
        this.adminService.addOrUpdateAdmin(qo);
        return CommonResult.success();
    }

    /**
     * 删除账户
     * @param adminId
     * @return
     */
    @GetMapping("/del")
    @ApiOperation(value = "删除账户")
    public CommonResult delAdmin(@ApiParam(required = true, value = "用户id") @RequestParam("adminId")String adminId){
        this.adminService.delAdmin(adminId);
        return CommonResult.success();
    }

    /**
     * 分页查询账户列表
     * @param businessId
     * @param adminRole
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询账户列表")
    public CommonResult<Page<AdminPageListVo>> pageList(
            @ApiParam(value = "用户商家") @RequestHeader(value = "business-id",required = false)String businessId,
            @ApiParam(value = "用户角色") @RequestHeader(value = "admin-role",required = false)String adminRole,
            @RequestBody @Validated PageListAdminQo qo
    ){
        //如果是平台人员或者商家管理员才可以操作
        if(!StringUtil.isAdminServiceRole(adminRole)){
            return CommonResult.failed("暂未对您开放该权限");
        }
        qo.setBusinessId(businessId);
        return CommonResult.success(this.adminService.pageList(qo));
    }

    /**
     * 下拉选择咨询师列表
     * @param adminId 用户id
     * @param adminRole 用户角色
     * @return
     */
    @GetMapping("/select/list")
    @ApiOperation(value = "下拉选择咨询师列表")
    public CommonResult<List<SelectAdminListVo>> selectList(
        @ApiParam(value = "用户id") @RequestHeader(value = "admin-id",required = false)String adminId,
        @ApiParam(value = "用户角色") @RequestHeader(value = "admin-role",required = false)String adminRole
    ){
        return CommonResult.success(this.adminService.getAdminListByRole(adminRole,adminId));
    }

    /**
     * 下拉选择商家列表
     * @param businessId 用户商家
     * @param adminRole 用户角色
     * @return
     */
    @GetMapping("/select/business/list")
    @ApiOperation(value = "下拉选择商家列表")
    public CommonResult<List<SelectBusinessListVo>> selectBusinessList(
            @ApiParam(value = "用户商家") @RequestHeader(value = "business-id",required = false)String businessId,
            @ApiParam(value = "用户角色") @RequestHeader(value = "admin-role",required = false)String adminRole
    ){
        return CommonResult.success(this.businessService.getSjListByRole(adminRole,businessId));
    }


}
