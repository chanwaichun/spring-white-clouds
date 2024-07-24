package com.byx.pub.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.ImportChangeDetailExcelBo;
import com.byx.pub.bean.bo.SaveSettlementRulesBo;
import com.byx.pub.bean.qo.PageMainSettlementQo;
import com.byx.pub.bean.qo.PagePersonSettlementQo;
import com.byx.pub.bean.qo.PageSettlementDetailQo;
import com.byx.pub.bean.qo.PageSettlementRulesQo;
import com.byx.pub.bean.vo.PageMainSettlementVo;
import com.byx.pub.bean.vo.PagePersonSettlementVo;
import com.byx.pub.bean.vo.PageSettlementDetailListVo;
import com.byx.pub.bean.vo.PageSettlementRulesVo;
import com.byx.pub.service.SettlementRulesService;
import com.byx.pub.service.SettlementService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * [管理后台]-[商家]-[结算]管理Api
 * @author Jump
 * @date 2023/8/18 16:29:41
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/settlement")
@Api(value = "[管理后台]-[商家]-[结算]管理Api",tags = "[管理后台]-[商家]-[结算]管理Api")
public class SettlementRulesController {
    @Resource
    SettlementRulesService settlementRulesService;
    @Resource
    SettlementService settlementService;

    /**
     * 结算账单
     * @param id 主键
     * @return
     */
    @GetMapping("/settlement/bill")
    @ApiOperation(value = "结算账单(管理员结算)")
    public CommonResult<Void> settlementBill(
            @ApiParam(required = true, value = "主键") @RequestParam("id") String id
    ){
        settlementService.confirmSettlementBill(id);
        return CommonResult.success();
    }

    /**
     * 导入修改计算金额
     * @param file 导入文件
     */
    @PostMapping("/import/change/detail")
    @ApiOperation(value = "导入修改计算金额")
    public CommonResult<String> importCustomerDaily(
            @RequestParam MultipartFile file
    ) throws IOException {
        List<ImportChangeDetailExcelBo> list = ExcelUtils.excelReadSync(file.getInputStream(),ImportChangeDetailExcelBo.class);
        Integer successNum = this.settlementService.importChangeDetailAmount(list);
        return CommonResult.success("成功处理："+successNum+"条");
    }

    /**
     * 修改结算金额
     * @param id
     * @param amount
     * @return
     */
    @GetMapping("/change/settlement/amount")
    @ApiOperation(value = "修改结算金额")
    public CommonResult<Void> changeSettlementAmount(
            @ApiParam(required = true, value = "主键") @RequestParam("id") String id,
            @ApiParam(required = true, value = "新金额") @RequestParam("amount") BigDecimal amount
    ){
        if(BigDecimal.ZERO.compareTo(amount) > 0 ){
            return CommonResult.failed("调整金额不能小于0");
        }
        settlementService.changeDetailAmount(id,amount);
        return CommonResult.success();
    }

    /**
     * 分页条件查询结算单列表
     * @param qo
     * @return
     */
    @PostMapping("/page/main/list")
    @ApiOperation(value = "分页条件查询结算单列表")
    public CommonResult<Page<PageMainSettlementVo>> pageMainList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody PageMainSettlementQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(settlementService.pageMainSettlementList(qo));
    }


    /**
     * 分页条件查询个人结算明细
     * @param qo
     * @return
     */
    @PostMapping("/page/person/detail")
    @ApiOperation(value = "分页条件查询个人结算明细")
    public CommonResult<Page<PagePersonSettlementVo>> pagePersonDetailList(
            @ApiParam(value = "用户角色")@RequestHeader(value = "user-role",required = false) String userRole,
            @ApiParam(value = "咨询师")@RequestHeader(value = "admin-id",required = false) String adminId,
            @Validated @RequestBody PagePersonSettlementQo qo
    ){
        //如果是 平台角色、商家管理员、商家导师、商家老师
        if(!StringUtil.isSettlementRole(userRole)){
            qo.setAdminId(adminId);
        }
        return CommonResult.success(settlementService.pagePersonSettlementList(qo));
    }

    /**
     * 分页条件查询结算明细
     * @param qo
     * @return
     */
    @PostMapping("/page/detail/list")
    @ApiOperation(value = "分页条件查询结算明细")
    public CommonResult<Page<PageSettlementDetailListVo>> pageDetailList(
        @ApiParam(value = "用户角色")@RequestHeader(value = "user-role",required = false) String userRole,
        @ApiParam(value = "咨询师")@RequestHeader(value = "admin-id",required = false) String adminId,
        @Validated @RequestBody PageSettlementDetailQo qo
    ){
        //如果是 平台角色、商家管理员、商家导师、商家老师
        if(!StringUtil.isSettlementRole(userRole)){
            qo.setAdminId(adminId);
        }
        return CommonResult.success(settlementService.pageDetailList(qo));
    }

    /**
     * 导出结算明细
     * @param qo
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping({"/export/detail/list"})
    @ApiOperation("导出结算明细")
    public void exportDetailList(
        @ApiParam(value = "用户角色")@RequestHeader(value = "user-role",required = false) String userRole,
        @ApiParam(value = "咨询师")@RequestHeader(value = "admin-id",required = false) String adminId,
        @Validated @RequestBody PageSettlementDetailQo qo,
        HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        //如果是 平台角色、商家管理员、商家导师、商家老师
        if(!StringUtil.isSettlementRole(userRole)){
            qo.setAdminId(adminId);
        }
        qo.setPageNum(1).setPageSize(1000);
        List<PageSettlementDetailListVo> list = this.settlementService.pageDetailList(qo).getRecords();
        List<String> rowTitle = CollUtil.newArrayList("账单ID","订单号","商家名称","结算人","类型","结算金额","出单时间");
        List<List<Object>> rowList = CustomerExcelUtil.loadValueRowSettlementDetail(list);
        ExcelData excelData = new ExcelData();
        excelData.setName("SettlementDetailList");
        excelData.setTitles(rowTitle);
        excelData.setRows(rowList);
        ExcelUtils.exportExcel(request,response,excelData.getName() + ".xlsx",excelData);
    }


    /**
     * 分页条件查询规则
     * @param businessId 商家id(请求头)
     * @param qo 查询体
     * @return
     */
    @PostMapping("/rules/page/list")
    @ApiOperation(value = "分页条件查询规则")
    public CommonResult<Page<PageSettlementRulesVo>> pageRuleList(
        @ApiParam(value = "商家id(请求头)")@RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody PageSettlementRulesQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(settlementRulesService.pageList(qo));
    }

    /**
     * 新增或修改规则
     * @param businessId
     * @param rulesBo
     * @return
     */
    @PostMapping("/rules/save")
    @ApiOperation(value = "新增或修改规则")
    public CommonResult<Void> saveRules(
        @ApiParam(value = "商家id(请求头)")@RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody SaveSettlementRulesBo rulesBo
    ){
        rulesBo.setBusinessId(businessId);
        settlementRulesService.saveSjSettlementRules(rulesBo);
        return CommonResult.success();
    }

    /**
     * 获取规则详情
     * @param id 规则id
     * @return
     */
    @GetMapping("/rules/detail")
    @ApiOperation(value = "获取规则详情")
    public CommonResult<SaveSettlementRulesBo> getDetail(
            @ApiParam(required = true, value = "规则id") @RequestParam("id") String id
    ){
        return CommonResult.success(settlementRulesService.getRulesDetail(id));
    }






}
