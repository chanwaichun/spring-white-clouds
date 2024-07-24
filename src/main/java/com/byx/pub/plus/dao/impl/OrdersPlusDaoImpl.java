package com.byx.pub.plus.dao.impl;

import com.byx.pub.plus.entity.Orders;
import com.byx.pub.plus.mapper.OrdersPlusMapper;
import com.byx.pub.plus.dao.OrdersPlusDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Service
public class OrdersPlusDaoImpl extends ServiceImpl<OrdersPlusMapper, Orders> implements OrdersPlusDao {

}
