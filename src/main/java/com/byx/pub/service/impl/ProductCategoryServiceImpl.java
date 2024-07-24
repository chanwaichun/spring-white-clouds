package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.byx.pub.bean.vo.ChildCategoryVo;
import com.byx.pub.bean.vo.ListProductCategoryVo;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.plus.dao.ProductCategoryPlusDao;
import com.byx.pub.plus.entity.ProductCategory;
import com.byx.pub.service.ProductCategoryService;
import com.byx.pub.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品分类服务
 * @Author Jump
 * @Date 2023/6/27 20:52
 */
@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Resource
    ProductCategoryPlusDao categoryPlusDao;

    /**
     * 获取下级分类列表
     * @return
     */
    @Override
    public List<ListProductCategoryVo> getAllCategoryList(){
        List<ListProductCategoryVo> resList = new ArrayList<>();
        //获取一级类目
        List<ProductCategory> list = this.categoryPlusDao.lambdaQuery()
                .eq(ProductCategory::getDataStatus, Boolean.TRUE)
                .eq(ProductCategory::getLevel, SystemFinalCode.MENU_LEVEL_1_VALUE)
                .orderByAsc(ProductCategory::getCreateTime).list();
        if(CollectionUtils.isEmpty(list)){
            return resList;
        }
        resList = CopyUtil.copyList(list,ListProductCategoryVo.class);
        for(ListProductCategoryVo vo : resList){
            List<ProductCategory> subList = this.categoryPlusDao.lambdaQuery()
                    .eq(ProductCategory::getDataStatus, Boolean.TRUE)
                    .eq(ProductCategory::getParentId, vo.getId())
                    .orderByAsc(ProductCategory::getCreateTime).list();
            if(CollectionUtils.isNotEmpty(subList)){
                vo.setChildCategoryVoList(CopyUtil.copyList(subList, ChildCategoryVo.class));
            }
        }
        return resList;
    }

}
