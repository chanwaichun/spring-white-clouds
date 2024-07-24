package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.byx.pub.bean.vo.ListAdminPlatformVo;
import com.byx.pub.bean.xhs.XhsPushProductResVo;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.AdminPlusDao;
import com.byx.pub.plus.dao.ProductPlusDao;
import com.byx.pub.plus.dao.XhsAdminProductPlusDao;
import com.byx.pub.plus.dao.XhsAuthTokenPlusDao;
import com.byx.pub.plus.entity.Admin;
import com.byx.pub.plus.entity.Product;
import com.byx.pub.plus.entity.XhsAdminProduct;
import com.byx.pub.plus.entity.XhsAuthToken;
import com.byx.pub.service.AdminService;
import com.byx.pub.service.PlatformService;
import com.byx.pub.service.XiaoHongShuService;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/12/26 21:31
 */
@Slf4j
@Service
public class PlatformServiceImpl implements PlatformService {
    @Resource
    AdminPlusDao adminPlusDao;
    @Resource
    XiaoHongShuService xiaoHongShuService;
    @Resource
    XhsAdminProductPlusDao xhsAdminProductPlusDao;
    @Resource
    ProductPlusDao productPlusDao;
    @Resource
    AdminService adminService;

    /**
     * 推送小红书商品
     * @param productId
     * @param adminId
     * @return
     */
    public XhsPushProductResVo pushXhsProduct(String productId, String adminId){
        XhsPushProductResVo resVo = new XhsPushProductResVo();
        List<String> successList = new ArrayList<>();
        List<String> failList = new ArrayList<>();
        //获取商品信息
        Product product = this.productPlusDao.getById(productId);
        if(Objects.isNull(product) || !product.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到商品信息");
        }
        if(StringUtil.countZwNum(product.getProductName()) < 8 || product.getProductName().length() > 16){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"小红书要求商品名称至少填写8个汉字或16个字符");
        }
        if(product.getServiceNum() <= 1){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"商品库存不足");
        }
        if(product.getPrice().compareTo(BigDecimal.valueOf(0.1)) < 0){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"商品售价不能低于0.1元");
        }
        //获取当前人的信息
        Admin nowAdmin = this.adminService.getAdminById(adminId);
        if(Objects.isNull(nowAdmin)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到当前咨询师信息");
        }
        //如果是商家管理员 推送当前商家所有小红书授权店铺
        if(RoleTypeEnum.SJ_MANEGE.getValue().equals(nowAdmin.getRoleId())){
            List<Admin> xhsAdminListBySj = this.adminService.getXhsAdminListBySj(nowAdmin.getBusinessId());
            if(CollectionUtils.isEmpty(xhsAdminListBySj)){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"暂无已授权小红书店铺的咨询师");
            }
            //循环推送
            for(Admin admin : xhsAdminListBySj){
                //校验当前用户是否推送过此产品
                XhsAdminProduct xhsAdminProduct = this.xhsAdminProductPlusDao.lambdaQuery().eq(XhsAdminProduct::getAdminId, admin.getAdminId())
                        .eq(XhsAdminProduct::getProductId, productId).last("limit 1").one();
                if(Objects.nonNull(xhsAdminProduct)){
                    failList.add("咨询师["+admin.getTrueName()+"]已推送过当前产品");
                    continue;
                }
                String item = this.xiaoHongShuService.createItem(product, admin);
                if("success".equals(item)){
                    successList.add("咨询师["+admin.getTrueName()+"]推送成功");
                }else{
                    failList.add("咨询师["+admin.getTrueName()+"]"+item);
                }
            }
        }
        //如果是普通咨询师 则只推到自己的店铺
        if(Integer.parseInt(nowAdmin.getRoleId()) > Integer.parseInt(RoleTypeEnum.SJ_MANEGE.getValue())){
            //校验是否已经推送过
            XhsAdminProduct xhsAdminProduct = this.xhsAdminProductPlusDao.lambdaQuery().eq(XhsAdminProduct::getAdminId, adminId)
                    .eq(XhsAdminProduct::getProductId, productId).last("limit 1").one();
            if(Objects.nonNull(xhsAdminProduct)){
                failList.add("咨询师["+nowAdmin.getTrueName()+"]已推送过当前产品");
                resVo.setFailList(failList);
                return resVo;
            }
            String item = this.xiaoHongShuService.createItem(product,nowAdmin);
            if("success".equals(item)){
                successList.add("咨询师["+nowAdmin.getTrueName()+"]推送成功");
            }else{
                failList.add("咨询师["+nowAdmin.getTrueName()+"]"+item);
            }
        }
        resVo.setFailList(failList);
        resVo.setSuccessList(successList);
        return resVo;
    }

    /**
     * 获取商家跨平台帐号列表
     * @param bid
     * @return
     */
    public List<ListAdminPlatformVo> adminPlatformList(String bid){
        //平台用
        if(StringUtil.isEmpty(bid)){
            List<Admin> list = this.adminPlusDao.lambdaQuery()
                    .eq(Admin::getDataStatus, true).eq(Admin::getUserStatus, true)
                    .orderByAsc(Admin::getBusinessId).list();
            if(CollectionUtils.isEmpty(list)){
                return new ArrayList<>();
            }
            return CopyUtil.copyList(list,ListAdminPlatformVo.class);
        }
        //商家用
        List<Admin> list = this.adminPlusDao.lambdaQuery().eq(Admin::getBusinessId, bid)
                .eq(Admin::getDataStatus, true).eq(Admin::getUserStatus, true)
                .orderByAsc(Admin::getRoleId).list();
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return CopyUtil.copyList(list,ListAdminPlatformVo.class);
    }

    /**
     * 绑定小红书店铺id
     * @param adminId
     * @param xhsSellerId
     */
    public void bindXhsId(String adminId,String xhsSellerId){
        //1.先绑定
        this.adminPlusDao.updateById(new Admin().setAdminId(adminId).setXhsSellerId(xhsSellerId));
        //2.查询店铺是否有授权
        XhsAuthToken xhsToken = this.xiaoHongShuService.getXhsToken(xhsSellerId);
        if(Objects.isNull(xhsToken)){
            throw new ApiException(ResultCode.XHS_NO_AUTH.getCode(),"绑定成功，小红书店铺未授权，请前往授权");
        }
    }








}
