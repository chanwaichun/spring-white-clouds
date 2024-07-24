package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.ShareFlyerBo;
import com.byx.pub.bean.bo.ShareFlyerClickBo;
import com.byx.pub.bean.qo.PageFlyerInfoListQo;
import com.byx.pub.bean.qo.PageFlyerPullListQo;
import com.byx.pub.bean.vo.FlyerInfoVo;
import com.byx.pub.bean.vo.FlyerShareVo;
import com.byx.pub.bean.vo.PageFlyerInfoListVo;
import com.byx.pub.bean.vo.PageFlyerPullListVo;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.entity.FlyerInfo;
import com.byx.pub.service.BusinessUserService;
import com.byx.pub.service.FlyerInfoService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/10/7 20:48
 */
@RestController
@RequestMapping("/white/clouds/front/v1/flyer")
@Api(value = "[客户端]-[电子传单]服务Api",tags = "[客户端]-[电子传单]服务Api")
public class FrontFlyerInfoController {
    @Resource
    FlyerInfoService flyerInfoService;
    @Resource
    BusinessUserService businessUserService;

    /**
     * 分页条件查询传单列表
     * @param businessId 商家id
     * @param qo 查询条件
     * @return
     */
    @PostMapping("/info/page/list")
    @ApiOperation(value = "分页条件查询传单列表")
    public CommonResult<Page<PageFlyerInfoListVo>> pageListFlyer(
            @ApiParam(value = "商家id(请求头)")@RequestHeader(value = "business-id",required = false) String businessId,
            @ApiParam(required = true, value = "查询条件") @Validated @RequestBody PageFlyerInfoListQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(flyerInfoService.pageList(qo));
    }

    /**
     * 分享传单
     * @param userId 用户id(请求头)
     * @param bo 分享传单请求体
     * @return
     */
    @PostMapping("/info/share")
    @ApiOperation(value = "分享传单")
    public CommonResult<FlyerShareVo> shareFlyer(
            @ApiParam(value = "用户id(请求头)")@RequestHeader(value = "user-id",required = false) String userId,
            @ApiParam(required = true, value = "分享传单请求体") @Validated @RequestBody ShareFlyerBo bo
    ){
        bo.setNowUid(userId);
        return CommonResult.success(flyerInfoService.shareFlyer(bo));
    }

    /**
     * 保存分享拉新记录
     * @param bo 请求体
     * @return
     */
    @PostMapping("/save/pull/record")
    @ApiOperation(value = "保存传单拉新记录")
    public CommonResult<Void> savePullRecord(
            @ApiParam(required = true, value = "请求体") @Validated @RequestBody ShareFlyerClickBo bo
    ){
        flyerInfoService.addClickRecord(bo);
        return CommonResult.success();
    }

    /**
     * 分页条件查询引流数据
     * @param qo 查询条件
     * @return
     */
    @PostMapping("/pull/page/list")
    @ApiOperation(value = "分页条件查询引流数据")
    public CommonResult<Page<PageFlyerPullListVo>> pageFlyerPullList(
            @ApiParam(required = true, value = "查询条件") @Validated @RequestBody PageFlyerPullListQo qo
    ){
        return CommonResult.success(this.flyerInfoService.pageFlyerPullList(qo));
    }

    /**
     * 获取传单详情
     * @param id 主键
     * @return
     */
    @GetMapping("/info/get/detail")
    @ApiOperation(value = "获取传单详情")
    public CommonResult<FlyerInfoVo> getFlyerDetail(
            @ApiParam(value = "用户id(请求头)")@RequestHeader(value = "user-id",required = false) String userId,
            @ApiParam(required = true, value = "主键") @RequestParam("id") String id
    ){
        FlyerInfo flyer = this.flyerInfoService.getFlyerById(id);
        if(Objects.isNull(flyer)){
            return CommonResult.failed("未找到电子传单信息");
        }
        //添加会员
        businessUserService.addSjMember("",flyer.getBusinessId(),userId,3,false);
        return CommonResult.success(this.flyerInfoService.getFlyerInfoDetail(id));
    }




}
