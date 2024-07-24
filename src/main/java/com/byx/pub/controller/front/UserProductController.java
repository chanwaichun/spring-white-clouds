package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.AdminCardVo;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.bean.vo.ProductPageListVo;
import com.byx.pub.bean.vo.UserProductDetailVo;
import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.entity.AdminCard;
import com.byx.pub.plus.entity.Product;
import com.byx.pub.service.AdminCardService;
import com.byx.pub.service.BusinessUserService;
import com.byx.pub.service.InteractiveClickService;
import com.byx.pub.service.ProductService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * [客户端]-[用户]-[咨询师]服务Api
 * @Author: jump
 * @Date: 2023-05-22  22:38
 */
@RestController
@RequestMapping("/white/clouds/front/user/v1/consultant")
@Api(value = "[客户端]-[用户]-[咨询师]服务Api",tags = "[客户端]-[用户]-[咨询师]服务Api")
public class UserProductController {

    @Resource
    ProductService productService;
    @Resource
    AdminCardService cardService;
    @Resource
    BusinessUserService businessUserService;
    @Resource
    InteractiveClickService clickService;

    /**
     * 查看名片详情
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/card/info")
    @ApiOperation(value = "查看名片详情")
    public CommonResult<AdminCardVo> getCardInfo(
            @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        AdminCard card = this.cardService.getAdminCardById(cardId);
        if(Objects.isNull(card)){
            return CommonResult.failed("未找到名片信息");
        }
        //添加会员
        businessUserService.addSjMember(card.getAdminId(),card.getBusinessId(),userId,2,false);
        //添加浏览记录
        clickService.saveLog(cardId, PromotionTypeEnum.BROWSE_CARD.getValue(),"",userId,card.getBusinessId());
        return CommonResult.success(this.cardService.getCardInfo(cardId));
    }

    /**
     * 分页查询商家商品
     * @param cardId
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询商家商品")
    public CommonResult<Page<ProductPageListVo>> pageList(
        @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        AdminCard adminCardById = this.cardService.getAdminCardById(cardId);
        if(Objects.isNull(adminCardById) || !adminCardById.getDataStatus()){
            return CommonResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"未找到名片信息");
        }
        PageProductQo qo = new PageProductQo();
        qo.setBusinessId(adminCardById.getBusinessId());
        return CommonResult.success(this.productService.pageListBusinessProduct(qo));
    }

    /**
     * 查看商商品详情
     * @param id
     * @return
     */
    @GetMapping("/get/detail")
    @ApiOperation(value = "查看商商品详情")
    public CommonResult<ProductDetailVo> changeShelvesStatus(
            @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
            @ApiParam(required = true, value = "主键") @RequestParam("id") String id
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
