package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageManageOrdersQo;
import com.byx.pub.bean.vo.PageOrdersVo;
import com.byx.pub.bean.vo.WorkPanelVo;
import com.byx.pub.mapper.OrdersSqlMapper;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.service.WorkPanelService;
import com.byx.pub.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 首页、工作面板服务
 * @Author Jump
 * @Date 2023/6/14 21:23
 */
@Slf4j
@Service
public class WorkPanelServiceImpl implements WorkPanelService {
    @Resource
    OrdersSqlMapper ordersSqlMapper;
    @Resource
    FrontOrderService frontOrderService;

    /**
     * 管理后台获取看板数据
     * @param qo
     * @return
     */
    @Override
    public WorkPanelVo queryPanelData(PageManageOrdersQo qo){
        String monthFirstDay = DateUtil.getMonthFirstTime();
        //获取看版数据
        WorkPanelVo resVo = new WorkPanelVo()
                .setTotalIncome(this.ordersSqlMapper.queryTotalIncome(qo.getBusinessId(),qo.getAdminId()))
                .setMonthIncome(this.ordersSqlMapper.queryMonthIncome(qo.getBusinessId(),qo.getAdminId(),monthFirstDay,null))
                .setMonthCustomer(this.ordersSqlMapper.queryMonthCustomer(qo.getBusinessId(),qo.getAdminId(),monthFirstDay,null))
                .setMonthOrders(this.ordersSqlMapper.queryMonthOrders(qo.getBusinessId(),qo.getAdminId(),monthFirstDay));
        //获取看版订单数据
        Page<PageOrdersVo> orderPage = this.frontOrderService.queryManagePageOrders(qo);
        if(0 == orderPage.getTotal()){
            return resVo;
        }
        resVo.setOrderList(orderPage.getRecords());
        return resVo;
    }


}
