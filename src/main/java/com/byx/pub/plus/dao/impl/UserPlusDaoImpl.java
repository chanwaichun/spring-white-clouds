package com.byx.pub.plus.dao.impl;

import com.byx.pub.plus.entity.User;
import com.byx.pub.plus.mapper.UserPlusMapper;
import com.byx.pub.plus.dao.UserPlusDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Service
public class UserPlusDaoImpl extends ServiceImpl<UserPlusMapper, User> implements UserPlusDao {

}
