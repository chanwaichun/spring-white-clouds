package com.byx.pub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageAdminUserQo;
import com.byx.pub.bean.vo.PageBusinessUserVo;
import com.byx.pub.service.BusinessUserService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [管理后台]-[商家客户]管理Api
 * @author Jump
 * @date 2023/6/14 15:45:51
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/adminUser")
@Api(value = "[管理后台]-[商家客户]管理Api",tags = "[管理后台]-[商家客户]管理Api")
public class BusinessUserController {
    @Resource
    BusinessUserService businessUserService;


    /**
     * 分页查询列表
     * @param businessId
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询列表")
    public CommonResult<Page<PageBusinessUserVo>> pageList(
        @ApiParam(value = "商家id") @RequestHeader(value = "business-id",required = false)String businessId,
        @Validated @RequestBody PageAdminUserQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(this.businessUserService.pageList(qo));
    }


}
