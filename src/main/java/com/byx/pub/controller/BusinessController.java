package com.byx.pub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateBusinessQo;
import com.byx.pub.bean.qo.PageBusinessQo;
import com.byx.pub.bean.vo.PageBusinessVo;
import com.byx.pub.service.BusinessService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * [管理后台]-[商家服务]管理Api
 * @Author Jump
 * @Date 2023/7/14 22:42
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/business")
@Api(value = "[管理后台]-[商家服务]管理Api",tags = "[管理后台]-[商家服务]管理Api")
public class BusinessController {
    @Resource
    BusinessService businessService;

    /**
     * 新增或修改商家信息
     * @param qo
     * @return
     */
    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "新增或修改商家信息")
    public CommonResult<Void> addOrUpdate(
       @Validated @RequestBody AddOrUpdateBusinessQo qo
    ){
        this.businessService.addOrUpdateBusiness(qo);
        return CommonResult.success();
    }

    /**
     * 分页条件查询商家
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页条件查询商家")
    public CommonResult<Page<PageBusinessVo>> pageList(
            @Validated @RequestBody PageBusinessQo qo
    ){
        return CommonResult.success(this.businessService.pageList(qo));
    }





}
