package com.byx.pub.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageListPayFlowQo;
import com.byx.pub.bean.vo.ConsultantIncomeVo;
import com.byx.pub.service.ManageFinanceService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import com.byx.pub.util.excel.CustomerExcelUtil;
import com.byx.pub.util.excel.ExcelData;
import com.byx.pub.util.excel.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * [管理后台]-[财务]管理Api
 * @Author Jump
 * @Date 2023/6/25 21:26
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/finance")
@Api(value = "[管理后台]-[财务]管理Api",tags = "[管理后台]-[财务]管理Api")
public class ManageFinanceController {
    @Resource
    ManageFinanceService financeService;

    /**
     * 分页查询订单支付流水
     * @param adminRole
     * @param adminId
     * @return
     */
    @PostMapping("/page/pay/flow")
    @ApiOperation(("分页查询订单支付流水"))
    public CommonResult<Page<ConsultantIncomeVo>> pagePayFlow(
        @ApiParam(value = "当前用户角色")@RequestHeader(value = "admin-role",required = false) String adminRole,
        @ApiParam(value = "当前用户id")@RequestHeader(value = "admin-id",required = false) String adminId,
        @Validated @RequestBody PageListPayFlowQo qo
    ){
        if(StringUtil.isBusiness(adminRole)){
            qo.setAdminId(adminId);
        }
        return CommonResult.success(financeService.pagePayFlowList(qo));
    }

    /**
     * 导出订单支付流水
     * @param adminRole
     * @param adminId
     * @param qo
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping({"/export/pay/flow"})
    @ApiOperation("导出订单支付流水")
    public void exportPayFlowList(
            @ApiParam(value = "当前用户角色")@RequestHeader(value = "admin-role",required = false) String adminRole,
            @ApiParam(value = "当前用户id")@RequestHeader(value = "admin-id",required = false) String adminId,
            @Validated @RequestBody PageListPayFlowQo qo,
            HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        if(StringUtil.isBusiness(adminRole)){
            qo.setAdminId(adminId);
        }
        qo.setPageNum(1).setPageSize(1000);
        List<ConsultantIncomeVo> list = this.financeService.pagePayFlowList(qo).getRecords();
        List<String> rowTitle = CollUtil.newArrayList("支付订单号","订单号","用户昵称","商家名称","支付渠道","支付金额","支付时间");
        List<List<Object>> rowList = CustomerExcelUtil.loadValueRowPayFlow(list);
        ExcelData excelData = new ExcelData();
        excelData.setName("orderPayFlowList");
        excelData.setTitles(rowTitle);
        excelData.setRows(rowList);
        ExcelUtils.exportExcel(request,response,excelData.getName() + ".xlsx",excelData);
    }



}
