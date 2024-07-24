package com.byx.pub.service.impl;

import com.byx.pub.plus.dao.InteractiveClickLogPlusDao;
import com.byx.pub.plus.entity.InteractiveClickLog;
import com.byx.pub.service.InteractiveClickService;
import com.byx.pub.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Jump
 * @Date 2023/10/8 21:29
 */
@Slf4j
@Service
public class InteractiveClickServiceImpl implements InteractiveClickService {
    @Resource
    InteractiveClickLogPlusDao clickLogPlusDao;

    /**
     * 保存点击记录
     * @param pid 活动id(商品id、名片id、订单id...)
     * @param pType 活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付、7：名片浏览、8：服务浏览))
     * @param shareUid 分享人id
     * @param clickUid 点击人id
     * @param bid 商家id
     */
    @Async
    public void saveLog(String pid,Integer pType,String shareUid,String clickUid,String bid){
        //自己点自己的不记录
        if(shareUid.equals(clickUid)){
            return;
        }
        InteractiveClickLog clickLog = new InteractiveClickLog();
        clickLog.setPromotionId(pid)
                .setPromotionType(pType)
                .setShareUid(shareUid)
                .setBid(bid)
                .setClickUid(clickUid);
        this.clickLogPlusDao.save(clickLog);
    }

}
