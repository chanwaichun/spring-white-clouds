package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddOrUpdateMemberGroupBo;
import com.byx.pub.bean.bo.AddWhitelistMemberBo;
import com.byx.pub.bean.qo.PageAdminUserQo;
import com.byx.pub.bean.qo.PageListGroupMemberQo;
import com.byx.pub.bean.qo.PageListUserPortraitQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.plus.entity.MemberGroup;
import com.byx.pub.plus.entity.MemberGroupBlacklist;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.List;

/**
 * 咨询师会员服务
 * @author Jump
 * @date 2023/6/14 15:10:18
 */
public interface BusinessUserService {

    /**
     * 分页条件查询客户列表
     * @param qo
     * @return
     */
    Page<PageBusinessUserVo> pageList(PageAdminUserQo qo);

    /**
     * 获取商家用户统计数据
     * @param sjId
     * @return
     */
    BusinessUserCountVo sjUserCount(String sjId);

    /**
     * 新增商家会员信息
     * @param adminId
     * @param bid
     * @param userId
     * @param memberType
     */
    void addSjMember(String adminId,String bid, String userId,Integer memberType,Boolean xhsStatus);

    /**
     * 累加会员支付金额
     * @param firstPay
     * @param bid
     * @param userId
     * @param orderAmount
     */
    void growMemberPayAmount(Boolean firstPay, String bid, String userId, BigDecimal orderAmount);

    /**
     * 获取会员组明细
     * @param id
     * @return
     */
    MemberGroupVo getGroupDetail(String id);

    /**
     * 查询商家会员分组
     * @param bid
     * @return
     */
    List<MemberGroup> listMemberGroup(String bid);

    /**
     * 新增或修改会员组
     * @param groupBo
     */
    void  addOrUpdateMemberGroup(AddOrUpdateMemberGroupBo groupBo);

    /**
     * 删除会员分组
     * @param id
     */
    void delGroup(String id);

    /**
     * 分页查询分组客户列表
     * @param qo
     * @return
     */
    Page<PageListGroupMemberVo> pageListGroupMember(PageListGroupMemberQo qo);

    /**
     * 添加白名单用户
     * @param boList
     */
    void addGroupMemberBy(List<AddWhitelistMemberBo> boList);

    /**
     * 添加黑名单用户
     * @param bo
     */
    void addBlackListMember(AddWhitelistMemberBo bo);

    /**
     * 查询分组黑名单列表
     * @param groupId
     * @return
     */
    List<MemberGroupBlacklist> listBlackList(String groupId);

    /**
     * 剔除分组黑名单用户
     * @param id
     */
    void delBlackListMember(String id);

    /**
     * 定时任务跑分组会员
     */
    void synGroupMember();

    /**
     * 分页查询用户画像信息
     * @param qo
     * @return
     */
    Page<BusinessUserPortraitVo> pageListUserPortrait(PageListUserPortraitQo qo);
}
