package com.byx.pub.service;


import com.byx.pub.bean.vo.ListProductCategoryVo;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/6/27 20:53
 */
public interface ProductCategoryService {

    /**
     * 获取所有分类列表
     * @return
     */
    List<ListProductCategoryVo> getAllCategoryList();
}
