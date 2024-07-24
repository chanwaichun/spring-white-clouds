package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.qo.PageManageOrdersQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.plus.entity.HotInfo;
import com.byx.pub.service.AdminCardService;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.service.FrontUserPanelService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * [客户端]-[用户]-[首页]服务Api
 * @Author Jump
 * @Date 2023/6/15 22:17
 */
@RestController
@RequestMapping("/white/clouds/front/user/v1/homePage")
@Api(value = "[客户端]-[用户]-[首页]服务Api",tags = "[客户端]-[用户]-[首页]服务Api")
public class UserHomePageController {
    @Resource
    FrontOrderService frontOrderService;
    @Resource
    FrontUserPanelService panelService;


    /**
     * 首页搜索名片(不传商家id)
     * @param cardQo 查询条件
     * @return
     */
    @PostMapping("/search/card")
    @ApiOperation(value = "首页搜索名片(不传商家id)")
    public CommonResult<Page<PageSearchCardVo>> selectShareCard(
            @ApiParam(value = "查询条件") @Validated @RequestBody PageAdminCardQo cardQo
    ){
        return CommonResult.success(this.panelService.searchAdminCard(cardQo));
    }

    /**
     * 个人中心未完成订单
     * @param userId
     * @return
     */
    @GetMapping("/order/list")
    @ApiOperation(value = "个人中心未完成订单")
    public CommonResult<List<PageOrdersVo>> orderList(
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId
    ){
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        PageManageOrdersQo queryQo = new PageManageOrdersQo()
                .setStatusList(statusList)
                .setUserId(userId)
                .setPageNum(1)
                .setPageSize(3);
        Page<PageOrdersVo> page = this.frontOrderService.queryManagePageOrders(queryQo);
        if(0 == page.getTotal()){
            return CommonResult.success(new ArrayList<>());
        }
        return CommonResult.success(page.getRecords());
    }

    /**
     * 服务过的咨询师列表
     * @param userId
     * @return
     */
    @GetMapping("/service/admin/list")
    @ApiOperation(value = "服务过的咨询师列表")
    public CommonResult<List<AdminShortCardVo>> serviceAdminList(
            @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId
    ){
        return CommonResult.success(this.panelService.queryUserByOrderAdminList(userId));
    }

    /**
     * 推荐商品列表
     * @return
     */
    @GetMapping("/product/list")
    @ApiOperation(value = "推荐商品列表")
    public CommonResult<List<PanelProductVo>> productList(
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
            @ApiParam(value = "咨询师角色")@RequestHeader(value = "user-role",required = false) String userRole
    ){
        return CommonResult.success(this.panelService.queryPanelProducts(userRole,businessId));
    }

    /**
     * 推荐咨询师列表(商家展示商家的名片)
     * @param businessId
     * @param userRole
     * @return
     */
    @GetMapping("/hot/admin/list")
    @ApiOperation(value = "推荐咨询师列表(商家展示商家的名片)")
    public CommonResult<List<ListHotAdminCardVo>> hotAdminList(
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
            @ApiParam(value = "咨询师角色")@RequestHeader(value = "user-role",required = false) String userRole
    ){
        return CommonResult.success(this.panelService.getHostAdminList(userRole,businessId));
    }

    /**
     * 资讯推荐
     * @return
     */
    @GetMapping("/hotInfo/list")
    @ApiOperation(value = "资讯推荐")
    public CommonResult<List<HotInfo>> hostInfoList(){
        return CommonResult.success(this.panelService.queryHotInfoList());
    }






}
