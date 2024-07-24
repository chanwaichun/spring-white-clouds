package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.ImportChangeDetailExcelBo;
import com.byx.pub.bean.qo.PageMainSettlementQo;
import com.byx.pub.bean.qo.PagePersonSettlementQo;
import com.byx.pub.bean.qo.PageSettlementDetailQo;
import com.byx.pub.bean.vo.PageMainSettlementVo;
import com.byx.pub.bean.vo.PagePersonSettlementVo;
import com.byx.pub.bean.vo.PageSettlementDetailListVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Jump
 * @Date 2023/6/16 22:01
 */
public interface SettlementService {
    /**
     * 分页条件查询结算单
     * @param qo
     * @return
     */
    Page<PageMainSettlementVo> pageMainSettlementList(PageMainSettlementQo qo);

    /**
     * 分页查询个人在服务的分成
     * @param qo
     * @return
     */
    Page<PagePersonSettlementVo> pagePersonSettlementList(PagePersonSettlementQo qo);

    /**
     * 分页条件查询结算明细
     * @param qo
     * @return
     */
    Page<PageSettlementDetailListVo> pageDetailList(PageSettlementDetailQo qo);

    /**
     * 修改明细结算金额
     * @param id
     * @param amount
     */
    void changeDetailAmount(String id, BigDecimal amount);

    /**
     * 导入变更修改结算金额
     * @param list
     */
    Integer importChangeDetailAmount(List<ImportChangeDetailExcelBo> list);

    /**
     * 确认结算账单
     * @param id
     */
    void confirmSettlementBill(String id);

    /**
     * 每天晚上凌晨结算符合条件的订单
     */
    void settlementOrder();

}
