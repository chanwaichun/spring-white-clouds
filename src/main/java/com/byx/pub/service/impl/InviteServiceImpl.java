package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageInviteRecordQo;
import com.byx.pub.bean.vo.InviteRecordVo;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.InvitePlusDao;
import com.byx.pub.plus.dao.InviteRecordPlusDao;
import com.byx.pub.plus.entity.Invite;
import com.byx.pub.plus.entity.InviteRecord;
import com.byx.pub.plus.entity.User;
import com.byx.pub.service.FrontUserService;
import com.byx.pub.service.InviteService;
import com.byx.pub.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/8/14 23:22
 */
@Slf4j
@Service
public class InviteServiceImpl implements InviteService {
    @Resource
    InvitePlusDao invitePlusDao;
    @Resource
    InviteRecordPlusDao inviteRecordPlusDao;
    @Resource
    FrontUserService frontUserService;

    /**
     * 保存邀请记录
     * @param inviteUserId
     * @param newUserId
     */
    public void saveInviteRecord(String inviteUserId,String newUserId){
        //查询邀请函信息
        Invite inviteByUserId = this.getInviteByUserId(inviteUserId);
        if(Objects.isNull(inviteByUserId)){
            throw new ApiException("未找到邀请函信息");
        }
        //查询新用户信息
        User userInfo = this.frontUserService.getUserById(newUserId);
        if(Objects.isNull(userInfo)){
            throw new ApiException("未找到用户信息");
        }
        //保存邀请记录
        InviteRecord record = new InviteRecord();
        record.setInviteUserId(inviteUserId)
                .setNewUserId(newUserId)
                .setOpenId(userInfo.getOpenId())
                .setInviteId(inviteByUserId.getId())
                .setNickName(userInfo.getNickName());
        this.inviteRecordPlusDao.save(record);
    }

    /**
     * 保存二维码
     * @param inviteUserId
     * @param qrCode
     */
    public Invite saveQrCode(String inviteUserId,String qrCode){
        //1.优先数据库获取
        Invite invite = getInviteByUserId(inviteUserId);
        if(Objects.nonNull(invite)){
            throw new ApiException("已存在二维码");
        }
        //2.保存数据库
        Invite addInvite = new Invite();
        addInvite.setInviteUserId(inviteUserId).setQrCode(qrCode);
        this.invitePlusDao.save(addInvite);
        return invitePlusDao.getById(addInvite.getId());
    }

    /**
     * 获取用户邀请函信息
     * @param inviteUserId
     * @return
     */
    public Invite getInviteByUserId(String inviteUserId){
       return invitePlusDao.lambdaQuery().eq(Invite::getDataStatus, Boolean.TRUE)
               .eq(Invite::getInviteUserId, inviteUserId).last("limit 1").one();
    }

    /**
     * 分页查询邀请记录
     * @param qo
     * @return
     */
    public Page<InviteRecordVo> pageInviteRecordList(PageInviteRecordQo qo){
        Page<InviteRecordVo> inviteRecordVoPage = new Page<>();
        QueryWrapper<InviteRecord> where = new QueryWrapper<>();
        where.lambda().eq(InviteRecord::getInviteId,qo.getInviteId());
        where.lambda().eq(InviteRecord::getDataStatus,Boolean.TRUE);
        IPage<InviteRecord> page = this.inviteRecordPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,inviteRecordVoPage);
        if(0 == page.getTotal()){
            return inviteRecordVoPage;
        }
        inviteRecordVoPage.setRecords(CopyUtil.copyList(page.getRecords(), InviteRecordVo.class));
        return inviteRecordVoPage;
    }


    /**
     * 更新邀请记录
     * @param userId
     */
    public void changeInviteRecord(String userId){
        User userById = this.frontUserService.getUserById(userId);
        if(Objects.isNull(userById)){
            return;
        }
        //1.查询是否被邀请过
        InviteRecord record = this.inviteRecordPlusDao.lambdaQuery().eq(InviteRecord::getNewUserId, userId)
                .orderByDesc(InviteRecord::getCreateTime).last("limit 1").one();
        if(Objects.isNull(record)){
            return;
        }
        //2.TODO 统计结算单金额
        BigDecimal amount = BigDecimal.ZERO;
        //3.更改邀请记录
        InviteRecord updateRecord = new InviteRecord();
        updateRecord.setNickName(userById.getNickName())
                .setId(record.getId()).setIncome(amount).setJoinStatus(1);
        this.inviteRecordPlusDao.updateById(updateRecord);
        //修改其他邀请记录状态为 他人邀请
        this.inviteRecordPlusDao.lambdaUpdate()
                .eq(InviteRecord::getNewUserId, userId).ne(InviteRecord::getId, record.getId())
                .set(InviteRecord::getJoinStatus, 3).update();
        //4.更改用户记录
        User updateUser = new User();
        updateUser.setId(userId).setInviteStatus(true);
        this.frontUserService.updateUser(updateUser);
    }


}
