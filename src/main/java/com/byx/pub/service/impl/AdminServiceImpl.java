package com.byx.pub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.crop.OauthDto;
import com.byx.pub.bean.qo.AddOrUpdateAdminQo;
import com.byx.pub.bean.qo.PageListAdminQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.*;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.MD5;
import com.byx.pub.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jump
 * @date 2023/5/7 14:20:16
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    AdminPlusDao adminPlusDao;
    @Resource
    AdminRolePlusDao rolePlusDao;
    @Resource
    WeChatApiService weChatApiService;
    @Resource
    RedisService redisService;
    @Resource
    TempAdminPlusDao tempAdminPlusDao;
    @Value("${token.effective.admin}")
    private Long tokenSecond;
    @Resource
    FrontUserService userService;
    @Resource
    BusinessService businessService;
    @Resource
    AdminCardService cardService;
    @Resource
    CropWeChatCallBackService cropWeChatCallBackService;


    /**
     * 保存或修改帐号
     * @param addOrUpdateAdminQo
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateAdmin(AddOrUpdateAdminQo addOrUpdateAdminQo){
        //获取角色信息
        AdminRole role = this.rolePlusDao.getById(addOrUpdateAdminQo.getRoleId());
        if(Objects.isNull(role) || !role.getStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"暂无该角色,或角色已停用");
        }
        //根据手机号查询
        Admin adminByMobile = this.getAdminByMobile(addOrUpdateAdminQo.getMobile());
        //新增
        if(StringUtil.isEmpty(addOrUpdateAdminQo.getAdminId())){
            if(Objects.nonNull(adminByMobile) && adminByMobile.getDataStatus()){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"该账号已被注册");
            }
            Admin admin = new Admin();
            //商家校验商家账户数量
            if(!StringUtil.isPlatformAdmin(addOrUpdateAdminQo.getRoleId())){
                chekSjAccountNum(addOrUpdateAdminQo.getBusinessId());
                admin.setBusinessId(addOrUpdateAdminQo.getBusinessId());
            }
            admin.setTrueName(addOrUpdateAdminQo.getTrueName());
            //角色id
            admin.setRoleId(addOrUpdateAdminQo.getRoleId());
            admin.setMobile(addOrUpdateAdminQo.getMobile());
            admin.setUserImg(addOrUpdateAdminQo.getUserImg());
            //md5密码
            if(StringUtil.isEmpty(addOrUpdateAdminQo.getPassword())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请输入密码");
            }
            admin.setUserPassword(MD5.md5(addOrUpdateAdminQo.getPassword()));
            admin.setUserStatus(Boolean.TRUE);
            this.adminPlusDao.save(admin);
        }else{//修改 如果更改手机号 则需要同步更改 小程序端 手机号
            Admin adminById = this.adminPlusDao.getById(addOrUpdateAdminQo.getAdminId());
            if(Objects.isNull(adminById)){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到用户信息");
            }
            //校验手机号是否被占用
            if(Objects.nonNull(adminByMobile)  && adminByMobile.getDataStatus()
                    && !adminByMobile.getAdminId().equals(adminById.getAdminId())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"该账号已被注册");
            }
            Admin updateBean = new Admin()
                    .setAdminId(addOrUpdateAdminQo.getAdminId())
                    .setRoleId(addOrUpdateAdminQo.getRoleId())
                    .setMobile(addOrUpdateAdminQo.getMobile())
                    .setUserImg(addOrUpdateAdminQo.getUserImg())
                    .setTrueName(addOrUpdateAdminQo.getTrueName());
            //如果 密码填写了 且 加密后不等于源数据库的 则 更改密码
            if(StringUtil.notEmpty(addOrUpdateAdminQo.getPassword())
                    && (!MD5.md5(addOrUpdateAdminQo.getPassword()).equals(adminById.getUserPassword()))
            ){
                updateBean.setUserPassword(MD5.md5(addOrUpdateAdminQo.getPassword()));
            }
            this.adminPlusDao.updateById(updateBean);
            //被修改的用户要重新登录
            this.redisService.delToken(addOrUpdateAdminQo.getAdminId());

            //同步更新客户端头像和昵称
            User userByMobile = this.userService.getUserByMobile(adminById.getMobile());
            if(Objects.isNull(userByMobile)){
                return;
            }
            User updateUser = new User()
                    .setId(userByMobile.getId())
                    .setUserImg(addOrUpdateAdminQo.getUserImg())
                    .setMobile(addOrUpdateAdminQo.getMobile());
            userService.updateUser(updateUser);
            //同步修改名片表 姓名
            AdminCard cardByAdminId = this.cardService.getCardByAdminId(adminById.getAdminId(),addOrUpdateAdminQo.getBusinessId());
            if(Objects.nonNull(cardByAdminId)){
                this.cardService.updateCard(new AdminCard()
                        .setId(cardByAdminId.getId())
                        .setTrueName(addOrUpdateAdminQo.getTrueName())
                );
            }
        }
    }

    /**
     * 删除用户
     * 删除咨询师需要把关联user恢复到用户角色
     * @param adminId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delAdmin(String adminId){
        Admin byId = this.adminPlusDao.getById(adminId);
        if(Objects.isNull(byId)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到账户信息");
        }
        if(!byId.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户早已删除");
        }
        //判断用户是否还关联名片
        AdminCard cardByAdminId = this.cardService.getCardByAdminId(adminId, byId.getBusinessId());
        if(Objects.nonNull(cardByAdminId)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户有关联名片，请修改名片后再删除");
        }
        Admin delAdmin = new Admin().setAdminId(adminId).setUserStatus(Boolean.FALSE).setDataStatus(Boolean.FALSE);
        //删除用户
        this.adminPlusDao.updateById(delAdmin);
        //设置临时表为未绑定
        TempAdmin tempAdmin = getByOpenId(byId.getOpenId());
        if(Objects.nonNull(tempAdmin)){
            this.tempAdminPlusDao.updateById(new TempAdmin().setId(tempAdmin.getId()).setBandStatus(Boolean.FALSE));
        }
        //设置user表为用户
        User userByAdminId = this.userService.getUserByAdminId(adminId);
        if(Objects.nonNull(userByAdminId)){
            User updateUser = new User();
            updateUser.setId(userByAdminId.getId()).setAdminId("")
                    .setRoleId(Integer.parseInt(RoleTypeEnum.FRONT_CUSTOM.getValue()))
                    .setBusinessId("");
            this.userService.updateUser(updateUser);
            //删除前端token
            this.redisService.delToken(userByAdminId.getId());
        }
        //删除token
        this.redisService.delToken(adminId);
    }

    /**
     * 分页查询账户列表
     * @param qo
     * @return
     */
    public Page<AdminPageListVo> pageList(PageListAdminQo qo){
        Page<AdminPageListVo> resPage = new Page<>();
        QueryWrapper<Admin> where = new QueryWrapper<>();
        where.lambda().eq(Admin::getDataStatus,Boolean.TRUE);

        if(!CollectionUtils.isEmpty(qo.getRuleList())){
            where.lambda().in(Admin::getRoleId,qo.getRuleList());
        }
        if(StringUtil.notEmpty(qo.getBusinessId())){
            where.lambda().eq(Admin::getBusinessId,qo.getBusinessId());
        }
        if(!StringUtil.isEmpty(qo.getAdminName())){
            where.lambda().and(s->{
                return s.like(Admin::getNickName, qo.getAdminName())
                        .or().like(Admin::getTrueName,qo.getAdminName());
            });
        }
        if(!StringUtil.isEmpty(qo.getMobile())){
            where.lambda().like(Admin::getMobile,qo.getMobile());
        }
        where.lambda().orderByDesc(Admin::getCreateTime);
        IPage<Admin> page = this.adminPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        List<AdminPageListVo> resList = new ArrayList<>();
        for(Admin admin : page.getRecords()){
            AdminPageListVo vo = new AdminPageListVo();
            BeanUtils.copyProperties(admin,vo);
            if(StringUtil.notEmpty(admin.getBusinessId())){
                Business business = this.businessService.getBusinessById(admin.getBusinessId());
                if(Objects.nonNull(business)){
                    vo.setShortName(business.getShortName());
                }
            }
            resList.add(vo);
        }
        resPage.setRecords(resList);
        return resPage;
    }

    /**
     * 获取登录用户信息
     * @param adminId
     * @param role
     * @return
     */
    public LoginAdminVo getAdminInfo(String adminId,String role){
        LoginAdminVo resVo = new LoginAdminVo();
        //如果是临时用户
        if("TEMP".equals(role)){
            TempAdmin tempAdmin = this.tempAdminPlusDao.getById(adminId);
            if(Objects.isNull(tempAdmin)){
                throw new ApiException(ResultCode.FAILED.getCode(),"用户信息错误,请联系管理员");
            }
            BeanUtils.copyProperties(tempAdmin,resVo);
            resVo.setRoleId("0");
            return resVo;
        }
        //正常用户
        Admin admin = this.adminPlusDao.getById(adminId);
        if(Objects.isNull(admin) || !admin.getDataStatus() || !admin.getUserStatus()){
            throw new ApiException(ResultCode.FAILED.getCode(),"用户已失效,请联系管理员");
        }
        BeanUtils.copyProperties(admin,resVo);
        //获取商家信息
        PageBusinessVo sjInfo = new PageBusinessVo()
                .setId(SystemFinalCode.PLATFORM_BUSINESS_ID)
                .setShortName(SystemFinalCode.PLATFORM_BUSINESS_NAME);
        if(!StringUtil.isPlatformAdmin(admin.getRoleId())){
            sjInfo = this.businessService.getSjInfo(admin.getBusinessId());
        }
        resVo.setBusinessId(admin.getBusinessId());
        resVo.setBusinessName(sjInfo.getShortName());
        return resVo;
    }


    /**
     * 根据手机号码获取管理后台帐号
     * @param mobile
     * @return
     */
    public Admin getAdminByMobile(String mobile){
        return this.adminPlusDao.lambdaQuery()
                .eq(Admin::getDataStatus,Boolean.TRUE)
                .eq(Admin::getMobile, mobile).last("limit 1").one();
    }

    /**
     * 修改咨询师信息
     * @param updateBean
     */
    public void updateAdmin(Admin updateBean){
        this.adminPlusDao.updateById(updateBean);
    }


    /**
     * 扫码登录微信回调接口
     * 如果用户第一次登录：
     * 1.用户临时表生成一条数据。
     * 2.角色为TEMP用户id为临时表id(openid)生成token返回前端。
     * 3.前端调用接口获取用户信息。
     * 4.根据不同的信息判断跳转不同页面。
     * @param code
     * @param state
     * @return
     */
    public String callBack(String code, String state){
        String token = "";
        //判断用户是否授权
        if(StringUtils.isEmpty(code)){
            return token;
        }
        //通过code获取微信用户信息。
        WeChatUserVo weChatUserInfo = this.weChatApiService.getWeChatUserInfo(code, state);
        if(Objects.isNull(weChatUserInfo)){
            return token;
        }
        //判断用户是否绑定账
        TempAdmin tempAdmin = getByOpenId(weChatUserInfo.getOpenId());
        //全新用户
        if(Objects.isNull(tempAdmin)){
            //保存临时表
            TempAdmin addAdmin = new TempAdmin().setOpenId(weChatUserInfo.getOpenId()).setBandStatus(Boolean.FALSE)
                    .setUnionId(weChatUserInfo.getUnionId()).setGender(weChatUserInfo.getUserSex())
                    .setNickName(weChatUserInfo.getNickName()).setUserImg(weChatUserInfo.getUserImg());
            this.tempAdminPlusDao.save(addAdmin);
            //生成token并返回 
            LoginHeadBean loginHeadBean = new LoginHeadBean().setAdminId(addAdmin.getId()).setAdminRole("TEMP").setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
            return this.redisService.setLoginToken(loginHeadBean);
        }
        //绑定账户 设置数据返回
        if(tempAdmin.getBandStatus()){
            //正常用户直接登录
            Admin adminByOpenId = this.getAdminByOpenId(weChatUserInfo.getOpenId());
            if(Objects.isNull(adminByOpenId)){
                throw new ApiException("用户信息错误,请联系管理员");
            }
            //更新最新的用户昵称和头像等
            Admin updateBean = new Admin().setAdminId(adminByOpenId.getAdminId()).setGender(weChatUserInfo.getUserSex())
                    .setNickName(weChatUserInfo.getNickName()).setUserImg(weChatUserInfo.getUserImg());
            this.adminPlusDao.updateById(updateBean);
            //获取商家信息
            PageBusinessVo sjInfo = new PageBusinessVo()
                    .setId(SystemFinalCode.PLATFORM_BUSINESS_ID)
                    .setShortName(SystemFinalCode.PLATFORM_BUSINESS_NAME);
            if(!StringUtil.isPlatformAdmin(adminByOpenId.getRoleId())){
                sjInfo = this.businessService.getSjInfo(adminByOpenId.getBusinessId());
            }
            //设置返回参数
            LoginHeadBean loginHeadBean = new LoginHeadBean()
                    .setBusinessId(sjInfo.getId())
                    .setBusinessName(sjInfo.getShortName())
                    .setAdminId(adminByOpenId.getAdminId())
                    .setAdminRole(adminByOpenId.getRoleId())
                    .setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
            return this.redisService.setLoginToken(loginHeadBean);
        }
        //未绑定用户
        LoginHeadBean loginHeadBean = new LoginHeadBean().setAdminId(tempAdmin.getId()).setAdminRole("TEMP").setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
        return this.redisService.setLoginToken(loginHeadBean);
    }

    /**
     * 根据openId获取用户
     * @param openId
     * @return
     */
    public Admin getAdminByOpenId(String openId){
        return this.adminPlusDao.lambdaQuery().eq(Admin::getOpenId,openId)
                .eq(Admin::getDataStatus,Boolean.TRUE).last(" limit 1 ").one();
    }

    /**
     * 帐号密码登录
     * @param mobile
     * @param password
     * @return
     */
    public String accountLogin(String mobile,String password){
        Admin adminByMobile = this.getAdminByMobile(mobile);
        if(Objects.isNull(adminByMobile) || !adminByMobile.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户未创建，请联系管理员");
        }
        if(!adminByMobile.getUserStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户未启用，请联系管理员");
        }
        if(!MD5.md5(password).equals(adminByMobile.getUserPassword())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"密码错误，请重试");
        }
        PageBusinessVo sjInfo = new PageBusinessVo()
                .setId(SystemFinalCode.PLATFORM_BUSINESS_ID)
                .setShortName(SystemFinalCode.PLATFORM_BUSINESS_NAME);
        //如果不是平台管理员则需要获取商家信息
        if(!StringUtil.isPlatformAdmin(adminByMobile.getRoleId())){
            sjInfo = this.businessService.getSjInfo(adminByMobile.getBusinessId());
        }
        //设置返回参数
        LoginHeadBean loginHeadBean = new LoginHeadBean()
                .setAdminId(adminByMobile.getAdminId())
                .setBusinessId(sjInfo.getId())
                .setBusinessName(sjInfo.getShortName())
                .setAdminRole(adminByMobile.getRoleId())
                .setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
        return this.redisService.setLoginToken(loginHeadBean);
    }

    /**
     * 绑定账户
     * @param tempAdminId
     * @param mobile
     * @param password
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginAdminVo bandAdmin(String tempAdminId, String mobile, String password, String xToken){
        TempAdmin tempAdmin = this.tempAdminPlusDao.getById(tempAdminId);
        if(Objects.isNull(tempAdmin)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到用户临时信息");
        }
        if(tempAdmin.getBandStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"该用户已绑定手机号");
        }
        Admin adminByMobile = this.getAdminByMobile(mobile);
        if(Objects.isNull(adminByMobile) || !adminByMobile.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户未创建，请联系管理员");
        }
        if(StringUtils.isNotEmpty(adminByMobile.getOpenId())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户已被绑定，请联系管理员");
        }
        if(!MD5.md5(password).equals(adminByMobile.getUserPassword())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"密码错误，请联系管理员");
        }
        //如果是平台用户不需要校验商家信息
        PageBusinessVo sjInfo = new PageBusinessVo()
                .setId(SystemFinalCode.PLATFORM_BUSINESS_ID).setShortName(SystemFinalCode.PLATFORM_BUSINESS_NAME);
        if(!StringUtil.isPlatformAdmin(adminByMobile.getRoleId())){
            sjInfo = this.businessService.getSjInfo(adminByMobile.getBusinessId());
        }
        //绑定账户信息
        Admin bandAdmin = new Admin();
        BeanUtils.copyProperties(tempAdmin,bandAdmin);
        bandAdmin.setAdminId(adminByMobile.getAdminId());
        this.adminPlusDao.updateById(bandAdmin);
        //更改临时表为绑定
        TempAdmin updateTemp = new TempAdmin().setId(tempAdmin.getId()).setBandStatus(Boolean.TRUE);
        this.tempAdminPlusDao.updateById(updateTemp);
        //重新设置token附带信息
        LoginHeadBean bean = new LoginHeadBean()
                .setAdminId(adminByMobile.getAdminId())
                .setAdminRole(adminByMobile.getRoleId())
                .setBusinessId(sjInfo.getId())
                .setBusinessName(sjInfo.getShortName())
                .setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
        this.redisService.set(xToken,JSONObject.toJSONString(bean),tokenSecond);
        //返回信息
        return getLoginAdminInfoByMobile(mobile);
    }


    /**
     * 根据手机号获取用户信息
     * @param mobile
     * @return
     */
    public LoginAdminVo getLoginAdminInfoByMobile(String mobile){
        LoginAdminVo resVo = new LoginAdminVo();
        Admin adminByMobile = this.getAdminByMobile(mobile);
        if(Objects.isNull(adminByMobile) || !adminByMobile.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"账户失效，请联系管理员");
        }
        BeanUtils.copyProperties(adminByMobile,resVo);

        return resVo;
    }


    /**
     * 获取所有咨询师账户(有效的)
     * @return
     */
    public List<Admin> getAllConsultant(){
        return adminPlusDao.lambdaQuery().eq(Admin::getRoleId,2).
                eq(Admin::getDataStatus,Boolean.TRUE).eq(Admin::getUserStatus,Boolean.TRUE).list();
    }

    /**
     * 根据openId获取临时表数据
     * @param openId
     * @return
     */
    public TempAdmin getByOpenId(String openId){
        return this.tempAdminPlusDao.lambdaQuery().eq(TempAdmin::getOpenId,openId).last("limit 1").one();
    }

    public TempAdmin getByCropOpenId(String cropOpenId){
        return this.tempAdminPlusDao.lambdaQuery().eq(TempAdmin::getCropOpenUserid,cropOpenId).last("limit 1").one();
    }


    /**
     * 获取账户详情
     * @param id
     * @return
     */
    public Admin getAdminDetail(String id){
        Admin adminById = this.adminPlusDao.getById(id);
        if(Objects.isNull(adminById) || !adminById.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到用户信息");
        }
        adminById.setUserPassword("");
        return adminById;
    }

    /**
     * 管理后台前端选择咨询师列表
     * @param roleId
     * @param adminId
     * @return
     */
    public List<SelectAdminListVo> getAdminListByRole(String roleId,String adminId){
        List<SelectAdminListVo> resList = new ArrayList<>();
        //1.咨询师 直 接返回
        if(StringUtil.isBusiness(roleId)){
            Admin adminPlusDaoById = this.adminPlusDao.getById(adminId);
            if(Objects.isNull(adminPlusDaoById)){
                throw new ApiException(ResultCode.VALIDATE_FAILED);
            }
            SelectAdminListVo resVo = new SelectAdminListVo();
            resList.add(resVo.setAdminId(adminId).setTrueName(adminPlusDaoById.getTrueName()));
            return resList;
        }
        //2.管理员 返回所有
        List<Admin> allConsultant = this.getAllConsultant();
        for(Admin admin : allConsultant){
            SelectAdminListVo resVo = new SelectAdminListVo();
            resList.add(resVo.setAdminId(admin.getAdminId()).setTrueName(admin.getTrueName()));
        }
        return resList;
    }

    /**
     * 校验 商家套餐能 办理的帐号数量
     * 个人版 1个
     * 小组版 20个
     * 团队版100个
     * @param sjId
     */
    public void chekSjAccountNum(String sjId){
        PageBusinessVo sjInfo = this.businessService.getSjInfo(sjId);
        List<Admin> adminList = this.getAdminListBySj(sjId);
        //个人版只能1个
        if(SuitTypeEnum.PERSON.getValue().equals(sjInfo.getSuitType())
                && adminList.size() >= SystemFinalCode.ONE_VALUE){
            throw new ApiException("个人版只能创建1个账号");
        }
        //2023-10-18 团队版和小组版一样权限

        //小组版只能20个
        if(SuitTypeEnum.GROUP.getValue().equals(sjInfo.getSuitType())
                && adminList.size() >= SystemFinalCode.TWENTY_VALUE){
            throw new ApiException("小组版最多只能创建20个账号");
        }
        //团队版只能20个
        if(SuitTypeEnum.TEAM.getValue().equals(sjInfo.getSuitType())
                && adminList.size() >= SystemFinalCode.TWENTY_VALUE){
            throw new ApiException("团队版最多只能创建20个账号");
        }
    }


    /**
     * 根据商家id获取帐号列表
     * @param sjId
     * @return
     */
    public List<Admin> getAdminListBySj(String sjId){
        return this.adminPlusDao.lambdaQuery()
                .eq(Admin::getDataStatus,Boolean.TRUE)
                .eq(Admin::getBusinessId,sjId)
                .orderByDesc(Admin::getCreateTime)
                .orderByAsc(Admin::getRoleId).list();
    }

    /**
     * 查询商家有授权小红书店铺的咨询师
     * @param sjId
     * @return
     */
    public List<Admin> getXhsAdminListBySj(String sjId){
        return this.adminPlusDao.lambdaQuery()
                .eq(Admin::getDataStatus,Boolean.TRUE)
                .eq(Admin::getBusinessId,sjId)
                .ne(Admin::getXhsSellerId,"")
                .list();
    }


    /**
     * 根据id查询admin
     * @param adminId
     * @return
     */
    public Admin getAdminById(String adminId){
        Admin admin = this.adminPlusDao.getById(adminId);
        if(Objects.isNull(admin) || !admin.getDataStatus()){
            return null;
        }
        return admin;
    }

    /**
     * 根据角色分页查询商家下的咨询师
     * @param sjId
     * @param roleIds
     * @param adminName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<AdminPageListVo> getSjAdminByRole(String sjId,List<Integer> roleIds,String adminName,Integer pageNum,Integer pageSize){
        PageListAdminQo qo = new PageListAdminQo()
                .setBusinessId(sjId).setRuleList(roleIds).setAdminName(adminName)
                .setPageNum(pageNum).setPageSize(pageSize);
        return this.pageList(qo);
    }
    /**
     * 获取商家管理员信息
     * @param sjId
     * @return
     */
    public Admin getBusinessMangeAdmin(String sjId){
        return this.adminPlusDao.lambdaQuery().eq(Admin::getBusinessId, sjId)
                .eq(Admin::getDataStatus, Boolean.TRUE)
                .eq(Admin::getRoleId, RoleTypeEnum.SJ_MANEGE.getValue())
                .last("limit 1").one();
    }

    /**
     * 根据企业微信id获取用户信息
     * @param userOpenId
     * @return
     */
    public Admin getByCropUserId(String userOpenId){
        return this.adminPlusDao.lambdaQuery().eq(Admin::getCropOpenUserid, userOpenId).last("limit 1").one();
    }

    /**
     * 企业微信登录
     * @param code
     * @param state
     * @return
     */
    public String cropAuth(String code, String state){
        String token = "";
        //判断用户是否授权
        if(StringUtils.isEmpty(code)){
            return token;
        }
        //通过企业微信code获取用户id
        OauthDto oauthDto = cropWeChatCallBackService.oauth(code, state);
        if(StringUtil.isEmpty(oauthDto.getOpenUserId())){
            return oauthDto.getResStr();
        }
        //判断用户是否绑定账
        TempAdmin tempAdmin = getByCropOpenId(oauthDto.getOpenUserId());
        //全新用户
        if(Objects.isNull(tempAdmin)){
            //保存临时表
            TempAdmin addAdmin = new TempAdmin()
                    .setCropOpenUserid(oauthDto.getOpenUserId())
                    .setBandStatus(Boolean.FALSE)
                    .setCropId(oauthDto.getAuthCorpId());
            this.tempAdminPlusDao.save(addAdmin);
            //生成token并返回
            LoginHeadBean loginHeadBean = new LoginHeadBean().setAdminId(addAdmin.getId()).setAdminRole("TEMP").setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
            return this.redisService.setLoginToken(loginHeadBean);
        }
        //绑定账户 设置数据返回
        if(tempAdmin.getBandStatus()){
            //正常用户直接登录
            Admin cropAdmin = this.getByCropUserId(oauthDto.getOpenUserId());
            if(Objects.isNull(cropAdmin)){
                throw new ApiException("用户信息错误,请联系管理员");
            }
            //获取商家信息
            PageBusinessVo sjInfo = new PageBusinessVo()
                    .setId(SystemFinalCode.PLATFORM_BUSINESS_ID)
                    .setShortName(SystemFinalCode.PLATFORM_BUSINESS_NAME);
            if(!StringUtil.isPlatformAdmin(cropAdmin.getRoleId())){
                sjInfo = this.businessService.getSjInfo(cropAdmin.getBusinessId());
            }
            //设置返回参数
            LoginHeadBean loginHeadBean = new LoginHeadBean()
                    .setBusinessId(sjInfo.getId())
                    .setBusinessName(sjInfo.getShortName())
                    .setAdminId(cropAdmin.getAdminId())
                    .setAdminRole(cropAdmin.getRoleId())
                    .setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
            return this.redisService.setLoginToken(loginHeadBean);
        }
        //未绑定用户
        LoginHeadBean loginHeadBean = new LoginHeadBean().setAdminId(tempAdmin.getId()).setAdminRole("TEMP").setClientType(ClientTypeEnum.BYX_MANEGE.getValue());
        return this.redisService.setLoginToken(loginHeadBean);
    }


    /**
     * 获取首页咨询师登录信息
     * @param adminId
     * @return
     */
    public AdminHomePageVo getAdminHomePageVo(String adminId){
        AdminHomePageVo resVo = new AdminHomePageVo();
        Admin admin = getAdminById(adminId);
        if(Objects.isNull(admin)){
            return resVo;
        }
        PageBusinessVo sjInfo = this.businessService.getSjInfo(admin.getBusinessId());
        BeanUtils.copyProperties(sjInfo,resVo);
        resVo.setAdminId(adminId).setTrueName(admin.getTrueName()).setUserImg(admin.getUserImg());
        return resVo;
    }

}
