package com.byx.pub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddOrUpdateFlyerInfoBo;
import com.byx.pub.bean.qo.PageFlyerInfoListQo;
import com.byx.pub.bean.vo.FlyerInfoVo;
import com.byx.pub.bean.vo.PageFlyerInfoListVo;
import com.byx.pub.service.FlyerInfoService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [管理后台]-[电子传单]管理Api
 * @Author Jump
 * @Date 2023/10/7 20:36
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/flyer")
@Api(value = "[管理后台]-[电子传单]管理Api",tags = "[管理后台]-[电子传单]管理Api")
public class FlyerInfoController {
    @Resource
    FlyerInfoService flyerInfoService;

    /**
     * 分页条件查询传单列表
     * @param businessId 商家id
     * @param qo
     * @return
     */
    @PostMapping("/info/page/list")
    @ApiOperation(value = "分页条件查询传单列表")
    public CommonResult<Page<PageFlyerInfoListVo>> pageListFlyer(
        @ApiParam(value = "商家id(请求头，前端不用传)") @RequestHeader(value = "business-id",required = false)String businessId,
        @ApiParam(required = true, value = "查询条件") @Validated @RequestBody PageFlyerInfoListQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(flyerInfoService.pageList(qo));
    }

    /**
     * 新增或修改传单
     * @param flyerInfoBo
     * @return
     */
    @PostMapping("/info/addOrUpdate")
    @ApiOperation(value = "新增或修改传单")
    public CommonResult<Void> addOrUpdateFlyer(
            @ApiParam(required = true, value = "请求体") @Validated @RequestBody AddOrUpdateFlyerInfoBo flyerInfoBo
    ){
        this.flyerInfoService.addOrUpdateFlyer(flyerInfoBo);
        return CommonResult.success();
    }

    /**
     * 获取传单详情
     * @param id 主键
     * @return
     */
    @GetMapping("/info/get/detail")
    @ApiOperation(value = "获取传单详情")
    public CommonResult<FlyerInfoVo> getFlyerDetail(
        @ApiParam(required = true, value = "主键") @RequestParam("id") String id
    ){
        return CommonResult.success(this.flyerInfoService.getFlyerInfoDetail(id));
    }

    /**
     * 删除传单
     * @param id 主键
     * @return
     */
    @GetMapping("/info/del")
    @ApiOperation(value = "删除传单")
    public CommonResult<Void> delFlyer(
            @ApiParam(required = true, value = "主键") @RequestParam("id") String id
    ){
        this.flyerInfoService.delFlyer(id);
        return CommonResult.success();
    }



















}
