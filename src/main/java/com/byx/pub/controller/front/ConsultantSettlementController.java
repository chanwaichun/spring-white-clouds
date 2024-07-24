package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.BatchChangeDetailBo;
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
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * [客户端]-[咨询师]-[结算]服务Api
 * @Author Jump
 * @Date 2023/9/21 20:23
 */
@RestController
@RequestMapping("/white/clouds/front/consultant/v1/settlement")
@Api(value = "[客户端]-[咨询师]-[结算]服务Api",tags = "[客户端]-[咨询师]-[结算]服务Api")
public class ConsultantSettlementController {
    @Resource
    SettlementRulesService settlementRulesService;
    @Resource
    SettlementService settlementService;


    /**
     * 结算账单(商家管理员、导师、老师可见)
     * @param id
     * @return
     */
    @GetMapping("/settlement/bill")
    @ApiOperation(value = "结算账单(商家管理员、导师、老师可见)")
    public CommonResult<Void> settlementBill(
        @ApiParam(required = true, value = "主键") @RequestParam("id") String id
    ){
        settlementService.confirmSettlementBill(id);
        return CommonResult.success();
    }

    /**
     * 批量修改结算金额
     * @param boList
     * @return
     */
    @PostMapping("/batch/change/amount")
    @ApiOperation("批量修改结算金额(商家管理员、导师、老师可见)")
    public CommonResult<String> batchChangeAmount(
        @ApiParam(value = "对象集合")@RequestBody List<BatchChangeDetailBo> boList
    ){
        if(CollectionUtils.isEmpty(boList)){
            return CommonResult.success("成功处理：0 条");
        }
        Integer successNum = this.settlementService.importChangeDetailAmount(CopyUtil.copyList(boList, ImportChangeDetailExcelBo.class));
        return CommonResult.success("成功处理："+successNum+" 条");
    }


    /**
     * 分页条件查询结算单列表
     * @param businessId
     * @param userRole
     * @param qo
     * @return
     */
    @PostMapping("/page/main/list")
    @ApiOperation(value = "分页条件查询结算单列表(商家管理员、导师、老师可见)")
    public CommonResult<Page<PageMainSettlementVo>> pageMainList(
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
            @ApiParam(value = "角色id")@RequestHeader(value = "user-role",required = false) String userRole,
            @Validated @RequestBody PageMainSettlementQo qo
    ){
        if(!StringUtil.isSettlementRole(userRole)){
            return CommonResult.failed("暂无权限查看");
        }
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
            @ApiParam(value = "用户id")@RequestHeader(value = "user-id",required = false) String userId,
            @ApiParam(value = "角色id")@RequestHeader(value = "user-role",required = false) String userRole,
            @Validated @RequestBody PagePersonSettlementQo qo
    ){
        //如果不是结算角色只能看自己的数据
        if(!StringUtil.isSettlementRole(userRole)){
            qo.setUserId(userId);
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
            @ApiParam(value = "用户id")@RequestHeader(value = "user-id",required = false) String userId,
            @ApiParam(value = "角色id")@RequestHeader(value = "user-role",required = false) String userRole,
            @Validated @RequestBody PageSettlementDetailQo qo
    ){
        //如果不是结算角色只能看自己的数据
        if(!StringUtil.isSettlementRole(userRole)){
            qo.setUserId(userId);
        }
        return CommonResult.success(settlementService.pageDetailList(qo));
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
