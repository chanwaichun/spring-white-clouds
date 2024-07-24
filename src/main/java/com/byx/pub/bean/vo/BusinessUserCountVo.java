package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/17 21:45
 */
@Data
public class BusinessUserCountVo {
    /**
     * 总客户数
     */
    @ApiModelProperty(value = "总客户数")
    private Integer customNum;

    /**
     * 昨日新增
     */
    @ApiModelProperty(value = "昨日新增")
    private Integer yesterdayAddNum;

}
