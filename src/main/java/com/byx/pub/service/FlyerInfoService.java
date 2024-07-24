package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddOrUpdateFlyerInfoBo;
import com.byx.pub.bean.bo.ShareFlyerBo;
import com.byx.pub.bean.bo.ShareFlyerClickBo;
import com.byx.pub.bean.qo.PageFlyerInfoListQo;
import com.byx.pub.bean.qo.PageFlyerPullListQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.plus.entity.FlyerInfo;
import com.byx.pub.plus.entity.OrderDetail;
import com.byx.pub.plus.entity.Orders;

import java.util.List;

/**
 * @author Jump
 * @date 2023/10/7 18:17:53
 */
public interface FlyerInfoService {

    /**
     * 新增或修改电子传单
     * @param flyerInfoBo
     */
    void addOrUpdateFlyer(AddOrUpdateFlyerInfoBo flyerInfoBo);

    /**
     * 分页条件查询传单
     * @param qo
     * @return
     */
    Page<PageFlyerInfoListVo> pageList(PageFlyerInfoListQo qo);

    /**
     * 查询传单详情
     * @param id
     * @return
     */
    FlyerInfoVo getFlyerInfoDetail(String id);

    /**
     * 删除传单信息
     * @param id
     */
    void delFlyer(String id);


    /**
     * 分享传单
     * 1.当前人分享所属商家的传单 有则返回 无则新增
     * 2.用户分享则返回空对象 (这样就不会影响原来的分享)
     * 3.同一个商家下A助教分享给B助教，B助教点进去再分享给a用户，这时分享对象已替换成B的。所以业绩算B的
     * 4.不同上加下A、B助教如3，则算A的，返回A的链接
     * @param bo
     * @return
     */
    FlyerShareVo shareFlyer(ShareFlyerBo bo);

    /**
     * 保存拉新记录
     * 1.增加分享id是用于过滤用户拉新人的统计
     * @param bo
     */
    void addClickRecord(ShareFlyerClickBo bo);

    /**
     * 分页查询引流统计
     * @param qo
     * @return
     */
    Page<PageFlyerPullListVo> pageFlyerPullList(PageFlyerPullListQo qo);

    /**
     * 校验并成交引流记录
     * 1.下单商家与引流商家同一个
     * 2.分享id不能是空(为空表示不是商家的员工)
     * 3.拿最早引流记录(区别多助教引流同一个人)
     * @param orders
     * @param detail
     */
    void checkAndDeal(Orders orders, OrderDetail detail);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    FlyerInfo getFlyerById(String id);

    /**
     * 统计转发数据
     * @return
     */
    List<SupplierRelayVo> listRelay();

    /**
     * 获取转发成交明细数据
     * @return
     */
    List<FlyerDealDetailVo> getRelayDealDetailList();
}
