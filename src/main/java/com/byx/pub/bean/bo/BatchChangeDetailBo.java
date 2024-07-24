package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author Jump
 * @Date 2023/9/27 22:14
 */
@Data
public class BatchChangeDetailBo {
    /**
     * 明细主键
     */
    @ApiModelProperty(value = "明细主键")
    private String id;

    /**
     * 新结算金额
     */
    @ApiModelProperty(value = "新结算金额")
    private BigDecimal settlementAmount;
}
