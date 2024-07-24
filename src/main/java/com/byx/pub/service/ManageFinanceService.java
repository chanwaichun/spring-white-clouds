package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageListPayFlowQo;
import com.byx.pub.bean.vo.ConsultantIncomeVo;

/**
 * 财务服务
 * @Author Jump
 * @Date 2023/6/25 21:20
 */
public interface ManageFinanceService {

    /**
     * 查询支付流水
     * @param qo
     * @return
     */
    Page<ConsultantIncomeVo> pagePayFlowList(PageListPayFlowQo qo);


}
