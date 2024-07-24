package com.byx.pub.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.ManageAddOrUpdateCardBo;
import com.byx.pub.bean.bo.ManageAddOrUpdateProduct;
import com.byx.pub.bean.bo.ManageAddOrUpdateShareBo;
import com.byx.pub.bean.qo.AddOrUpdateCardBaseQo;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.qo.PageTagListQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.entity.AdminCard;
import com.byx.pub.plus.entity.CardPaper;
import com.byx.pub.plus.entity.Tag;
import com.byx.pub.service.AdminCardService;
import com.byx.pub.service.AdminService;
import com.byx.pub.service.ProductService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/8/5 14:19
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/business/card")
@Api(value = "[管理后台]-[商家]-[名片]管理Api",tags = "[管理后台]-[商家]-[名片]管理Api")
public class BusinessCardController {
    @Resource
    AdminService adminService;
    @Resource
    AdminCardService adminCardService;
    @Resource
    ProductService productService;

    /**
     * 分页查询名片列表
     * @param cardQo 查询条件
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询名片列表")
    public CommonResult<Page<PageAdminCardVo>> pageList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(value = "查询条件")@Validated @RequestBody PageAdminCardQo cardQo
    ){
        cardQo.setBusinessId(businessId);
        Page<PageAdminCardVo> page = this.adminCardService.pageListCard(cardQo);
        if(SystemFinalCode.ZERO_VALUE == page.getTotal()){
            return CommonResult.success(page);
        }
        //获取审核状态
        for(PageAdminCardVo vo : page.getRecords()){
            vo.setPaperStatus(adminCardService.getPaperAuditStatus(vo.getId()));
        }
        return CommonResult.success(page);
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
     * 获取证书列表(用于审核)
     * @param cardId
     * @return
     */
    @GetMapping("/paper/list")
    @ApiOperation("获取证书列表(用于审核)")
    public  CommonResult<List<CardPaper>> getPaperList(
        @ApiParam(required = true, value = "名片id") @RequestParam("cardId")String cardId
    ){
        return CommonResult.success(this.adminCardService.getPaperList(cardId,1));
    }

    /**
     * 审核单个证书
     * @param id 主键
     * @param paperStatus 状态(2：不通过，3：通过)
     * @param auditReason 拒绝理由
     * @return
     */
    @GetMapping("/paper/audit/single")
    @ApiOperation("审核单个证书")
    public  CommonResult<Void> auditSingle(
        @ApiParam(required = true, value = "主键") @RequestParam("id")String id,
        @ApiParam(required = true, value = "状态(2：不通过，3：通过)") @RequestParam("paperStatus")Integer paperStatus,
        @ApiParam(value = "拒绝理由") @RequestParam(value = "auditReason",required = false)String auditReason
    ){
        this.adminCardService.auditSinglePaper(id,paperStatus,auditReason);
        return CommonResult.success();
    }

    /**
     * 审核全量证书
     * @param cardId 名片id
     * @param paperStatus 状态(2：不通过，3：通过)
     * @param auditReason 拒绝理由
     * @return
     */
    @GetMapping("/paper/audit/all")
    @ApiOperation("审核全量证书")
    public  CommonResult<Void> auditAll(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId")String cardId,
            @ApiParam(required = true, value = "状态(2：不通过，3：通过)") @RequestParam("paperStatus")Integer paperStatus,
            @ApiParam(value = "拒绝理由") @RequestParam(value = "auditReason",required = false)String auditReason
    ){
        this.adminCardService.auditAllPaper(cardId,paperStatus,auditReason);
        return CommonResult.success();
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
     * 查看名片详情
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/card/info")
    @ApiOperation(value = "查看名片详情")
    public CommonResult<AdminCardVo> getCardInfo(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.getCardInfo(cardId));
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
     * 获取名片产品页数据
     * @param cardId
     * @return
     */
    @GetMapping("/get/product/page")
    @ApiOperation(value = "获取名片产品页数据")
    public CommonResult<CardProductPageVo> getCardProductPage(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.queryCardProductPageVo(cardId));
    }

    /**
     * 新增或修改名片产品页信息
     * @param bo 产品页信息
     * @return
     */
    @PostMapping("/addOrUpdate/card/product")
    @ApiOperation(value = "新增或修改名片产品页信息")
    public CommonResult<Void> addOrUpdateCardProductInfo(
            @ApiParam(value = "产品页信息")@Validated @RequestBody ManageAddOrUpdateProduct bo
    ){
        this.adminCardService.manageAddOrUpdateCardProduct(bo);
        return CommonResult.success();
    }

    /**
     * 新增或修改名片分享页信息
     * @param bo 分享页信息
     * @return
     */
    @PostMapping("/addOrUpdate/card/share")
    @ApiOperation(value = "新增或修改名片分享页信息")
    public CommonResult<Void> addOrUpdateCardShareInfo(
            @ApiParam(value = "分享页信息")@Validated @RequestBody ManageAddOrUpdateShareBo bo
    ){
        this.adminCardService.manageAddOrUpdateCardShare(bo);
        return CommonResult.success();
    }

    /**
     * 获取名片分享页页数据
     * @param cardId 名片id
     * @return
     */
    @GetMapping("/get/share/page")
    @ApiOperation(value = "获取名片分享页页数据")
    public CommonResult<ManageCardShareVo> getCardSharePage(
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ){
        return CommonResult.success(this.adminCardService.getCardShareVo(cardId));
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
     * 初始化名片二维码
     * @return
     */
    @GetMapping("/info/qrcode")
    @ApiOperation(value = "初始化名片二维码")
    public CommonResult<Void> infoQrCode(){
        this.adminCardService.infoCardQrCode();
        return CommonResult.success();
    }

    /**
     * 删除名片(对接)
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
     * 获取咨询师名片信息(查插)
     * @param adminId 咨询师id(请求头)
     * @return

     @GetMapping("/get/admin/card")
     @ApiOperation(value = "获取咨询师名片信息")
     public CommonResult<AdminCardVo> getAddCardInfo(
     @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
     @ApiParam(value = "咨询师id")@RequestHeader(value = "admin-id",required = false) String adminId
     ){
     if(StringUtil.isEmpty(businessId) || StringUtil.isEmpty(adminId)){
     return CommonResult.failed("未找到信息");
     }
     return CommonResult.success(this.adminCardService.getOrAddCardInfo(adminId,businessId));
     }*/
}
