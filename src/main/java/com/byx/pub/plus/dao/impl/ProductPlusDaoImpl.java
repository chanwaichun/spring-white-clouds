package com.byx.pub.plus.dao.impl;

import com.byx.pub.plus.entity.Product;
import com.byx.pub.plus.mapper.ProductPlusMapper;
import com.byx.pub.plus.dao.ProductPlusDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商家产品表 服务实现类
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Service
public class ProductPlusDaoImpl extends ServiceImpl<ProductPlusMapper, Product> implements ProductPlusDao {

}
