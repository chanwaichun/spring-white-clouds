package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**clouds/front/business/v1/card/get/share/product
 * @Author: jump
 * @Date: 2023-05-16  21:08
 */
@Data
public class ProductPageListVo {
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
     * 商品类型(1：课程，2：服务，3：实物)
     */
    @ApiModelProperty(value = "商品类型(1：课程，2：服务，3：实物)")
    private Integer productType;
    /**
     * 商品类目名称
     */
    @ApiModelProperty(value = "商品类目名称")
    private String categoryName;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String productId;
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
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;
    /**
     * 商家
     */
    @ApiModelProperty(value = "商家")
    private String businessName;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String img;
    /**
     * 上下架状态(true:上架,false:下架)
     */
    @ApiModelProperty(value = "上下架状态(true:上架,false:下架)")
    private Boolean shelvesStatus;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 售卖次数
     */
    @ApiModelProperty(value = "售卖次数")
    private Integer saleNum;
    /**
     * 推荐理由
     */
    @ApiModelProperty(value = "推荐理由")
    private String sellReason;

    /**
     * 小红书状态 (true：是，false：否)
     */
    @ApiModelProperty(value = "小红书状态 (true：是，false：否)")
    private Boolean xhsSkuStatus;
}
