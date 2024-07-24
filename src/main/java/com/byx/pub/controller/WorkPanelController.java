package com.byx.pub.controller;

import com.byx.pub.bean.qo.PageManageOrdersQo;
import com.byx.pub.bean.vo.WorkPanelVo;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.service.WorkPanelService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
/**
 * [管理后台]-[工作面板]管理Api
 * @Author Jump
 * @Date 2023/6/14 23:04
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/work/panel")
@Api(value = "[管理后台]-[工作面板]管理Api",tags = "[管理后台]-[工作面板]管理Api")
public class WorkPanelController {
    @Resource
    WorkPanelService workPanelService;


    /**
     * 数据面板
     * @param adminId
     * @param adminRole
     * @return
     */
    @GetMapping("/data")
    @ApiOperation(value = "数据面板")
    public CommonResult<WorkPanelVo> getData(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(value = "当前用户id")@RequestHeader(value = "admin-id",required = false) String adminId,
        @ApiParam( value = "用户角色") @RequestHeader(value = "admin-role",required = false)String adminRole
    ){
        PageManageOrdersQo qo = new PageManageOrdersQo();
        qo.setPageNum(1).setPageSize(5).setBusinessId(businessId);
        if(StringUtil.isSeeSelfData(adminRole)){
            qo.setAdminId(adminId);
        }
        return CommonResult.success(this.workPanelService.queryPanelData(qo));
    }



}
