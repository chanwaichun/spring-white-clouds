package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateProductQo;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.bean.vo.ProductPageListVo;
import com.byx.pub.bean.xhs.XhsPushProductResVo;
import com.byx.pub.plus.entity.Product;

import java.util.List;

/**
 * @author Jump
 * @date 2023/5/16 17:23:08
 */
public interface ProductService {

    /**
     * 新增或修改商品信息
     * @param productQo
     */
    void addOrUpdateProduct(AddOrUpdateProductQo productQo);

    /**
     * 分页查询总部商品
     * @param qo
     * @return
     */
    Page<ProductPageListVo> pageListBusinessProduct(PageProductQo qo);

    /**
     * 批量(单个)上、下架
     * @param ids
     * @param shelvesStatus
     */
    void batchChangeShelvesStatus(List<String> ids, Boolean shelvesStatus);

    /**
     * 批量删除商品
     * @param ids
     */
    void batchDel(List<String> ids);


    /**
     * 查询商品信息
     * @param productId
     * @return
     */
     ProductDetailVo getProductById(String productId);

    /**
     * 根据产品ids查询
     * @param ids
     * @return
     */
    List<Product> getProductListByIds(List<String> ids);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Product getProductDbById(String id);
}
