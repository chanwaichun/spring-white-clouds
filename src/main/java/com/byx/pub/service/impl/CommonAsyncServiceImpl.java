package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.byx.pub.exception.ApiException;
import com.byx.pub.mapper.ProductSqlMapper;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.CommonAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 公共异步服务
 * @Author Jump
 * @Date 2023/10/22 12:55
 */
@Slf4j
@Service
public class CommonAsyncServiceImpl implements CommonAsyncService {
    @Resource
    BusinessUserPlusDao memberPlusDao;
    @Resource
    MemberGroupPlusDao memberGroupPlusDao;
    @Resource
    MemberGroupRulePlusDao groupRulePlusDao;
    @Resource
    MemberGroupRelationPlusDao relationPlusDao;
    @Resource
    MemberGroupBlacklistPlusDao blacklistPlusDao;
    @Resource
    OrdersPlusDao ordersPlusDao;
    @Resource
    FlyerPullRecordPlusDao flyerPullRecordPlusDao;
    @Resource
    ProductSqlMapper productSqlMapper;
    @Resource
    UserPlusDao userPlusDao;


    /**
     * 给会员分组跑数据
     * @param groupId
     */
    @Async
    public void runningGroupMember(String groupId){
        //1.删除分组下规则进入的用户
        QueryWrapper<MemberGroupRelation> delRelationWhere = new QueryWrapper<>();
        delRelationWhere.lambda().eq(MemberGroupRelation::getGroupId,groupId);
        delRelationWhere.lambda().eq(MemberGroupRelation::getInType,1);
        relationPlusDao.remove(delRelationWhere);
        //2.查询出分组信息
        MemberGroup memberGroup = this.memberGroupPlusDao.getById(groupId);
        if(Objects.isNull(memberGroup) || !memberGroup.getDataStatus() || !memberGroup.getRuleStatus()){
            //更新分组状态
            doneGroupMember(groupId);
            return;
        }
        //3.查询分组规则列表
        List<MemberGroupRule> ruleList = this.groupRulePlusDao.lambdaQuery().eq(MemberGroupRule::getGroupId, groupId).list();
        if(CollectionUtils.isEmpty(ruleList)){
            //更新分组状态
            doneGroupMember(groupId);
            return;
        }
        //4.查询查询商家会员
        List<BusinessUser> memberList = this.memberPlusDao.lambdaQuery().eq(BusinessUser::getBusinessId, memberGroup.getBusinessId()).list();
        if(CollectionUtils.isEmpty(memberList)){
            //更新分组状态
            doneGroupMember(groupId);
            return;
        }
        //5.针对规则筛选会员
        for(BusinessUser member : memberList){
            //如果用户在分组黑名单直接跳过
            if(checkBlacklist(groupId,member.getUserId())){
                continue;
            }
            //校验状态，所有规则都通过才算通过
            Boolean checkStatus = Boolean.TRUE;
            //所有规则都符合才进组
            for(MemberGroupRule rule : ruleList ){
                //订单状态规则
                if(1 == rule.getRuleType()){
                    Integer count = this.ordersPlusDao.lambdaQuery().eq(Orders::getUserId, member.getUserId())
                            .eq(Orders::getBusinessId, memberGroup.getBusinessId())
                            .eq(Orders::getStatus, Integer.parseInt(rule.getRuleValue())).count();
                    if(0 == count){
                        checkStatus = Boolean.FALSE;
                        break;
                    }
                }
                //电子传单
                if(2 == rule.getRuleType()){
                    Integer count = this.flyerPullRecordPlusDao.lambdaQuery().eq(FlyerPullRecord::getFlyerId, rule.getRuleValue())
                            .eq(FlyerPullRecord::getInviteeUid, member.getUserId()).count();
                    if(0 == count){
                        checkStatus = Boolean.FALSE;
                        break;
                    }
                }
                //购买服务(下单过就算)
                if(3 == rule.getRuleType()){
                    Integer count = productSqlMapper.countUserOrderNumByPid(member.getUserId(), rule.getRuleValue());
                    if(0 == count){
                        checkStatus = Boolean.FALSE;
                        break;
                    }
                }
            }
            //如果校验成功则加入组
            if(checkStatus){
                //校验是否已存在名单
                MemberGroupRelation oldRelation = getGroupUserByUserId(groupId, member.getUserId());
                User user = this.userPlusDao.getById(member.getUserId());
                if(Objects.isNull(oldRelation) && Objects.nonNull(user)){
                    MemberGroupRelation relation = new MemberGroupRelation();
                    relation.setGroupId(groupId)
                            .setUserId(member.getUserId())
                            .setUserImg(user.getUserImg())
                            .setNickName(user.getNickName())
                            .setInType(1);
                    this.relationPlusDao.save(relation);
                }
            }
        }
        //更新分组状态
        doneGroupMember(groupId);
    }

    /**
     * 完成分组跑会员
     * @param groupId
     */
    public void doneGroupMember(String groupId){
        //查询分组会员数
        Integer count = this.relationPlusDao.lambdaQuery().eq(MemberGroupRelation::getGroupId, groupId).count();
        //完成分组
        this.memberGroupPlusDao.updateById(
                new MemberGroup().setId(groupId).setUpdateStatus(2).setMemberNum(count)
        );
    }

    /**
     * 判断订单状态
     * @param userId
     * @param bid
     * @param orderStatus
     * @return
     */
    public Boolean checkOrder(String userId,String bid,Integer orderStatus){
        Integer count = this.ordersPlusDao.lambdaQuery().eq(Orders::getUserId, userId)
                .eq(Orders::getBusinessId, bid).eq(Orders::getStatus, orderStatus).count();
        if(0 == count){
            return false;
        }
        return true;
    }


    /**
     * 判断用户是否在分组黑名单
     * @param groupId
     * @param userId
     * @return
     */
    public Boolean checkBlacklist(String groupId,String userId){
        Integer count = this.blacklistPlusDao.lambdaQuery().eq(MemberGroupBlacklist::getGroupId, groupId)
                .eq(MemberGroupBlacklist::getUserId, userId).count();
        if(0 == count){
            return false;
        }
        return true;
    }


    /**
     * 获取分组用户
     * @param groupId
     * @param userId
     * @return
     */
    public MemberGroupRelation getGroupUserByUserId(String groupId,String userId){
        return this.relationPlusDao.lambdaQuery()
                .eq(MemberGroupRelation::getGroupId,groupId)
                .eq(MemberGroupRelation::getUserId,userId)
                .last("limit 1").one();
    }



}
