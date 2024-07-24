package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 管理后台订单查询qo
 * @author Jump
 * @date 2023/6/8 15:05:37
 */
@Data
@Accessors(chain = true)
public class PageManageOrdersQo {
    /**
     * 商家id(前端不用传)
     */
    @ApiModelProperty(value = "商家id(前端不用传)")
    private String businessId;

    /**
     * 咨询师id(前端不用传)
     */
    @ApiModelProperty(value = "咨询师id(前端不用传)")
    private String adminId;

    /**
     * 用户id(前端不用传)
     */
    @ApiModelProperty(value = "用户id(前端不用传)")
    private String userId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String adminName;

    /**
     * 订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)
     */
    @ApiModelProperty(value = "订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)")
    private Integer status;

    /**
     * 订单状态列表(前端不用传)
     */
    @ApiModelProperty(value = "订单状态列表(前端不用传)")
    private List<Integer> statusList;

    /**
     * 下单人
     */
    @ApiModelProperty(value = "下单人")
    private String userName;

    /**
     * 下单手机
     */
    @ApiModelProperty(value = "下单手机")
    private String userMobile;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
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
