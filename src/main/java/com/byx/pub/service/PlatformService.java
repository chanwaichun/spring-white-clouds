package com.byx.pub.service;

import com.byx.pub.bean.vo.ListAdminPlatformVo;
import com.byx.pub.bean.xhs.XhsPushProductResVo;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/12/26 21:31
 */
public interface PlatformService {
    /**
     * 推送小红书商品
     * @param productId
     * @param adminId
     * @return
     */
    XhsPushProductResVo pushXhsProduct(String productId, String adminId);

    /**
     * 获取商家跨平台帐号列表
     * @param bid
     * @return
     */
    List<ListAdminPlatformVo> adminPlatformList(String bid);

    /**
     * 绑定小红书店铺id
     * @param adminId
     * @param xhsSellerId
     */
    void bindXhsId(String adminId,String xhsSellerId);
}
