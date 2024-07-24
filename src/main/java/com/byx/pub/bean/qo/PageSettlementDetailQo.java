package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author Jump
 * @Date 2023/9/21 21:01
 */
@Data
@Accessors(chain = true)
public class PageSettlementDetailQo {
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
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

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
