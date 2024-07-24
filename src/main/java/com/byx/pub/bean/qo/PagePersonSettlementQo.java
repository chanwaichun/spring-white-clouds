package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/9/21 22:09
 */
@Data
public class PagePersonSettlementQo {
    /**
     * 账单id
     */
    @ApiModelProperty(value = "账单id")
    @NotEmpty(message = "账单id不能为空")
    private String mainId;

    /**
     * adminId(前端不用传)
     */
    @ApiModelProperty(value = "adminId(前端不用传)")
    private String adminId;
    /**
     * userId(前端不用传)
     */
    @ApiModelProperty(value = "userId(前端不用传)")
    private String userId;

    /**
     * 结算人后台名称
     */
    @ApiModelProperty(value = "结算人后台名称")
    private String settlerAdminName;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderSn;
    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;

    /**
     * 创建开始时间
     */
    @ApiModelProperty(value = "创建开始时间")
    private Date startTime;

    /**
     * 创建结束时间
     */
    @ApiModelProperty(value = "创建结束时间")
    private Date endTime;

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
