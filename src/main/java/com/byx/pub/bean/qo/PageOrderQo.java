package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 订单分页列表请求参数
 * @author Jump
 * @date 2023/5/31 16:39:17
 */
@Accessors(chain = true)
@Data
public class PageOrderQo {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 咨询师名称
     */
    @ApiModelProperty(value = "咨询师名称")
    private String adminName;

    /**
     * 订单状态(0->已取消，1->待支付，2->已支付，3->已完成)
     */
    @ApiModelProperty(value = "订单状态(0->已取消，1->待支付，2->已支付，3->已完成)")
    private Integer status;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 用户手机
     */
    @ApiModelProperty(value = "用户手机")
    private String userMobile;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date createStart;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date createEnd;

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
