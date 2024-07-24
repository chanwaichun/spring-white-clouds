package com.byx.pub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.byx.pub.bean.qo.UpdateUserInfoQo;
import com.byx.pub.bean.vo.FrontLoginUserLogVo;
import com.byx.pub.bean.vo.LoginHeadBean;
import com.byx.pub.bean.vo.UserLoginVo;
import com.byx.pub.enums.ClientTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.UserPlusDao;
import com.byx.pub.plus.entity.Admin;
import com.byx.pub.plus.entity.AdminCard;
import com.byx.pub.plus.entity.User;
import com.byx.pub.service.*;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jump
 * @date 2023/5/23 17:34:10
 */
@Slf4j
@Service
public class FrontUserServiceImpl implements FrontUserService {
    @Resource
    UserPlusDao userPlusDao;
    @Resource
    WeChatApiService weChatApiService;
    @Resource
    AdminService adminService;
    @Resource
    InviteService inviteService;
    @Resource
    AdminCardService cardService;
    @Resource
    RedisService redisService;

    /**
     * 小程序登录(游客)
     * @param code
     * @return
     */
    public User miniAppLogin(String code){
        FrontLoginUserLogVo logVo = new FrontLoginUserLogVo();
        //通过code换取用户openid
        Map<String, Object> weChatAuth = this.weChatApiService.weChatAuth(code);
        String openId =(String)weChatAuth.get("openid");
        String sessionKey = (String)weChatAuth.get("session_key");
        String unionId =  (String)weChatAuth.get("unionid");
        //通过openid获取用户信息 无则注册，有则返回
        User user = getUserByOpenId(openId);
        if(Objects.isNull(user)){
            user = new User().setOpenId(openId).setUnionId(unionId).setDataStatus(Boolean.TRUE)
            .setSessionKey(sessionKey).setLastLoginTime(new Date())
            .setNickName(StringUtil.getDefaultNickname());
            this.userPlusDao.save(user);
            logVo.setNewUser(Boolean.TRUE);
            logVo.setLastLoginTime(new Date());
        }else{
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setSessionKey(sessionKey);
            updateUser.setLastLoginTime(new Date());
            this.userPlusDao.updateById(updateUser);
            logVo.setLastLoginTime(user.getLastLoginTime());
            logVo.setUserType(user.getRoleId().toString());
        }
        logVo.setUserId(user.getId());
        log.info("------当前登录用户:"+ JSONObject.toJSONString(logVo));
        return userPlusDao.getById(user.getId());
    }

    /**
     * 获取用户信息(校验角色)
     * @param userId
     * @return
     */
    public UserLoginVo getUserInfo(String userId){
        UserLoginVo resVo = new UserLoginVo();
        User user = this.userPlusDao.getById(userId);
        if(Objects.isNull(user) || !user.getDataStatus()) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户信息已失效");
        }
        BeanUtils.copyProperties(user,resVo);
        resVo.setUserId(userId);
        resVo.setRoleType(user.getRoleId());
        if(StringUtils.isEmpty(user.getMobile())){
            return resVo;
        }
        //判断是否咨询师 查询管理后台是否存在
        Admin admin = this.adminService.getAdminByMobile(user.getMobile());
        if(Objects.isNull(admin)){
            //修改为用户
            if(!RoleTypeEnum.FRONT_CUSTOM.getValue().equals(user.getRoleId().toString())){
                this.userPlusDao.updateById(new User()
                        .setId(userId)
                        .setRoleId(Integer.parseInt(RoleTypeEnum.FRONT_CUSTOM.getValue()))
                        .setBusinessId("")
                        .setAdminId(""));
            }
            resVo.setRoleType(Integer.parseInt(RoleTypeEnum.FRONT_CUSTOM.getValue()));
            resVo.setBusinessId(SystemFinalCode.ZERO_STR);
            resVo.setIsBusiness(Boolean.FALSE);
            resVo.setAdminId("");
        }else {
            //修改为对应的角色
            if(RoleTypeEnum.FRONT_CUSTOM.getValue().equals(user.getRoleId().toString())){
                this.userPlusDao.updateById(new User()
                        .setId(userId).setRoleId(Integer.parseInt(admin.getRoleId()))
                        .setBusinessId(admin.getBusinessId())
                        .setAdminId(admin.getAdminId()));
            }
            resVo.setRoleType(Integer.parseInt(admin.getRoleId()));
            resVo.setIsBusiness(Boolean.TRUE);
            //是否平台用户
            if(StringUtil.isPlatformAdmin(admin.getRoleId())){
                resVo.setAdminId(SystemFinalCode.PLATFORM_BUSINESS_ID);
                resVo.setBusinessId(SystemFinalCode.PLATFORM_BUSINESS_ID);
            }else{
                resVo.setAdminId(admin.getAdminId());
                resVo.setBusinessId(admin.getBusinessId());
            }
            //同步名片昵称
            AdminCard card = cardService.getCardByAdminId(admin.getAdminId(), admin.getBusinessId());
            if(Objects.nonNull(card) && !card.getNickName().equals(user.getNickName())){
                this.cardService.updateCard(new AdminCard().setId(card.getId()).setNickName(user.getNickName()));
            }

            //查询是否有被邀请加入系统的 如果是给邀请者算提成
            /*if(!user.getInviteStatus()){
                inviteService.changeInviteRecord(userId);
            }*/
        }
        return resVo;
    }

    /**
     * 根据openid获取用户
     * @param openId
     * @return
     */
    public User getUserByOpenId(String openId){
       return this.userPlusDao.lambdaQuery().eq(User::getOpenId,openId).eq(User::getDataStatus,Boolean.TRUE).last(" limit 1 ").one();
    }

    /**
     * 授权微信手机号
     * @param userId
     * @param iv
     * @param encryptedData
     * @return
     */
    public UserLoginVo authWxMobile(String userId, String iv, String encryptedData){
        User user = this.userPlusDao.getById(userId);
        if(Objects.isNull(user) || !user.getDataStatus()) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户信息已失效");
        }
        if(StringUtil.notEmpty(user.getMobile())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户已绑定手机号");
        }
        String mobile = this.weChatApiService.getWxMobile(iv,encryptedData,user.getSessionKey());
        //查询手机是否存在(新用户)
        User userByMobile = this.getUserByMobile(mobile);
        if(Objects.isNull(userByMobile)){
            User updateBean = new User().setId(userId).setMobile(mobile);
            this.userPlusDao.updateById(updateBean);
            return this.getUserInfo(userId);
        }
        //小红书用户(合并帐号：删除当前userId 用户对象)
        User updateBean = new User();
        BeanUtils.copyProperties(userByMobile,updateBean);
        updateBean.setOpenId(user.getOpenId()).setUnionId(user.getUnionId())
                .setSessionKey(user.getSessionKey()).setUserImg(user.getUserImg())
                .setGender(user.getGender()).setLastLoginTime(new Date());
        this.userPlusDao.updateById(updateBean);
        //删除当前 用户 和token
        this.userPlusDao.removeById(userId);
        redisService.delToken(userId);
        //返回新用户数据
        return this.getUserInfo(updateBean.getId());
    }

    /**
     * 用户设置头像等信息
     * @param userId
     * @param qo
     * @return
     */
    public UserLoginVo updateUserInfo(String userId, UpdateUserInfoQo qo){
        User user = this.userPlusDao.getById(userId);
        if(Objects.isNull(user) || !user.getDataStatus()) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户信息已失效");
        }
        //如果是商家
        if(StringUtil.notEmpty(user.getAdminId())
                && StringUtil.isBusiness(user.getRoleId().toString())){
            //同步更改 商家表的头像 和 昵称
            Admin admin = this.adminService.getAdminById(user.getAdminId());
            if(Objects.nonNull(admin)){
                if(StringUtil.notEmpty(qo.getNickName()) || StringUtil.notEmpty(qo.getUserImg())){
                    Admin updateBean = new Admin();
                    updateBean.setAdminId(admin.getAdminId());
                    if(StringUtil.notEmpty(qo.getNickName())){
                        updateBean.setNickName(qo.getNickName());
                    }
                    if(StringUtil.notEmpty(qo.getUserImg())){
                        updateBean.setUserImg(qo.getUserImg());
                    }
                    this.adminService.updateAdmin(updateBean);
                }
            }
            //同步更改 名片 昵称、头像
            AdminCard card = cardService.getCardByAdminId(user.getAdminId(), user.getBusinessId());
            if(Objects.nonNull(card)){
                if(StringUtil.notEmpty(qo.getNickName()) || StringUtil.notEmpty(qo.getUserImg())){
                    AdminCard updateCard = new AdminCard();
                    updateCard.setId(card.getId());
                    if(StringUtil.notEmpty(qo.getNickName())){
                        updateCard.setNickName(qo.getNickName());
                    }
                    if(StringUtil.notEmpty(qo.getUserImg())){
                        updateCard.setUserImg(qo.getUserImg());
                    }
                    this.cardService.updateCard(updateCard);
                }
            }
        }
        User updateUser = new User();
        BeanUtils.copyProperties(qo,updateUser);
        updateUser.setId(userId);
        this.userPlusDao.updateById(updateUser);
        return this.getUserInfo(userId);
    }

    /**
     * 修改用户信息
     * @param updateUser
     */
    public void updateUser(User updateUser){
        this.userPlusDao.updateById(updateUser);
    }

    /**
     * 根据手机号获取用户信息
     * @param mobile
     * @return
     */
    public User getUserByMobile(String mobile){
        return this.userPlusDao.lambdaQuery().eq(User::getDataStatus,Boolean.TRUE).eq(User::getMobile,mobile).last("limit 1").one();
    }

    /**
     * 根据adminId查询用户
     * @param adminId
     * @return
     */
    public User getUserByAdminId(String adminId){
        return this.userPlusDao.lambdaQuery().eq(User::getDataStatus,Boolean.TRUE).eq(User::getAdminId,adminId).last("limit 1").one();
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public User getUserById(String userId){
        return this.userPlusDao.getById(userId);
    }

}
