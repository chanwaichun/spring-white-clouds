package com.byx.pub.service;

import com.byx.pub.bean.qo.UpdateUserInfoQo;
import com.byx.pub.bean.vo.UserLoginVo;
import com.byx.pub.plus.entity.User;

/**
 * @author Jump
 * @date 2023/5/23 17:57:26
 */
public interface FrontUserService {

    /**
     * 小程序登录(游客)
     * @param code
     * @return
     */
    User miniAppLogin(String code);

    /**
     * 获取用户信息(校验角色)
     * @param userId
     * @return
     */
    UserLoginVo getUserInfo(String userId);

    /**
     * 授权微信手机号
     * @param userId
     * @param iv
     * @param encryptedData
     * @return
     */
    UserLoginVo authWxMobile(String userId, String iv, String encryptedData);

    /**
     * 用户设置头像等信息
     * @param userId
     * @param qo
     * @return
     */
    UserLoginVo updateUserInfo(String userId, UpdateUserInfoQo qo);

    /**
     * 修改用户信息
     * @param updateUser
     */
    void updateUser(User updateUser);

    /**
     * 根据手机号获取用户信息
     * @param mobile
     * @return
     */
    User getUserByMobile(String mobile);

    /**
     * 根据adminId查询用户
     * @param adminId
     * @return
     */
    User getUserByAdminId(String adminId);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    User getUserById(String userId);
}
