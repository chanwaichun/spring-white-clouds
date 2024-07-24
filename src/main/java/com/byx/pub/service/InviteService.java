package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageInviteRecordQo;
import com.byx.pub.bean.vo.InviteRecordVo;
import com.byx.pub.plus.entity.Invite;


/**
 * @Author Jump
 * @Date 2023/8/14 23:22
 */
public interface InviteService {

    /**
     * 保存邀请记录
     * @param inviteUserId
     * @param newUserId
     */
    void saveInviteRecord(String inviteUserId,String newUserId);

    /**
     * 保存二维码
     * @param inviteUserId
     * @param qrCode
     */
    Invite saveQrCode(String inviteUserId,String qrCode);

    /**
     * 获取用户邀请函信息
     * @param inviteUserId
     * @return
     */
    Invite getInviteByUserId(String inviteUserId);

    /**
     * 分页查询邀请记录
     * @param qo
     * @return
     */
    Page<InviteRecordVo> pageInviteRecordList(PageInviteRecordQo qo);

    /**
     * 更新邀请记录
     * @param userId
     */
    void changeInviteRecord(String userId);
}
