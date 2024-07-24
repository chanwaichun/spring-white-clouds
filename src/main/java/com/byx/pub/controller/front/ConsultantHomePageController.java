package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.FrontPageOrdersQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.PayStatus;
import com.byx.pub.service.*;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * [客户端]-[商家]-[首页]服务Api
 * @Author Jump
 * @Date 2023/6/17 12:02
 */
@RestController
@RequestMapping("/white/clouds/front/business/v1/homePage")
@Api(value = "[客户端]-[商家]-[首页]服务Api",tags = "[客户端]-[商家]-[首页账单]服务Api")
public class ConsultantHomePageController {

    @Resource
    FrontOrderService orderService;
    @Resource
    ProductService productService;
    @Resource
    HelpListService helpListService;
    @Resource
    AdminService adminService;

    /**
     * 咨询师信息(含商家信息)
     * @param adminId
     * @return
     */
    @GetMapping("/admin/info")
    @ApiOperation(value = "咨询师信息(含商家信息)")
    public CommonResult<AdminHomePageVo> adminInfo(
            @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId
    ){
        return CommonResult.success(this.adminService.getAdminHomePageVo(adminId));
    }

    /**
     * 互帮榜
     * @param adminId
     * @return
     */
    @GetMapping("/help/list")
    @ApiOperation(value = "互帮榜")
    public CommonResult<PageHomeHelpVo> getHelpList(
            @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId
    ){
        return CommonResult.success(this.helpListService.getPageHomeHelpVo(adminId));
    }


    /**
     * 分页查询商品信息(3条)
     * @return
     */
    @PostMapping("/product/list")
    @ApiOperation(value = "商品列表信息(3条)")
    public CommonResult<List<ProductPageListVo>> top3Product(
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId
    ){
        PageProductQo qo = new PageProductQo();
        qo.setBusinessId(businessId).setPageSize(3).setPageNum(1);
        Page<ProductPageListVo> page = this.productService.pageListBusinessProduct(qo);
        return CommonResult.success(page.getRecords());
    }

    /**
     * 订单列表(3条)
     * @param businessId
     * @param userRole
     * @param adminId
     * @return
     */
    @GetMapping("/order/list")
    @ApiOperation(value = "订单列表(3条)")
    public CommonResult<List<PageOrdersVo>> top3Order(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(value = "咨询师角色")@RequestHeader(value = "user-role",required = false) String userRole,
        @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId
    ){
        FrontPageOrdersQo queryQo = new FrontPageOrdersQo();
        queryQo.setBusinessId(businessId);
        if(StringUtil.isSeeSelfData(userRole)){
            queryQo.setAdminId(adminId);
        }
        //展示待付款，定金付款的订单
        List<Integer> payStatusList = new ArrayList<>();
        payStatusList.add(0);payStatusList.add(1);
        queryQo.setPayStatusList(payStatusList);
        queryQo.setPageNum(1);queryQo.setPageSize(3);
        Page<PageOrdersVo> pageOrdersVoPage = orderService.pageListOrder(queryQo);
        if(0 == pageOrdersVoPage.getTotal()){
            //若无两种状态的订单，订单列表显示已付款的订单
            queryQo.setPageNum(1);queryQo.setPageSize(3);queryQo.setPayStatus(PayStatus.SUCCESS.getCode());
            pageOrdersVoPage = orderService.pageListOrder(queryQo);
            if(0 == pageOrdersVoPage.getTotal()){
                return CommonResult.success(new ArrayList<>());
            }
        }
        return CommonResult.success(pageOrdersVoPage.getRecords());
    }




}
