package com.byx.pub.bean.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/7/16 0:17
 */
@Data
public class ProductDetailVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id")
    private String adminId;
    /**
     * 数据状态(true:有效,false:删除)
     */
    @ApiModelProperty(value = "数据状态(true:有效,false:删除)")
    private Boolean dataStatus;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 上下架状态(true:上架,false:下架)
     */
    @ApiModelProperty(value = "上下架状态(true:上架,false:下架)")
    private Boolean shelvesStatus;
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;
    /**
     * 商品类型(1：课程，2：服务，3：实物)
     */
    @ApiModelProperty(value = "商品类型(1：课程，2：服务，3：实物)")
    private Integer productType;
    /**
     * 商品类目id
     */
    @ApiModelProperty(value = "商品类目id")
    private String categoryId;
    /**
     * 商品类目名称
     */
    @ApiModelProperty(value = "商品类目名称")
    private String categoryName;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;
    /**
     * 商品副标题(课程)
     */
    @ApiModelProperty(value = "商品副标题(课程)")
    private String subName;
    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String productCode;
    /**
     * 商品(服务)价格
     */
    @ApiModelProperty(value = "商品(服务)价格")
    private BigDecimal price;
    /**
     * 商品图片分号分隔第一张为首图
     */
    @ApiModelProperty(value = "商品图片分号分隔第一张为首图")
    private String img;
    /**
     * 结算规则id
     */
    @ApiModelProperty(value = "结算规则id")
    private String settleRules;
    /**
     * 服务次数、服务人数、库存
     */
    @ApiModelProperty(value = "服务次数、服务人数、库存")
    private Integer serviceNum;
    /**
     * 服务时长(课程)
     */
    @ApiModelProperty(value = "服务时长(课程)")
    private Integer serviceDuration;
    /**
     * 招生开始日期
     */
    @ApiModelProperty(value = "招生开始日期")
    private LocalDate enrollStart;
    /**
     * 招生结束日期
     */
    @ApiModelProperty(value = "招生结束日期")
    private LocalDate enrollEnd;
    /**
     * 服务开始日期
     */
    @ApiModelProperty(value = "服务开始日期")
    private LocalDate serviceStart;
    /**
     * 服务结束日期
     */
    @ApiModelProperty(value = "服务结束日期")
    private LocalDate serviceEnd;
    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String productDetail;

    /**
     * 关联名片
     */
    @ApiModelProperty(value = "关联名片")
    private String cardId;
    /**
     * 名片名称
     */
    @ApiModelProperty(value = "名片名称")
    private String cardName;
    /**
     * 名片头像
     */
    @ApiModelProperty(value = "名片头像")
    private String cardImg;
    /**
     * 名片联系方式
     */
    @ApiModelProperty(value = "名片联系方式")
    private String cardPhone;
    /**
     * 服务人次(用户去重)
     */
    @ApiModelProperty(value = "服务人次(用户去重)")
    private Integer userBuyNum;
    /**
     * 售卖次数
     */
    @ApiModelProperty(value = "售卖次数")
    private Integer saleNum;
}
