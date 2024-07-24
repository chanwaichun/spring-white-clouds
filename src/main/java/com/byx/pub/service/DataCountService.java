package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.TradeOrderListQo;
import com.byx.pub.bean.vo.DataCountTradeOrderVo;
import com.byx.pub.bean.vo.DataCountTradeVo;

/**
 * @Author Jump
 * @Date 2023/8/17 21:59
 */
public interface DataCountService {

    /**
     * 查询商家交易订单
     * @param qo
     * @return
     */
    Page<DataCountTradeOrderVo> pageSjOrderList(TradeOrderListQo qo);

    /**
     * 查询商家交易数据
     * @param sjId
     * @param queryType
     * @return
     */
    DataCountTradeVo getTradeData(String sjId, Integer queryType);
}
