package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddOrUpdateMemberGroupBo;
import com.byx.pub.bean.bo.AddOrUpdateMemberRuleBo;
import com.byx.pub.bean.bo.AddWhitelistMemberBo;
import com.byx.pub.bean.qo.PageAdminUserQo;
import com.byx.pub.bean.qo.PageListGroupMemberQo;
import com.byx.pub.bean.qo.PageListUserPortraitQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.filter.RedisLock;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 咨询师会员服务
 * @author Jump
 * @date 2023/6/14 15:10:09
 */
@Service
@Slf4j
public class BusinessUserServiceImpl implements BusinessUserService {
    @Resource
    BusinessUserPlusDao businessUserPlusDao;
    @Resource
    UserPlusDao userPlusDao;
    @Resource
    MemberGroupPlusDao groupPlusDao;
    @Resource
    MemberGroupRulePlusDao rulePlusDao;
    @Resource
    MemberGroupBlacklistPlusDao blacklistPlusDao;
    @Resource
    MemberGroupRelationPlusDao relationPlusDao;
    @Resource
    CommonAsyncService asyncService;
    @Resource
    InteractiveClickLogPlusDao clickLogPlusDao;
    @Resource
    ProductService productService;
    @Resource
    FlyerInfoService flyerInfoService;
    @Resource
    AdminCardService adminCardService;
    @Resource
    AdminService adminService;
    @Resource
    OrderDetailPlusDao detailPlusDao;
    @Resource
    OrdersPlusDao ordersPlusDao;

    /**
     * 分页条件查询客户列表
     * @param qo
     * @return
     */
    @Override
    public Page<PageBusinessUserVo> pageList(PageAdminUserQo qo){
        Page<PageBusinessUserVo> resPage = new Page<>(qo.getPageNum(),qo.getPageNum());
        QueryWrapper<BusinessUser> where = new QueryWrapper<>();
        if(StringUtil.notEmpty(qo.getBusinessId())){
            where.lambda().eq(BusinessUser::getBusinessId,qo.getBusinessId());
        }
        if(StringUtil.notEmpty(qo.getUserMobile())){
            where.lambda().like(BusinessUser::getMobile,qo.getUserMobile());
        }
        if(StringUtil.notEmpty(qo.getNickName())){
            where.lambda().like(BusinessUser::getNickName,qo.getNickName());
        }
        where.lambda().orderByDesc(BusinessUser::getCreateTime);
        IPage<BusinessUser> page = this.businessUserPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        if(0 == page.getTotal()){
            return resPage;
        }
        BeanUtils.copyProperties(page,resPage);
        List<PageBusinessUserVo> resList = new ArrayList<>();
        for(BusinessUser adminUser : page.getRecords()){
            User user = userPlusDao.getById(adminUser.getUserId());
            if(Objects.isNull(user)){
                continue;
            }
            PageBusinessUserVo resVo = new PageBusinessUserVo();
            BeanUtils.copyProperties(adminUser,resVo);
            resVo.setNickName(user.getNickName());
            resVo.setUserImg(user.getUserImg());
            resVo.setMobile(user.getMobile());
            resList.add(resVo);
        }
        resPage.setRecords(resList);
        return resPage;
    }


    /**
     * 获取商家用户统计数据
     * @param sjId
     * @return
     */
    public BusinessUserCountVo sjUserCount(String sjId){
        BusinessUserCountVo resVo = new BusinessUserCountVo();
        Integer totalNum = this.businessUserPlusDao.lambdaQuery().eq(BusinessUser::getBusinessId, sjId).count();
        resVo.setCustomNum(totalNum);
        Integer yesterdayNum = this.businessUserPlusDao.lambdaQuery().eq(BusinessUser::getBusinessId, sjId)
                .ge(BusinessUser::getCreateTime, DateUtil.getBeginDayOfYesterday())
                .le(BusinessUser::getCreateTime, DateUtil.getEndDayOfYesterDay())
                .count();
        resVo.setYesterdayAddNum(yesterdayNum);
        return resVo;
    }


    /**
     * 新增商家会员信息
     * @param adminId
     * @param bid
     * @param userId
     * @param memberType
     */
    @Async
    public void addSjMember(String adminId,String bid, String userId,Integer memberType,Boolean xhsStatus){
        User user = this.userPlusDao.getById(userId);
        if(Objects.isNull(user)){
            return;
        }
        //1.查询用户是否商家会员
        BusinessUser record = this.getRecord(bid, userId);
        //2.是则跳过，否则新增
        if(Objects.isNull(record)){
            BusinessUser addRecord = new BusinessUser().setAdminId(adminId)
                    .setUserId(userId).setNickName(user.getNickName()).setUserImg(user.getUserImg())
                    .setMobile(user.getMobile()).setUserCreateTime(user.getCreateTime())
                    .setBusinessId(bid).setUserType(memberType).setXhsStatus(xhsStatus);
            this.businessUserPlusDao.save(addRecord);
            return;
        }
    }

    /**
     * 累加会员支付金额
     * @param firstPay
     * @param bid
     * @param userId
     * @param orderAmount
     */
    @Async
    public void growMemberPayAmount(Boolean firstPay, String bid, String userId, BigDecimal orderAmount){
        //1.查询用户是否商家会员
        BusinessUser record = this.getRecord(bid, userId);
        //2.是则累加
        if(Objects.nonNull(record)){
            BusinessUser updateRecord = new BusinessUser()
                    .setId(record.getId()).setUpdateTime(new Date())
                    .setTotalAmount(record.getTotalAmount().add(orderAmount));
            //第一次支付才计入订单统计
            if(firstPay){
                updateRecord.setTotalOrderNum(record.getTotalOrderNum()+1);
            }
            this.businessUserPlusDao.updateById(updateRecord);
        }
    }

    /**
     * 获取咨询师的某个会员
     * @param sjId
     * @param userId
     * @return
     */
    public BusinessUser getRecord(String sjId, String userId){
        return this.businessUserPlusDao.lambdaQuery().eq(BusinessUser::getBusinessId,sjId)
                .eq(BusinessUser::getUserId,userId).last(" limit 1 ").one();
    }

/**************************************************************** 用户组等功能 ***********************************************************************/

    /**
     * 新增或修改用户组
     * @param groupBo
     */
    public void  addOrUpdateMemberGroup(AddOrUpdateMemberGroupBo groupBo){
        //校验数据
        if(groupBo.getRuleStatus() && CollectionUtils.isEmpty(groupBo.getRuleBoList())){
            throw new ApiException("请填写规则列表");
        }
        String groupId;
        //新增
        if(StringUtil.isEmpty(groupBo.getId())){
            //保存分组
            groupId = IdWorker.getIdStr();
            MemberGroup memberGroup = new MemberGroup();
            BeanUtils.copyProperties(groupBo,memberGroup);
            memberGroup.setId(groupId);
            this.groupPlusDao.save(memberGroup);
        }else{//修改
            groupId = groupBo.getId();
            MemberGroup updateGroup = new MemberGroup();
            updateGroup.setId(groupId).setGroupName(groupBo.getGroupName())
                    .setUpdateStatus(1).setMemberNum(0).setRuleStatus(groupBo.getRuleStatus());
            this.groupPlusDao.updateById(updateGroup);
        }
        //保存分组规则列表
        if(groupBo.getRuleStatus()){
            saveRule(groupId,groupBo.getRuleBoList());
        }
        //异步跑数据
        asyncService.runningGroupMember(groupId);
    }

    /**
     * 保存分组规则
     * @param groupId
     * @param ruleBoList
     */
    public void saveRule(String groupId,List<AddOrUpdateMemberRuleBo> ruleBoList){
        //先删除
        QueryWrapper<MemberGroupRule> delWhere = new QueryWrapper<>();
        delWhere.lambda().eq(MemberGroupRule::getGroupId,groupId);
        this.rulePlusDao.remove(delWhere);
        //再插入
        if(CollectionUtils.isEmpty(ruleBoList)){
            return;
        }
        List<MemberGroupRule> rules = new ArrayList<>(ruleBoList.size());
        for(AddOrUpdateMemberRuleBo bo : ruleBoList){
            MemberGroupRule rule = new MemberGroupRule();
            BeanUtils.copyProperties(bo,rule);
            rule.setGroupId(groupId);
            rules.add(rule);
        }
        this.rulePlusDao.saveBatch(rules);
    }

    /**
     * 查询商家会员分组
     * @param bid
     * @return
     */
    public List<MemberGroup> listMemberGroup(String bid){
        QueryWrapper<MemberGroup> where = new QueryWrapper<>();
        where.lambda().eq(MemberGroup::getDataStatus,Boolean.TRUE);
        if(StringUtil.notEmpty(bid)){
            where.lambda().eq(MemberGroup::getBusinessId,bid);
        }
        where.lambda().orderByDesc(MemberGroup::getUpdateTime);
        return this.groupPlusDao.list(where);
    }

    /**
     * 删除会员分组
     * @param id
     */
    public void delGroup(String id){
        //1.删除分组
        this.groupPlusDao.updateById(new MemberGroup().setId(id).setDataStatus(Boolean.FALSE));
        //2.删除规则
        saveRule(id,null);
        //3.删除关联
        QueryWrapper<MemberGroupRelation> delRelation = new QueryWrapper<>();
        delRelation.lambda().eq(MemberGroupRelation::getGroupId,id);
        this.relationPlusDao.remove(delRelation);
        //4.删除黑名单
        QueryWrapper<MemberGroupBlacklist> delBlacklist = new QueryWrapper<>();
        delBlacklist.lambda().eq(MemberGroupBlacklist::getGroupId,id);
        this.blacklistPlusDao.remove(delBlacklist);
    }

    /**
     * 获取会员组明细
     * @param id
     * @return
     */
    public MemberGroupVo getGroupDetail(String id){
        MemberGroupVo resVo = new MemberGroupVo();
        MemberGroup group = this.groupPlusDao.getById(id);
        if(Objects.isNull(group)){
            return resVo;
        }
        BeanUtils.copyProperties(group,resVo);
        if(resVo.getRuleStatus()){
            List<MemberGroupRule> groupRuleList = getGroupRuleList(id);
            if(!CollectionUtils.isEmpty(groupRuleList)){
                resVo.setRuleBoList(CopyUtil.copyList(groupRuleList,AddOrUpdateMemberRuleBo.class));
            }
        }
        return resVo;
    }


    /**
     * 获取分组规则列表
     * @param groupId
     * @return
     */
    public List<MemberGroupRule> getGroupRuleList(String groupId){
         return this.rulePlusDao.lambdaQuery().eq(MemberGroupRule::getGroupId,groupId)
                .orderByAsc(MemberGroupRule::getUpdateTime).list();
    }

    /**
     * 分页查询分组客户列表
     * @param qo
     * @return
     */
    public Page<PageListGroupMemberVo> pageListGroupMember(PageListGroupMemberQo qo){
        Page<PageListGroupMemberVo> resPage = new Page<>();
        QueryWrapper<MemberGroupRelation> where = new QueryWrapper<>();
        where.lambda().eq(MemberGroupRelation::getGroupId,qo.getGroupId());
        if(Objects.nonNull(qo.getInType())){
            where.lambda().eq(MemberGroupRelation::getInType,qo.getInType());
        }
        if(StringUtil.notEmpty(qo.getNickName())){
            where.lambda().like(MemberGroupRelation::getNickName,qo.getNickName());
        }
        IPage<MemberGroupRelation> page = this.relationPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(page.getTotal() > 0){
            resPage.setRecords(CopyUtil.copyList(page.getRecords(),PageListGroupMemberVo.class));
        }
        return resPage;
    }

    /**
     * 添加白名单用户
     * @param boList
     */
    public void addGroupMemberBy(List<AddWhitelistMemberBo> boList){
        StringBuilder msg= new StringBuilder();
        for(AddWhitelistMemberBo bo : boList){
            //校验是否在黑名单
            MemberGroupBlacklist blackBo = this.blacklistPlusDao.lambdaQuery()
                    .eq(MemberGroupBlacklist::getGroupId, bo.getGroupId())
                    .eq(MemberGroupBlacklist::getUserId, bo.getUserId())
                    .last("limit 1").one();
            if(Objects.nonNull(blackBo)){
                msg.append(bo.getUserId());
                continue;
            }
            //校验是否已存在 名单
            MemberGroupRelation relation = getGroupUserByUserId(bo.getGroupId(), bo.getUserId(),null);
            if(Objects.nonNull(relation)){
                continue;
            }
            MemberGroupRelation addBean = new MemberGroupRelation();
            BeanUtils.copyProperties(bo,addBean);
            addBean.setInType(2);
            addBean.setId(null);
            this.relationPlusDao.save(addBean);
        }
        if(StringUtil.notEmpty(msg.toString())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"「部分用户被拉黑，无法加入用户组」");
        }
    }

    /**
     * 添加黑名单用户
     * @param bo
     */
    public void addBlackListMember(AddWhitelistMemberBo bo){
        //1.从分组剔除用户
        this.relationPlusDao.removeById(bo.getId());
        //2.加入黑名单
        this.blacklistPlusDao.save(CopyUtil.copy(bo,MemberGroupBlacklist.class));
    }

    /**
     * 查询分组黑名单列表
     * @param groupId
     * @return
     */
    public List<MemberGroupBlacklist> listBlackList(String groupId){
        return this.blacklistPlusDao.lambdaQuery().
                eq(MemberGroupBlacklist::getGroupId,groupId)
                .orderByDesc(MemberGroupBlacklist::getUpdateTime).list();
    }

    /**
     * 剔除分组黑名单用户
     * @param id
     */
    public void delBlackListMember(String id){
        this.blacklistPlusDao.removeById(id);
    }

    /**
     * 定时任务跑分组会员
     */
    @RedisLock(constantKey = "runningGroupMember",prefix ="byx:scheduled:running:member",isWaitForLock = false)
    public void synGroupMember(){
        log.info("------------开始分组添加用户-------");
        //1.查询出所有的分组
        List<MemberGroup> list = this.groupPlusDao.lambdaQuery()
                .eq(MemberGroup::getDataStatus,Boolean.TRUE)
                .eq(MemberGroup::getRuleStatus,Boolean.TRUE)
                .orderByDesc(MemberGroup::getBusinessId).list();
        if(CollectionUtils.isEmpty(list)){
            log.info("------------分组添加用户没有需要更新的数据而结束-------");
            return;
        }
        //2.每个处理每个分组
        for(MemberGroup memberGroup : list){
            this.asyncService.runningGroupMember(memberGroup.getId());
        }
        log.info("------------分组添加用户结束-------");
    }

    /**
     * 分页查询用户画像信息
     * @param qo
     * @return
     */
    public Page<BusinessUserPortraitVo> pageListUserPortrait(PageListUserPortraitQo qo){
        Page<BusinessUserPortraitVo> resPage = new Page<>();
        QueryWrapper<InteractiveClickLog> where = new QueryWrapper<>();
        where.lambda().eq(InteractiveClickLog::getBid,qo.getBid());
        where.lambda().eq(InteractiveClickLog::getClickUid,qo.getUserId());
        List<Integer> logTypeList = new ArrayList<>();
        logTypeList.add(8);logTypeList.add(2);logTypeList.add(7);
        logTypeList.add(5);logTypeList.add(6);
        where.lambda().in(InteractiveClickLog::getPromotionType,logTypeList);
        where.lambda().orderByDesc(InteractiveClickLog::getCreateTime);
        IPage<InteractiveClickLog> page = this.clickLogPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        //获取用户信息
        User user = this.userPlusDao.getById(qo.getUserId());
        if(Objects.isNull(user)){
            return resPage;
        }
        List<BusinessUserPortraitVo> resList = new ArrayList<>();
        for(InteractiveClickLog clickLog : page.getRecords()){
            BusinessUserPortraitVo resVo = new BusinessUserPortraitVo();
            resVo.setPId(clickLog.getPromotionId());
            resVo.setLogType(clickLog.getPromotionType());
            resVo.setLogTime(DateUtil.dateTimeToTimeString(clickLog.getCreateTime()));
            //服务 文案：「用户名」浏览了「服务名称」
            if(PromotionTypeEnum.BROWSE_PRODUCT.getValue().equals(clickLog.getPromotionType())){
                Product dbProduct = this.productService.getProductDbById(clickLog.getPromotionId());
                if(Objects.isNull(dbProduct)){
                    continue;
                }
                resVo.setLogText("「"+user.getNickName()+"」浏览了「"+dbProduct.getProductName()+"」");
            }
            //电子传单 文案：「用户名」浏览了「分享人名称」分享的「电子传单名称」
            if(PromotionTypeEnum.DZ_FLYER.getValue().equals(clickLog.getPromotionType())){
                User shareUser = this.userPlusDao.getById(clickLog.getShareUid());
                if(Objects.isNull(shareUser)){
                    continue;
                }
                FlyerInfo flyer = flyerInfoService.getFlyerById(clickLog.getPromotionId());
                if(Objects.isNull(flyer)){
                    continue;
                }
                resVo.setLogText("「"+user.getNickName()+"」浏览了「"+shareUser.getNickName()+"」分享的「"+flyer.getFlyerName()+"」");
            }
            //名片 文案：「用户名」浏览了「老师真实名称」的名片
            if(PromotionTypeEnum.BROWSE_CARD.getValue().equals(clickLog.getPromotionType())){
                AdminCard card = this.adminCardService.getAdminCardById(clickLog.getPromotionId());
                if(Objects.isNull(card)){
                    continue;
                }
                Admin admin = this.adminService.getAdminById(card.getAdminId());
                if(Objects.isNull(admin)){
                    continue;
                }
                resVo.setLogText("「"+user.getNickName()+"」浏览了「"+admin.getTrueName()+"」的名片");
            }
            //订单 文案：「用户名」 购买了「服务名称」
            if(PromotionTypeEnum.CREATE_ORDER.getValue().equals(clickLog.getPromotionType())){
                OrderDetail detail = detailPlusDao.lambdaQuery().eq(OrderDetail::getOrderSn, clickLog.getPromotionId()).last("limit 1").one();
                if(Objects.isNull(detail)){
                    continue;
                }
                resVo.setLogText("「"+user.getNickName()+"」购买了「"+detail.getProductName()+"」");
            }
            //定金支付 文案：「用户名」支付 订单「订单号」70元
            if(PromotionTypeEnum.FIRST_PAY.getValue().equals(clickLog.getPromotionType())){
                Orders orders = ordersPlusDao.lambdaQuery().eq(Orders::getOrderSn, clickLog.getPromotionId()).last("limit 1").one();
                if(Objects.isNull(orders)){
                    continue;
                }
                resVo.setLogText("「"+user.getNickName()+"」支付订单「"+clickLog.getPromotionId()+"」"+orders.getFirstPayMoney()+"元");
            }
            resList.add(resVo);
        }
        resPage.setRecords(resList);
        return resPage;
    }

    /**
     * 获取分组用户
     * @param groupId
     * @param userId
     * @param inType
     * @return
     */
    public MemberGroupRelation getGroupUserByUserId(String groupId,String userId,Integer inType){
        QueryWrapper<MemberGroupRelation> where = new QueryWrapper<>();
        where.lambda().eq(MemberGroupRelation::getGroupId,groupId);
        where.lambda().eq(MemberGroupRelation::getUserId,userId);
        if(Objects.nonNull(inType)){
            where.lambda().eq(MemberGroupRelation::getInType,inType);
        }
        where.lambda().last("limit 1");
        return this.relationPlusDao.getOne(where);
    }





}
