package com.byx.pub.tasks;

import com.byx.pub.service.BusinessUserService;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.service.SettlementService;
import com.byx.pub.service.XiaoHongShuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 咨询师月结账单定时任务
 * @Author Jump
 * @Date 2023/6/16 23:55
 */
@Slf4j
@Component
public class AdminMonthBillTask {

    @Resource
    SettlementService settlementService;
    @Resource
    BusinessUserService businessUserService;
    @Resource
    FrontOrderService frontOrderService;
    @Resource
    XiaoHongShuService xiaoHongShuService;


    /**
     * 每天00:01分结算订单(取消未结算订单)
     */
    @Scheduled(cron = "0 01 00 * * ?")
    public void task1() {
        this.frontOrderService.closedNoPayOrder();
        this.settlementService.settlementOrder();
    }

    /**
     * 每天00:10分给会员组跑数
     */
    @Scheduled(cron = "0 10 00 * * ?")
    public void task2() {
        this.businessUserService.synGroupMember();
    }

    /**
     * 每十分钟刷新小红书token
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void task3() {
        xiaoHongShuService.checkAndRefreshToken();
    }

    /**
     * 每小时同步一次小红书新增订单
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void task4() {
        xiaoHongShuService.synAddOrder();
    }

    /**
     * 每半小时同步一次订单状态
     */
    @Scheduled(cron = "0 0/25 * * * ?")
    public void task5() {
        xiaoHongShuService.synChangeOrder();
    }
}
