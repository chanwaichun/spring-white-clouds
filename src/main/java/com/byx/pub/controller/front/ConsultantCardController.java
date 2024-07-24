package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.*;
import com.byx.pub.bean.qo.AddOrUpdateCardBaseQo;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.qo.PageTagListQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.entity.AdminCard;
import com.byx.pub.plus.entity.CardShareFile;
import com.byx.pub.plus.entity.Tag;
import com.byx.pub.service.*;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * [客户端]-[商家]-[名片]服务Api
 * @Author Jump
 * @Date 2023/7/26 23:34
 */
@Slf4j
@RestController
@RequestMapping("/white/clouds/front/business/v1/card")
@Api(value = "[客户端]-[商家]-[名片]服务Api",tags = "[客户端]-[商家]-[名片]服务Api")
public class ConsultantCardController {
    @Resource
    AdminService adminService;
    @Resource
    AdminCardService adminCardService;
    @Resource
    ProductService productService;
    @Resource
    CardLikesService likesService;
    @Resource
    BusinessUserService businessUserService;
    @Resource
    InteractiveClickService clickService;

    /**
     * 查询商家下面所有名片
     * @param businessId 商家id(请求头)
     * @return
     */
    @GetMapping("/query/list")
    @ApiOperation(value = "查询商家下面所有名片")
    public CommonResult<List<AdminCard>> querySjCardList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId
    ){
        return CommonResult.success(adminCardService.getCardListBySj(businessId));
    }

    /**
     * 查询商家下面所有名片(传值)
     * @param businessId (传值)
     * @return
     */
    @GetMapping("/list/bySjId")
    @ApiOperation(value = "查询商家下面所有名片(传值)")
    public CommonResult<List<AdminCard>> queryCardListBySj(
            @ApiParam(value = "商家id") @RequestParam(value = "businessId",required = false) String businessId
    ){
        return CommonResult.success(adminCardService.getCardListBySj(businessId));
    }

    /**
     * 分页查询商家老师列表(选择咨询师)
     * @param businessId 商家id
     * @param adminName 咨询师名称
     * @param pageNum 当前页
     * @param pageSize 每页数
     * @return
     */
    @GetMapping("/page/teacher/list")
    @ApiOperation(value = "分页查询商家老师列表(选择咨询师)")
    public CommonResult<Page<AdminPageListVo>> pageListTeacher(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(value = "咨询师名称") @RequestParam(required = false) String adminName,
        @ApiParam(required = true, value = "当前页") @RequestParam("pageNum") Integer pageNum,
        @ApiParam(required = true, value = "每页数") @RequestParam("pageSize") Integer pageSize
    ){
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(3);
        roleIds.add(4);
        roleIds.add(5);
        return CommonResult.success(this.adminService.getSjAdminByRole(businessId,roleIds,adminName,pageNum,pageSize));
    }

    /**
     * 查看名片基础信息(根据名片id查询)
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/card/info")
    @ApiOperation(value = "查看名片基础信息(根据名片id查询)")
    public CommonResult<AdminCardVo> getCardInfo(
            @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        AdminCard card = this.adminCardService.getAdminCardById(cardId);
        if(Objects.isNull(card)){
            return CommonResult.failed("未找到名片信息");
        }
        //添加会员
        businessUserService.addSjMember(card.getAdminId(),card.getBusinessId(),userId,2,false);
        //添加浏览记录
        clickService.saveLog(cardId, PromotionTypeEnum.BROWSE_CARD.getValue(),"",userId,card.getBusinessId());
        return CommonResult.success(this.adminCardService.getCardInfo(cardId));
    }

    /**
     * 新增或修改名片基础页信息
     * @param bo 基础信息
     * @return
     */
    @PostMapping("/addOrUpdate/card/base")
    @ApiOperation(value = "新增或修改名片基础页信息")
    public CommonResult<String> addOrUpdateCardBaseInfo(
            @ApiParam(value = "基础信息")@Validated @RequestBody ManageAddOrUpdateCardBo bo
    ){
        return CommonResult.success(this.adminCardService.manageAddOrUpdateCardInfo(bo));
    }

    /**
     * 查询系统标签库
     * @param tagListQo 查询条件
     * @return
     */
    @PostMapping("/page/tag/list")
    @ApiOperation(value = "查询系统标签库")
    public CommonResult<IPage<Tag>> pageListTag(
            @ApiParam(value = "查询条件") @Validated @RequestBody PageTagListQo tagListQo
    ){
        return CommonResult.success(this.adminCardService.pageTagList(tagListQo));
    }

    /**
     * 新增或修改名片产品
     * @param cardProductBo
     * @return
     */
    @PostMapping("/addOrUpdate/card/products")
    @ApiOperation(value = "新增或修改名片产品")
    public CommonResult<Void> addOrUpdateCardProducts(
        @ApiParam(value = "名片产品")@Validated @RequestBody CardProductBo cardProductBo
    ){
        this.adminCardService.addOrUpdateCardProduct(cardProductBo);
        return CommonResult.success();
    }

    /**
     * 新增或修改名片广告
     * @param cardPosterBo
     * @return
     */
    @PostMapping("/addOrUpdate/card/poster")
    @ApiOperation(value = "新增或修改名片广告")
    public CommonResult<Void> addOrUpdateCardPoster(
            @ApiParam(value = "名片广告")@Validated @RequestBody CardPosterBo cardPosterBo
    ){
        this.adminCardService.addOrUpdateCardPoster(cardPosterBo);
        return CommonResult.success();
    }

    /**
     * 获取名片产品页数据
     * @param cardId
     * @return
     */
    @GetMapping("/get/productPage")
    @ApiOperation(value = "获取名片产品页数据")
    public CommonResult<CardProductPageVo> getCardProductPage(
        @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.queryCardProductPageVo(cardId));
    }

    /**
     * 选择推荐老师列表(系统所有名片)
     * @param cardQo 查询条件
     * @return
     */
    @PostMapping("/select/share/card")
    @ApiOperation(value = "选择推荐老师列表(系统所有名片)")
    public CommonResult<Page<PageAdminCardVo>> selectShareCard(
            @ApiParam(value = "查询条件") @Validated @RequestBody PageAdminCardQo cardQo
    ){
        return CommonResult.success(this.adminCardService.pageListCard(cardQo));
    }

    /**
     * 保存我的分享文件(合集)
     * @param shareBo 我的分享
     * @return
     */
    @PostMapping("/save/share/file")
    @ApiOperation(value = "保存我的分享文件(合集)")
    public CommonResult<Void> addShareFile(
            @ApiParam(value = "我的分享")@Validated @RequestBody SaveShareFileBo shareBo
    ){
        this.adminCardService.addOrUpdateShare(shareBo);
        return CommonResult.success();
    }

    /**
     * 删除分享文件
     * @param id 分享文件(集合)主键
     * @return
     */
    @GetMapping("/del/share/file")
    @ApiOperation(value = "删除分享文件")
    public CommonResult<Void> delShareFile(
            @ApiParam(required = true, value = "分享文件(集合)主键") @RequestParam("id") String id
    ){
        this.adminCardService.delShareFile(id);
        return CommonResult.success();
    }

    /**
     * 获取我的分享列表(一级)
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/share/file")
    @ApiOperation(value = "获取我的分享列表(一级)")
    public CommonResult<List<SaveShareFileBo>> getShareFileList(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.getShareFileList(cardId));
    }

    /**
     * 获取单个分享文件详情
     * @param id 主键id
     * @return
     */
    @GetMapping("/get/shareFile/info")
    @ApiOperation(value = "获取单个分享文件详情")
    public CommonResult<SaveShareFileBo> getShareFileInfo(
            @ApiParam(required = true, value = "主键id") @RequestParam("id") String id
    ){
        return CommonResult.success(this.adminCardService.getShareFileById(id));
    }

    /**
     * 获取分享合集下的文件
     * @param id 合集主键id
     * @param shareType 分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)
     * @return
     */
    @GetMapping("/get/share/folder/file")
    @ApiOperation(value = "获取分享合集下的文件")
    public CommonResult<List<SaveShareFileBo>> getShareFolderFileList(
            @ApiParam(required = true, value = "合集主键id") @RequestParam("id") String id,
            @ApiParam(value = "分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)") @RequestParam(required = false) Integer shareType
    ){
        return CommonResult.success(this.adminCardService.getShareFolderFile(id,shareType));
    }

    /**
     * 保存分享老师
     * @param shareTeacherBo 分享老师
     * @return
     */
    @PostMapping("/save/share/teacher")
    @ApiOperation(value = "保存分享老师")
    public CommonResult<Void> addShareTeacher(
            @ApiParam(value = "分享老师")@Validated @RequestBody BatchTeacherBo shareTeacherBo
    ){
        this.adminCardService.addOrUpdateShareTeacher(shareTeacherBo);
        return CommonResult.success();
    }

    /**
     * 删除分享老师
     * @param id 分享老师id
     * @return
     */
    @GetMapping("/del/share/teacher")
    @ApiOperation(value = "删除分享老师")
    public CommonResult<Void> delShareTeacher(
            @ApiParam(required = true, value = "分享老师id") @RequestParam("id") String id
    ){
        this.adminCardService.delShareTeacher(id);
        return CommonResult.success();
    }

    /**
     * 获取分享老师列表
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/share/teacher")
    @ApiOperation(value = "获取分享老师列表")
    public CommonResult<List<CardShareTeacherVo>> getShareTeacherList(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.getShareTeacherList(cardId));
    }

    /**
     * 保存分享商品
     * @param shareProductBo 分享商品
     * @return
     */
    @PostMapping("/save/share/product")
    @ApiOperation(value = "保存分享商品")
    public CommonResult<Void> addShareProduct(
            @ApiParam(value = "保存分享商品")@Validated @RequestBody BatchShareProductBo shareProductBo
    ){
        this.adminCardService.saveOrUpdateShareProduct(shareProductBo);
        return CommonResult.success();
    }

    /**
     * 删除分享商品
     * @param id 分享商品id
     * @return
     */
    @GetMapping("/del/share/product")
    @ApiOperation(value = "删除分享商品")
    public CommonResult<Void> delShareProduct(
            @ApiParam(required = true, value = "分享商品id") @RequestParam("id") String id
    ){
        this.adminCardService.delShareProduct(id);
        return CommonResult.success();
    }

    /**
     * 获取分享商品列表
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/share/product")
    @ApiOperation(value = "获取分享商品列表")
    public CommonResult<List<ProductPageListVo>> getShareProductList(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.getShareProductList(cardId));
    }

    /**
     * 分页查询所有商品(分享页选产品)
     * @param qo
     * @return
     */
    @PostMapping("/page/list/product")
    @ApiOperation(value = "分页查询所有商品(分享页选产品)")
    public CommonResult<Page<ProductPageListVo>> pageListProduct(
            @Validated @RequestBody PageProductQo qo
    ){
        return CommonResult.success(this.productService.pageListBusinessProduct(qo));
    }

    /**
     * 点赞名片
     * @param userId 用户id(请求头)
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/likes/card")
    @ApiOperation(value = "点赞名片")
    public CommonResult<Void> likesCard(
        @ApiParam(value = "用户id")@RequestHeader(value = "user-id",required = false) String userId,
        @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        likesService.likesCard(cardId,userId);
        return CommonResult.success();
    }

    /**
     * 是否点赞名片
     * @param userId 用户id(请求头)
     * @param cardId 名片id
     * @return true：是，false：否
     */
    @GetMapping("/is/likes")
    @ApiOperation(value = "是否点赞名片")
    public CommonResult<Boolean> isLikes(
            @ApiParam(value = "用户id")@RequestHeader(value = "user-id",required = false) String userId,
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(likesService.isLikes(cardId,userId));
    }


    /**
     * 互帮榜跳转逻辑
     * @param businessId
     * @param adminId
     * @return
     */
     @GetMapping("/jump/page")
     @ApiOperation(value = "判断跳转页面")
     public CommonResult<CardJumpVo> jumpPage(
     @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
     @ApiParam(value = "咨询师角色")@RequestHeader(value = "admin-role",required = false) String roleId,
     @ApiParam(value = "咨询师id")@RequestHeader(value = "admin-id",required = false) String adminId
     ){
         if(StringUtil.isPlatformAdmin(roleId)){
             return CommonResult.success(new CardJumpVo().setJumpType(2));
         }
         return CommonResult.success(adminCardService.toPage(adminId,businessId));
     }

    /**
     * 删除名片
     * @param cardId
     * @return
     */
    @GetMapping("/del")
    @ApiOperation(value = "删除名片")
    public CommonResult<Void> delCard(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
         this.adminCardService.delAdminCard(cardId);
         return CommonResult.success();
    }


    /**
     * 获取咨询师名片信息
     * @param adminId 咨询师id(传参)
     * @return

     @GetMapping("/get/admin/card")
     @ApiOperation(value = "获取咨询师名片信息")
     public CommonResult<AdminCardVo> getAddCardInfo(
     @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
     @ApiParam(required = true, value = "咨询师id") @RequestParam("adminId") String adminId
     ){
     return CommonResult.success(this.adminCardService.getOrAddCardInfo(adminId,businessId));
     }*/

}
