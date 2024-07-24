package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddOrUpdateMemberGroupBo;
import com.byx.pub.bean.bo.AddWhitelistMemberBo;
import com.byx.pub.bean.qo.PageAdminUserQo;
import com.byx.pub.bean.qo.PageListGroupMemberQo;
import com.byx.pub.bean.qo.PageListUserPortraitQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.plus.entity.MemberGroup;
import com.byx.pub.plus.entity.MemberGroupBlacklist;
import com.byx.pub.service.BusinessUserService;
import com.byx.pub.service.CommonAsyncService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * [客户端]-[咨询师]-[商家客户]服务Api
 * @author Jump
 * @date 2023/6/14 15:52:34
 */
@RestController
@RequestMapping("/white/clouds/front/consultant/v1/adminUser")
@Api(value = "[客户端]-[咨询师]-[商家客户]服务Api",tags = "[客户端]-[咨询师]-[商家客户]服务Api")
public class ConsultantUserController {
    @Resource
    BusinessUserService businessUserService;
    @Resource
    CommonAsyncService asyncService;

    /**
     * 分页查询列表
     * @param userRole
     * @param businessId
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询商家会员列表")
    public CommonResult<Page<PageBusinessUserVo>> pageList(
        @ApiParam(value = "用户角色(请求头)") @RequestHeader(value = "user-role",required = false) String userRole,
        @ApiParam(value = "商家id(请求头)") @RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody PageAdminUserQo qo
    ){
        if(RoleTypeEnum.FRONT_CUSTOM.getValue().equals(userRole)){
            return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"暂无权限查看");
        }
        qo.setBusinessId(businessId);
        return CommonResult.success(this.businessUserService.pageList(qo));
    }

    /**
     * 客户统计
     * @param businessId 商家id(请求头)
     * @return
     */
    @GetMapping("/count/num")
    @ApiOperation(value = "客户统计")
    public CommonResult<BusinessUserCountVo> countNum(
        @ApiParam(value = "商家id(请求头)") @RequestHeader(value = "business-id",required = false) String businessId
    ){
        return CommonResult.success(this.businessUserService.sjUserCount(businessId));
    }

    /**
     * 商家会员组列表
     * @param businessId
     * @return
     */
    @GetMapping("/member/group/list")
    @ApiOperation(value = "商家会员组列表")
    public CommonResult<List<MemberGroup>> memberGroupList(
        @ApiParam(value = "商家id(请求头)") @RequestHeader(value = "business-id",required = false) String businessId
    ){
        return CommonResult.success(this.businessUserService.listMemberGroup(businessId));
    }

    /**
     * 会员组详情
     * @param id 分组主键
     * @return
     */
    @GetMapping("/member/group/detail")
    @ApiOperation(value = "会员组详情")
    public CommonResult<MemberGroupVo> memberGroupDetail(
            @ApiParam(required = true, value = "分组主键") @RequestParam("id") String id
    ){
        return CommonResult.success(this.businessUserService.getGroupDetail(id));
    }

    /**
     * 删除会员组
     * @param id 分组主键
     * @return
     */
    @GetMapping("/member/group/del")
    @ApiOperation(value = "删除会员组")
    public CommonResult<Void> memberGroupDel(
            @ApiParam(required = true, value = "分组主键") @RequestParam("id") String id
    ){
        this.businessUserService.delGroup(id);
        return CommonResult.success();
    }

    /**
     * 新增或修改会员分组
     * @param bo
     * @return
     */
    @PostMapping("/member/group/addOrUpdate")
    @ApiOperation(value = "新增或修改会员分组")
    public CommonResult<Void> memberGroupDelAddOrUpdate(
        @ApiParam(value = "商家id(请求头)") @RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody AddOrUpdateMemberGroupBo bo
    ){
        bo.setBusinessId(businessId);
        this.businessUserService.addOrUpdateMemberGroup(bo);
        return CommonResult.success();
    }

    /**
     * 分页查询分组会员列表
     * @param qo 查询条件
     * @return
     */
    @PostMapping("/page/list/group/member")
    @ApiOperation(value = "分页查询分组会员列表")
    public CommonResult<Page<PageListGroupMemberVo> > pageListGroupMember(
            @Validated @RequestBody PageListGroupMemberQo qo
    ){
        return CommonResult.success(this.businessUserService.pageListGroupMember(qo));
    }

    /**
     * 分组添加白名单用户
     * @param boList
     * @return
     */
    @PostMapping("/add/whitelist/member")
    @ApiOperation(value = "分组添加白名单用户")
    public CommonResult<Void> addWhitelistMember(
        @Validated @RequestBody List<AddWhitelistMemberBo> boList
    ){
        this.businessUserService.addGroupMemberBy(boList);
        return CommonResult.success();
    }

    /**
     * 分组添加黑名单用户
     * @param bo
     * @return
     */
    @PostMapping("/add/blacklist/member")
    @ApiOperation(value = "分组添加黑名单用户")
    public CommonResult<Void> addBlacklistMember(
            @Validated @RequestBody AddWhitelistMemberBo bo
    ){
        this.businessUserService.addBlackListMember(bo);
        return CommonResult.success();
    }

    /**
     * 分组黑名单列表
     * @param groupId 分组主键
     * @return
     */
    @GetMapping("/group/blacklist")
    @ApiOperation(value = "分组黑名单列表")
    public CommonResult<List<MemberGroupBlacklist>> groupBlacklist(
            @ApiParam(required = true, value = "分组主键") @RequestParam("groupId") String groupId
    ){
        return CommonResult.success(this.businessUserService.listBlackList(groupId));
    }

    /**
     * 分组黑名单列表
     * @param id
     * @return
     */
    @GetMapping("/group/blacklist/del")
    @ApiOperation(value = "分组黑名单列表")
    public CommonResult<Void> groupBlacklistDel(
            @ApiParam(required = true, value = "黑名单主键") @RequestParam("id") String id
    ){
        this.businessUserService.delBlackListMember(id);
        return CommonResult.success();
    }

    /**
     * 分页查询用户画像信息分页查询用户画像信息
     * @param qo
     * @return
     */
    @PostMapping("/page/user/portrait")
    @ApiOperation(value = "分页查询用户画像信息")
    public CommonResult<Page<BusinessUserPortraitVo>> pageListUserPortrait(
            @ApiParam(value = "商家id(请求头)") @RequestHeader(value = "business-id",required = false) String businessId,
            @Validated @RequestBody PageListUserPortraitQo qo
    ){
        qo.setBid(businessId);
        return CommonResult.success(this.businessUserService.pageListUserPortrait(qo));
    }


    @GetMapping("/run/group")
    @ApiOperation(value = "给分组跑用户")
    public CommonResult<Void> runGroup(
            @ApiParam(required = true, value = "分组id") @RequestParam("groupId") String groupId
    ){
        this.asyncService.runningGroupMember(groupId);
        return CommonResult.success();
    }

}
