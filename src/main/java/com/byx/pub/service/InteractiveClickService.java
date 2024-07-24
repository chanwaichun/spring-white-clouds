package com.byx.pub.service;

/**
 * @Author Jump
 * @Date 2023/10/8 21:30
 */
public interface InteractiveClickService {

    /**
     * 保存点击记录
     * @param pid 活动id(商品id、名片id、订单id...)
     * @param pType 活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付)
     * @param shareUid 分享人id
     * @param clickUid 点击人id
     * @param bid 商家id
     */
    void saveLog(String pid,Integer pType,String shareUid,String clickUid,String bid);
}
