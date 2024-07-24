package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 面板商品vo
 * @Author Jump
 * @Date 2023/6/15 22:41
 */
@Data
@Accessors(chain = true)
public class PanelProductVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键(商品详情页用)")
    private String id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String productCode;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * 商品图片分号分隔第一张为首图
     */
    @ApiModelProperty(value = "商品图片分号分隔第一张为首图")
    private String img;

    /**
     * 服务人次(用户去重)
     */
    @ApiModelProperty(value = "服务人次(用户去重)")
    private Integer userBuyNum;

}
