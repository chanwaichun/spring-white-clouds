package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.TradeOrderListQo;
import com.byx.pub.bean.vo.DataCountTradeOrderVo;
import com.byx.pub.bean.vo.DataCountTradeVo;
import com.byx.pub.service.DataCountService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [客户端]-[咨询师]-[数据统计]服务Api
 * @Author Jump
 * @Date 2023/8/18 0:22
 */
@RestController
@RequestMapping("/white/clouds/front/consultant/v1/data/count")
@Api(value = "[客户端]-[咨询师]-[数据统计]服务Api",tags = "[客户端]-[咨询师]-[数据统计]服务Api")
public class ConsultantDataCountController {
    @Resource
    DataCountService dataCountService;

    /**
     * 分页查询交易订单
     * @param qo
     * @return
     */
    @PostMapping("/trade/order/list")
    @ApiOperation(value = "分页查询交易订单")
    public CommonResult<Page<DataCountTradeOrderVo>> tradeOrderList(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @Validated @RequestBody TradeOrderListQo qo
    ){
        qo.setBusinessId(businessId);
        return CommonResult.success(dataCountService.pageSjOrderList(qo));
    }

    /**
     * 交易数据
     * @param businessId 商家id
     * @param queryType 查询类型
     * @return
     */
    @GetMapping("/trade/data")
    @ApiOperation(value = "交易数据")
    public CommonResult<DataCountTradeVo> getTradeData(
        @ApiParam(value = "商家id")@RequestHeader(value = "business-id",required = false) String businessId,
        @ApiParam(required = true, value = "查询类型(1：日报，2：周报，3：月报，0：全部)") @RequestParam("queryType") Integer queryType
    ){
        return CommonResult.success(dataCountService.getTradeData(businessId,queryType));
    }


}
