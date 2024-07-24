package com.byx.pub.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateProductQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.ListProductCategoryVo;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.bean.vo.ProductPageListVo;
import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.plus.entity.Product;
import com.byx.pub.service.BusinessUserService;
import com.byx.pub.service.InteractiveClickService;
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
import java.util.Objects;


/**
 * [客户端]-[商家]-[商品]服务Api
 * @author Jump
 * @date 2023/5/19 17:49:07
 */
@RestController
@RequestMapping("/white/clouds/front/business/v1/product")
@Api(value = "[客户端]-[商家]-[商品]服务Api",tags = "[客户端]-[商家]-[商品]服务Api")
public class FrontProductController {
    @Resource
    ProductService productService;
    @Resource
    ProductCategoryService categoryService;
    @Resource
    BusinessUserService businessUserService;
    @Resource
    InteractiveClickService clickService;



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
            @ApiParam(value = "咨询师角色")@RequestHeader(value = "admin-role",required = false) String roleId,
            @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
            @Validated @RequestBody AddOrUpdateProductQo productQo
    ){
        if(StringUtil.isPlatformAdmin(roleId)){
            return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"平台人员不能修改商品");
        }
        productQo.setBusinessId(businessId);
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
     * 获取商品详情
     * @param id 主键
     * @return
     */
    @GetMapping("/get/detail")
    @ApiOperation(value = "获取商品详情")
    public CommonResult<ProductDetailVo> getDetail(
        @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
        @ApiParam(value = "主键")@RequestParam("id")String id
    ){
        Product product = this.productService.getProductDbById(id);
        if(Objects.isNull(product)){
            return CommonResult.failed("未找到商品数据");
        }
        //添加会员
        businessUserService.addSjMember("",product.getBusinessId(),userId,1,false);
        //添加记录
        clickService.saveLog(id, PromotionTypeEnum.BROWSE_PRODUCT.getValue(),"",userId,product.getBusinessId());
        return CommonResult.success(this.productService.getProductById(id));
    }



}
