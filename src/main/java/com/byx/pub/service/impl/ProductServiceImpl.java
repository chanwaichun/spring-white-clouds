package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateProductQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.bean.vo.ProductPageListVo;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.mapper.ProductSqlMapper;
import com.byx.pub.plus.dao.ProductCategoryPlusDao;
import com.byx.pub.plus.dao.ProductPlusDao;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.ProductCodeHelper;
import com.byx.pub.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * 商品服务Api
 * @author Jump
 * @date 2023/5/16 17:22:38
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    ProductPlusDao productPlusDao;
    @Resource
    ProductCodeHelper productCodeHelper;
    @Resource
    ProductSqlMapper productSqlMapper;
    @Resource
    AdminCardService cardService;
    @Resource
    ProductCategoryPlusDao categoryPlusDao;
    @Resource
    AdminService adminService;
    @Resource
    FrontUserService userService;


    /**
     * 新增或修改商品信息
     * 1.新增直接插入 商家商品关联表，修改商品范围则需要做数据处理
     * 2.另 商品上下架 需要同步上下架 关系表
     * 3.新增用户也需要同步 新增关联总部[通铺]商品
     * @param productQo
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateProduct(AddOrUpdateProductQo productQo){
        ProductCategory category = this.categoryPlusDao.getById(productQo.getCategoryId());
        if(Objects.isNull(category)){
            throw new ApiException("未找到类目信息");
        }
        Product product = new Product();
        //新增商品信息
        if(StringUtils.isEmpty(productQo.getId())){
            Queue<String> skuCodeList = productCodeHelper.genCode("P", 1);
            BeanUtils.copyProperties(productQo,product);
            product.setId(IdWorker.getIdStr());
            product.setProductCode(skuCodeList.poll());
            product.setCategoryName(category.getTitle());
            this.productPlusDao.save(product);
            //同步相关名片产品 如果指定某名片就只推所选，否则推商家所有名片
            if(StringUtil.isEmpty(productQo.getCardId())){
                //查询商家下所有名片 并同步 产品过去
                List<AdminCard> cardList = cardService.getCardListBySj(productQo.getBusinessId());
                if(CollectionUtils.isNotEmpty(cardList)){
                    for(AdminCard adminCard : cardList){
                        this.cardService.sendCardProduct(new CardProduct()
                                .setCardId(adminCard.getId())
                                .setProductId(product.getId())
                                .setSellType(SystemFinalCode.ONE_VALUE)
                        );
                    }
                }
            }else{
                AdminCard card = this.cardService.getAdminCardById(productQo.getCardId());
                if(Objects.isNull(card)){
                    throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到名片信息");
                }
                this.cardService.sendCardProduct(new CardProduct()
                        .setCardId(productQo.getCardId())
                        .setProductId(product.getId())
                        .setSellType(SystemFinalCode.ONE_VALUE)
                );
            }
        }else{//修改
            Product oldProduct = this.productPlusDao.getById(productQo.getId());
            if(Objects.isNull(oldProduct)){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到商品数据");
            }
            if(!oldProduct.getCategoryId().equals(productQo.getCategoryId())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"不可以修改商品分类");
            }
            //修改商品
            BeanUtils.copyProperties(productQo,product);
            product.setBusinessId(oldProduct.getBusinessId());//不允许修改商家
            product.setCategoryName(category.getTitle());
            this.productPlusDao.updateById(product);
        }
    }

    /**
     * 分页查询商品
     * @param qo
     * @return
     */
    public Page<ProductPageListVo> pageListBusinessProduct(PageProductQo qo){
        return productSqlMapper.queryPageListProduct(new Page(qo.getPageNum(), qo.getPageSize()), qo);
    }

    /**
     * 批量(单个)上、下架
     * 同步上下架关联数据
     * @param ids
     * @param shelvesStatus
     */
    public void batchChangeShelvesStatus(List<String> ids,Boolean shelvesStatus){
        List<Product> updateList = new ArrayList<>();
        for(String id : ids){
            Product updateBean = new Product();
            updateBean.setId(id);
            updateBean.setShelvesStatus(shelvesStatus);
            updateList.add(updateBean);
        }
        this.productPlusDao.updateBatchById(updateList);
    }

    /**
     * 批量删除商品
     * @param ids
     */
    public void batchDel(List<String> ids){
        //删除(软)商品数据
        List<Product> updateList = new ArrayList<>();
        for(String id : ids){
            Product updateBean = new Product();
            updateBean.setId(id);
            updateBean.setDataStatus(Boolean.FALSE);
            updateList.add(updateBean);
        }
        this.productPlusDao.updateBatchById(updateList);
    }

    /**
     * 查询商品信息
     * @param productId
     * @return
     */
    public ProductDetailVo getProductById(String productId){
        ProductDetailVo resVo = new ProductDetailVo();
        Product product = this.productPlusDao.getById(productId);
        if(Objects.isNull(product)){
            throw new ApiException("未找到商品数据");
        }
        BeanUtils.copyProperties(product,resVo);
        //关联名片
        if(StringUtil.notEmpty(product.getCardId())){
            AdminCard adminCardById = this.cardService.getAdminCardById(product.getCardId());
            if(Objects.nonNull(adminCardById)){
                resVo.setCardId(product.getCardId());
                resVo.setCardPhone(adminCardById.getPhone());
                resVo.setCardName(adminCardById.getNickName());
                resVo.setAdminId(adminCardById.getAdminId());
                //头像改为 咨询师微信头像
                User user = this.userService.getUserByAdminId(adminCardById.getAdminId());
                if(Objects.nonNull(user)){
                    resVo.setCardImg(user.getUserImg());
                }
            }
            return resVo;
        }
        //获取商家管理员id(按权限正序)
        List<Admin> adminListBySj = adminService.getAdminListBySj(product.getBusinessId());
        if(!CollectionUtils.isEmpty(adminListBySj)){
            resVo.setCardPhone(adminListBySj.get(0).getMobile());
            resVo.setAdminId(adminListBySj.get(0).getAdminId());
        }
        return resVo;
    }

    /**
     * 根据产品ids查询
     * @param ids
     * @return
     */
    public List<Product> getProductListByIds(List<String> ids){
       return this.productPlusDao.lambdaQuery()
               .in(Product::getId,ids).orderByDesc(Product::getCreateTime).list();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Product getProductDbById(String id){
        return this.productPlusDao.getById(id);
    }

}
