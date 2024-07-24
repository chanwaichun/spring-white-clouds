package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
/**
 * @Author Jump
 * @Date 2023/8/18 0:05
 */
@Data
public class TradeOrderListQo {
    /**
     * 商家id(前端不传)
     */
    @ApiModelProperty(value = "商家id(前端不传)")
    private String businessId;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间(前端不传)")
    private String createStart;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间(前端不传)")
    private String createEnd;

    /**
     * 查询类型(1：日报，2：周报，3：月报，0：全部)
     */
    @ApiModelProperty(value = "查询类型(1：日报，2：周报，3：月报，0：全部)")
    @Range(min = 0,max = 3,message = "请选择查询类型")
    private Integer queryType;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "页面条数最小值为1")
    @NotNull(message = "页面条数不能为空")
    @ApiModelProperty(value = "页面大小", required = true)
    private Integer pageSize;
    /**
     * 页码
     */
    @Min(value = 1, message = "页码最小值为1")
    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;
}
