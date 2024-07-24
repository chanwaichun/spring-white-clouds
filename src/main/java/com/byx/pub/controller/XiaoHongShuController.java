package com.byx.pub.controller;

import com.byx.pub.bean.xhs.OrderReceiverInfoVo;
import com.byx.pub.bean.xhs.XhsOrderVo;
import com.byx.pub.plus.dao.AdminPlusDao;
import com.byx.pub.plus.dao.ProductPlusDao;
import com.byx.pub.plus.entity.Product;
import com.byx.pub.plus.entity.XhsAuthToken;
import com.byx.pub.service.XiaoHongShuService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * [管理后台]-[小红书]-[回调]管理Api
 * @author Jump
 * @date 2023/12/20 15:10:55
 */
@Slf4j
@RestController
@RequestMapping("/white/clouds/manage/v1/xhs")
@Api(value = "[管理后台]-[小红书]-[回调]管理Api",tags = "[管理后台]-[小红书]-[回调]管理Api")
public class XiaoHongShuController {
    @Resource
    XiaoHongShuService xiaoHongShuService;
    @Resource
    ProductPlusDao productPlusDao;
    @Resource
    AdminPlusDao adminPlusDao;

    /**
     * 授权回调
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation("授权回调")
    @GetMapping("/callback")
    @ResponseBody
    public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("------1.小红书回调啦----");
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("------2.小红书回调 code:{}----state:{}",code,state);
        this.xiaoHongShuService.code2AccessToken(code);
        return "success";
    }

    @GetMapping("/test/syn/add/Order")
    @ApiOperation(value = "测试同步变更订单")
    public CommonResult<Void> testSynAddOrders(){
        this.xiaoHongShuService.synChangeOrder();
        return CommonResult.success();
    }

    /**
     * 同步过去一小时新增订单列表
     * @return
     */
    @GetMapping("/test/syn/add/order/list")
    @ApiOperation(value = "同步过去一小时新增订单列表")
    public CommonResult<List<Void>> synAddOrderList(){
        this.xiaoHongShuService.synAddOrder();
        return CommonResult.success();
    }

    @GetMapping("/get/order/receiver")
    @ApiOperation(value = "获取订单收货人信息")
    public CommonResult<OrderReceiverInfoVo> getOrderReceiver(
            @ApiParam(required = true, value = "小红书店铺Id") @RequestParam("sellerId") String sellerId,
            @ApiParam(required = true, value = "小红书订单id") @RequestParam("orderId") String orderId,
            @ApiParam(required = true, value = "开放收货地址id") @RequestParam("openAddressId") String openAddressId
    ){
        return CommonResult.success(this.xiaoHongShuService.getReceiverInfo(orderId,sellerId,openAddressId));
    }

    /**
     * 获取过去半小时更新订单列表
     * @param sellerId
     * @return
     */
    @GetMapping("/get/change/order/list")
    @ApiOperation(value = "获取过去半小时更新订单列表")
    public CommonResult<List<XhsOrderVo>> getChangeOrderList(
            @ApiParam(required = true, value = "小红书店铺Id") @RequestParam("sellerId") String sellerId
    ){
        return CommonResult.success(this.xiaoHongShuService.getUpdateOrderList(sellerId));
    }

    /**
     * 获取过去一小时新增订单列表
     * @return
     */
    @GetMapping("/get/add/order/list")
    @ApiOperation(value = "获取过去一小时新增订单列表")
    public CommonResult<List<XhsOrderVo>> getAddOrderList(
            @ApiParam(required = true, value = "小红书店铺Id") @RequestParam("sellerId") String sellerId
    ){
        return CommonResult.success(this.xiaoHongShuService.getAddOrderList(sellerId));
    }

    /**
     * 创建商品item
     * @param productId 产品id
     * @return
     */
    @GetMapping("/create/item")
    @ApiOperation(value = "创建商品item")
    public CommonResult<String> createItem(
        @ApiParam(required = true, value = "产品id") @RequestParam("productId") String productId,
        @ApiParam(value = "当前用户id")@RequestHeader(value = "admin-id",required = false) String adminId
    ){
        return CommonResult.success(this.xiaoHongShuService.createItem(this.productPlusDao.getById(productId),adminPlusDao.getById(adminId)));
    }

    /**
     * 获取运费模板列表(默认一个)
     * @param xhsToken
     * @return
     */
    @GetMapping("/get/carriage/templates")
    @ApiOperation(value = "获取运费模板(默认一个)")
    public CommonResult<String> getCarriageTemplate(
            @ApiParam(required = true, value = "小红书token") @RequestParam("xhsToken") String xhsToken
    ){
        return CommonResult.success(this.xiaoHongShuService.getCarriageTemplate(xhsToken));
    }

    /**
     * 获取物流方案(默认一个)
     * @param xhsToken
     * @return
     */
    @GetMapping("/get/logistics")
    @ApiOperation(value = "获取物流方案(默认一个)")
    public CommonResult<String> getLogistics(
            @ApiParam(required = true, value = "小红书token") @RequestParam("xhsToken") String xhsToken
    ){
        return CommonResult.success(this.xiaoHongShuService.getLogisticsList(xhsToken));
    }

    /**
     * 获取属性值列表
     * @param attributeId 属性id
     * @return
     */
    @GetMapping("/get/attribute/values")
    @ApiOperation(value = "获取属性值列表")
    public CommonResult<String> getAttributeValueList(
            @ApiParam(required = true, value = "属性id") @RequestParam("attributeId") String attributeId,
            @ApiParam(required = true, value = "小红书店铺id") @RequestParam("sellerId") String sellerId
    ){
        return CommonResult.success(this.xiaoHongShuService.getAttributeValueList(attributeId,sellerId));
    }

    /**
     * 获取末级类目下属性列表
     * @param categoryId 末级分类id
     * @return
     */
    @GetMapping("/get/attribute/list")
    @ApiOperation(value = "获取末级类目下属性列表")
    public CommonResult<String> getAttributeList(
            @ApiParam(value = "末级分类id") @RequestParam(required = false) String categoryId,
            @ApiParam(required = true, value = "小红书店铺id") @RequestParam("sellerId") String sellerId
    ){
        return CommonResult.success(this.xiaoHongShuService.getCategoryAttributeList(categoryId,sellerId));
    }

    /**
     * 获取末级类目下品牌
     * @param categoryId 末级分类id
     * @return
     */
    @GetMapping("/get/brand/list")
    @ApiOperation(value = "获取末级类目下品牌")
    public CommonResult<String> getBrand(
            @ApiParam(required = true, value = "末级分类id") @RequestParam("categoryId") String categoryId,
            @ApiParam(required = true, value = "小红书店铺id") @RequestParam("sellerId") String sellerId
    ){
        return CommonResult.success(this.xiaoHongShuService.getCategoryBrand(categoryId,sellerId));
    }

    /**
     * 获取类目列表
     * @return
     */
    @GetMapping("/get/category/list")
    @ApiOperation(value = "获取类目列表")
    public CommonResult<String> getCategories(
        @ApiParam(value = "分类id") @RequestParam(required = false) String categoryId,
        @ApiParam(required = true, value = "小红书店铺id") @RequestParam("sellerId") String sellerId
    ){
        return CommonResult.success(this.xiaoHongShuService.getCategories(categoryId,sellerId));
    }

    /**
     * code换token
     * @param code
     * @return
     */
    @GetMapping("/code/to/token")
    @ApiOperation(value = "code换token")
    public CommonResult<String> code2Token(
        @ApiParam(required = true, value = "code") @RequestParam("code") String code
    ){
        this.xiaoHongShuService.code2AccessToken(code);
        return CommonResult.success();
    }

    /**
     * 刷新店铺token
     * @return
     */
    @GetMapping("/refresh/token")
    @ApiOperation(value = "刷新店铺token")
    public CommonResult<String> refreshToken(
            @ApiParam(required = true, value = "小红书店铺id") @RequestParam("sellerId") String sellerId
    ){
        XhsAuthToken xhsToken = this.xiaoHongShuService.getXhsToken(sellerId);
        if(Objects.isNull(xhsToken)){
            CommonResult.failed("小红书店铺未授权应用，请联系运营人员");
        }
        xiaoHongShuService.refreshToken(xhsToken);
        return CommonResult.success();
    }

    /**
     * 上传素材
     * @param img 图片名
     * @return

     @GetMapping("/upload/material")
     @ApiOperation(value = "上传素材")
     public CommonResult<String> uploadMaterial(
     @ApiParam(required = true, value = "图片名") @RequestParam("img") String img
     ){
     return CommonResult.success(this.xiaoHongShuService.uploadMaterial(img));
     }*/

    /*@GetMapping("/enable/sku")
    @ApiOperation(value = "上架小红书sku")
    public CommonResult<String> enableSku(
            @ApiParam(required = true, value = "商品id") @RequestParam("productId") String productId,
            @ApiParam(required = true, value = "小红书token") @RequestParam("xhsToken") String xhsToken
    ){
        return CommonResult.success(xiaoHongShuService.availableSku(productId,xhsToken,true));
    }*/

}
