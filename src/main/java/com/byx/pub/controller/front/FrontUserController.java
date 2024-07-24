package com.byx.pub.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.byx.pub.bean.qo.UpdateUserInfoQo;
import com.byx.pub.bean.vo.LoginHeadBean;
import com.byx.pub.bean.vo.UserLoginVo;
import com.byx.pub.enums.ClientTypeEnum;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.plus.entity.User;
import com.byx.pub.service.FrontUserService;
import com.byx.pub.service.RedisService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [客户端]-[用户]-[授权登录]服务Api
 * @author Jump
 * @date 2023/5/19 17:52:07
 */
@Slf4j
@RestController
@RequestMapping("/white/clouds/front/user/v1/user")
@Api(value = "[客户端]-[用户]-[授权登录]服务Api",tags = "[客户端]-[用户]-[授权登录]服务Api")
public class FrontUserController {

    @Resource
    FrontUserService userService;
    @Resource
    RedisService redisService;

    /**
     * 用户登录
     * @param code
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "用户登录")
    public CommonResult<String> userLogin(
            @ApiParam(required = true, value = "微信code") @RequestParam("code") String code
    ){
        User user = this.userService.miniAppLogin(code);
        LoginHeadBean loginHeadBean = new LoginHeadBean()
                .setUserId(user.getId())
                .setUserRole(user.getRoleId().toString())
                .setClientType(ClientTypeEnum.BYX_FRONT.getValue());
        //设置商家信息
        if(StringUtil.isBusiness(user.getRoleId().toString())){
            loginHeadBean.setIsBusiness(Boolean.TRUE)
                    .setBusinessId(user.getBusinessId())
                    .setAdminId(user.getAdminId());
        }else{
            loginHeadBean.setIsBusiness(Boolean.FALSE)
                    .setBusinessId(SystemFinalCode.ZERO_STR)
                    .setAdminId("");
        }
        return CommonResult.success(redisService.setLoginToken(loginHeadBean));
    }

    /**
     * 获取用户信息(可刷新在线时长)
     * @param userId
     * @return
     */
    @GetMapping("/get/info")
    @ApiOperation(value = "获取用户信息(可刷新在线时长)")
    public CommonResult<UserLoginVo> getUserInfo(
        @ApiParam(required = true, value = "用户token") @RequestHeader("x-key")String xToken,
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId
    ){
        UserLoginVo userInfo = userService.getUserInfo(userId);
        this.redisService.reSetToken(xToken,userInfo);
        return CommonResult.success(userInfo);
    }

    /**
     * 微信手机号码授权
     * @param userId
     * @param encryptedData
     * @param iv
     * @return
     */
    @GetMapping("/auth/mobile")
    @ApiOperation(value = "微信手机号码授权")
    public CommonResult<UserLoginVo> authWeChatMobile(
        @ApiParam(value = "用户token") @RequestHeader(value = "x-key",required = false)String xToken,
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
        @ApiParam(required = true, value = "encryptedData") @RequestParam("encryptedData") String encryptedData,
        @ApiParam(required = true, value = "iv") @RequestParam("iv") String iv
    ){
        UserLoginVo userLoginVo = this.userService.authWxMobile(userId, iv, encryptedData);
        this.redisService.reSetToken(xToken,userLoginVo);
        return CommonResult.success(userLoginVo);
    }

    /**
     * 更新用户头像等信息
     * @param userId
     * @param qo
     * @return
     */
    @PostMapping("/update/info")
    @ApiOperation(value = "更新用户头像等信息")
    public CommonResult<UserLoginVo> updateUserInfo(
            @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
            @Validated @RequestBody UpdateUserInfoQo qo
    ){
        return CommonResult.success(this.userService.updateUserInfo(userId,qo));
    }

    /**
     * 设置用户手机号
     * @param userId
     * @param mobile
     * @return

    @GetMapping("/set/mobile")
    @ApiOperation(value = "设置用户手机号")
    public CommonResult<UserLoginVo> setUserMobile(
        @ApiParam(required = true, value = "用户id") @RequestHeader("user-id") String userId,
        @ApiParam(required = true, value = "手机号") @RequestParam("mobile") String mobile
    ){
        return CommonResult.success(this.userService.setUserMobile(userId,mobile));
    }*/
}
