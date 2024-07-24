package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageListPayFlowQo;
import com.byx.pub.bean.vo.ConsultantIncomeVo;
import com.byx.pub.mapper.OrdersSqlMapper;
import com.byx.pub.service.ManageFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 财务服务
 * @Author Jump
 * @Date 2023/6/25 21:19
 */
@Slf4j
@Service
public class ManageFinanceServiceImpl implements ManageFinanceService {
    @Resource
    OrdersSqlMapper ordersSqlMapper;

    /**
     * 查询支付流水
     * @param qo
     * @return
     */
    public Page<ConsultantIncomeVo> pagePayFlowList(PageListPayFlowQo qo){
        return this.ordersSqlMapper.queryPayFlowList(new Page<>(qo.getPageNum(),qo.getPageSize()),qo);
    }




}
