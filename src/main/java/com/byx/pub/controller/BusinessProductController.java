package com.byx.pub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateProductQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.ListProductCategoryVo;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.bean.vo.ProductPageListVo;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.plus.entity.ProductCategory;
import com.byx.pub.service.ProductCategoryService;
import com.byx.pub.service.ProductService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * [管理后台]-[商家商品]管理Api
 * 商家自建商品，无需审核，各自管理
 * @author Jump
 * @date 2023/5/16 18:18:15
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/business/product")
@Api(value = "[管理后台]-[商家商品]管理Api",tags = "[管理后台]-[商家商品]管理Api")
public class BusinessProductController {
    @Resource
    ProductService productService;
    @Resource
    ProductCategoryService categoryService;

    /**
     * 获取所有商品分类
     * @return
     */
    @GetMapping("/get/all/category")
    @ApiOperation(value = "获取所有商品分类")
    public CommonResult<List<ListProductCategoryVo>> getAllCategory(){
        return CommonResult.success(this.categoryService.getAllCategoryList());
    }

    /**
     * 分页查询
     * @param businessId
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询")
    public CommonResult<Page<ProductPageListVo>> pageList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody PageProductQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(this.productService.pageListBusinessProduct(qo));
    }

    /**
     * 新增或修改商品
     * @param productQo
     * @return
     */
    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "新增或修改商品")
    public CommonResult<Void> addOrUpdate(
        @Validated @RequestBody AddOrUpdateProductQo productQo
    ){
        if(StringUtil.isEmpty(productQo.getBusinessId())){
            return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"请选择商家");
        }
        this.productService.addOrUpdateProduct(productQo);
        return CommonResult.success();
    }

    /**
     * 批量上下架商品
     * @param status 状态
     * @param ids id集合
     * @return
     */
    @GetMapping("/batch/switch")
    @ApiOperation(value = "批量上下架商品")
    public CommonResult<Void> batchSwitch(
        @ApiParam(value = "状态") @RequestParam("status")Boolean status,
        @ApiParam(value = "id集合")@RequestParam("ids") List<String> ids
    ){
        this.productService.batchChangeShelvesStatus(ids,status);
        return CommonResult.success();
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @GetMapping("/batch/del")
    @ApiOperation(value = "批量删除商品")
    public CommonResult<Void> batchDel(
            @ApiParam(value = "id集合")@RequestParam("ids") List<String> ids
    ){
        this.productService.batchDel(ids);
        return CommonResult.success();
    }

    /**
     * 获取商品详情
     * @param id 主键
     * @return
     */
    @GetMapping("/get/detail")
    @ApiOperation(value = "获取商品详情")
    public CommonResult<ProductDetailVo> getDetail(
            @ApiParam(value = "主键")@RequestParam("id")String id
    ){
        return CommonResult.success(this.productService.getProductById(id));
    }


}
