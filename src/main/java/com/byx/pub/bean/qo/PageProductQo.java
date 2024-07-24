package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: jump
 * @Date: 2023-05-16  21:12
 */
@Data
@Accessors(chain = true)
public class PageProductQo {
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id(前端不用传)")
    private String businessId;
    /**
     * 商品类型-小程序(1：课程，2：服务，3：实物)
     */
    @ApiModelProperty(value = "商品类型(1：课程，2：服务，3：实物)")
    private Integer productType;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商家
     */
    @ApiModelProperty(value = "商家")
    private String businessName;

    /**
     * 上下架状态(true:上架,false:下架)
     */
    @ApiModelProperty(value = "上下架状态(true:上架,false:下架)")
    private Boolean shelvesStatus;

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
