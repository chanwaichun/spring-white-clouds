package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.TradeOrderListQo;
import com.byx.pub.bean.vo.DataCountTradeOrderVo;
import com.byx.pub.bean.vo.DataCountTradeVo;
import com.byx.pub.mapper.OrdersSqlMapper;
import com.byx.pub.service.DataCountService;
import com.byx.pub.service.FrontOrderService;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jump
 * @Date 2023/8/17 21:59
 */
@Slf4j
@Service
public class DataCountServiceImpl implements DataCountService {
    @Resource
    OrdersSqlMapper sqlMapper;
    @Resource
    FrontOrderService orderService;

    /**
     * 查询商家交易订单
     * @param qo
     * @return
     */
    public Page<DataCountTradeOrderVo> pageSjOrderList(TradeOrderListQo qo){
        //设置参数
        Map<String, String> dateTimeMap = typeToDate(qo.getQueryType());
        qo.setCreateStart(dateTimeMap.get("startTime"));
        qo.setCreateEnd(dateTimeMap.get("endTime"));
        return this.orderService.pageSjOrderList(qo);
    }



    /**
     * 查询商家交易数据
     * @param sjId
     * @param queryType
     * @return
     */
    public DataCountTradeVo getTradeData(String sjId,Integer queryType){
        DataCountTradeVo  resVo = new DataCountTradeVo();
        String startTime = ""; String endTime = "";
        //转换时间
        if(0 < queryType){
            Map<String, String> dateTimeMap = typeToDate(queryType);
            startTime = dateTimeMap.get("startTime");
            endTime = dateTimeMap.get("endTime");
        }
        //查询数据
        BigDecimal payAmountBySj = sqlMapper.queryPayAmountBySj(sjId, startTime, endTime);
        resVo.setPayAmount(payAmountBySj.toString());
        resVo.setPayOrderNum(sqlMapper.queryOrderNumBySj(sjId,startTime,endTime)+"");
        Integer userNumBySj = sqlMapper.queryPayUserNumBySj(sjId, startTime, endTime);
        resVo.setPayUserNum(userNumBySj+"");
        BigDecimal pct = StringUtil.customDivision(payAmountBySj, BigDecimal.valueOf(userNumBySj), 2);
        resVo.setPct(pct.toString());
        resVo.setNoPayOrderAmount(sqlMapper.queryNoPayOrderAmountBySj(sjId,startTime,endTime).toString());
        resVo.setNoPayOrderNum(sqlMapper.queryNoPayOrderNumBySj(sjId,startTime,endTime)+"");
        return resVo;
    }


    /**
     * 类型转换开始结束时间
     * @param queryType
     * @return
     */
    public Map<String,String> typeToDate(Integer queryType){
        LocalDate now = LocalDate.now();
        //查询类型(1；日报，2：周报，3：月报，0：全部)
        Map<String,String> resMap = new HashMap<>(2);
        //今天
        if(queryType == 1){
            resMap.put("startTime",DateUtil.LocalDateTimeToStr(now.atStartOfDay()));
            resMap.put("endTime",DateUtil.LocalDateTimeToStr(LocalDateTime.of(now, LocalTime.MAX)));
        }
        //本周
        if(queryType == 2){
            LocalDate start = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            resMap.put("startTime",DateUtil.LocalDateTimeToStr(start.atStartOfDay()));
            LocalDate end = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            resMap.put("endTime",DateUtil.LocalDateTimeToStr(LocalDateTime.of(end, LocalTime.MAX)));
        }
        //本月
        if(queryType == 3){
            LocalDate start = now.with(TemporalAdjusters.firstDayOfMonth());
            resMap.put("startTime",DateUtil.LocalDateTimeToStr(start.atStartOfDay()));
            LocalDate end = now.with(TemporalAdjusters.lastDayOfMonth());
            resMap.put("endTime",DateUtil.LocalDateTimeToStr(LocalDateTime.of(end, LocalTime.MAX)));
        }
        return resMap;
    }

}
