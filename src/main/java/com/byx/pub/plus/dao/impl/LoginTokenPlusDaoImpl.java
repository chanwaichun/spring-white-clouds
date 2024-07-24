package com.byx.pub.plus.dao.impl;

import com.byx.pub.plus.entity.LoginToken;
import com.byx.pub.plus.mapper.LoginTokenPlusMapper;
import com.byx.pub.plus.dao.LoginTokenPlusDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录token表 服务实现类
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Service
public class LoginTokenPlusDaoImpl extends ServiceImpl<LoginTokenPlusMapper, LoginToken> implements LoginTokenPlusDao {

}
