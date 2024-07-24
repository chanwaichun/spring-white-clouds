package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.vo.AdminShortCardVo;
import com.byx.pub.bean.vo.ListHotAdminCardVo;
import com.byx.pub.bean.vo.PageSearchCardVo;
import com.byx.pub.bean.vo.PanelProductVo;
import com.byx.pub.plus.entity.HotInfo;

import java.util.List;

/**
 * 用户面板服务
 * @Author Jump
 * @Date 2023/6/15 22:39
 */
public interface FrontUserPanelService {
    /**
     * 获取首页商品列表
     * @return
     */
    List<PanelProductVo> queryPanelProducts(String userRole,String businessId);
    /**
     * 获取资讯推荐
     * @return
     */
    List<HotInfo> queryHotInfoList();

    /**
     * 查询用户下过单的咨询师列表
     * @param userId
     * @return
     */
    List<AdminShortCardVo> queryUserByOrderAdminList(String userId);
    /**
     * 获取推荐咨询师
     * 由晓东提供
     * @return
     */
    List<ListHotAdminCardVo> getHostAdminList(String userRole,String businessId);

    /**
     * 查询名片信息
     * @param cardQo
     * @return
     */
    Page<PageSearchCardVo> searchAdminCard(PageAdminCardQo cardQo);
}
