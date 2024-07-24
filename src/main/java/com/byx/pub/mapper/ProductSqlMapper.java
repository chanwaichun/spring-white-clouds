package com.byx.pub.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageProductQo;
import com.byx.pub.bean.vo.ProductPageListVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: jump
 * @Date: 2023-05-16  22:09
 */
public interface ProductSqlMapper {
    /**
     * 分页查询总部商品列表
     * @param page
     * @param queryQo
     * @return
     */
    Page<ProductPageListVo> queryPageListProduct(Page page,@Param(value = "queryQo")PageProductQo queryQo);

    /**
     * 统计用户对于某商品下单次数
     * @param uid
     * @param pid
     * @return
     */
    @Select("select count(d.id) AS num from orders AS o LEFT JOIN order_detail AS d on o.order_sn = d.order_sn where o.user_id = #{uid} AND d.product_id=#{pid}")
    Integer countUserOrderNumByPid(
            @Param(value = "uid")String uid,
            @Param(value = "pid")String pid
    );
}
