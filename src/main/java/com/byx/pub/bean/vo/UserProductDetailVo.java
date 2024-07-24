package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 消费者查看商品详情vo
 * @Author: jump
 * @Date: 2023-05-22  22:44
 */
@Data
public class UserProductDetailVo {

    /**
     * 咨询师商品id(关系表id)
     */
    @ApiModelProperty(value = "咨询师商品id(关系表id)")
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
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String trueName;


    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userImg;

    /**
     * 咨询师职称
     */
    @ApiModelProperty(value = "咨询师职称")
    private String adminTitle = "资深心理咨询师";



}
