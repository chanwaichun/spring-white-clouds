package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 新增或修改商品信息Qo
 * @author Jump
 * @date 2023/5/16 17:24:48
 */
@Data
public class AddOrUpdateProductQo {
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 商品类型(1：课程，2：服务，3：实物)
     */
    @ApiModelProperty(value = "商品类型(1：课程，2：服务，3：实物)")
    @Range(min = 1,max = 3,message = "请选择商品类型")
    private Integer productType;
    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    @Size(max = 60, message = "商品标题长度最多60个字符")
    @NotEmpty(message = "商品标题不能为空")
    private String productName;
    /**
     * 商品副标题(课程)
     */
    @ApiModelProperty(value = "商品副标题(课程)")
    @Size(max = 20, message = "商品副标题长度最多20个字符")
    private String subName;
    /**
     * 商品类目id
     */
    @ApiModelProperty(value = "商品类目id")
    @NotEmpty(message = "请选择商品分类")
    private String categoryId;
    /**
     * 结算规则id
     */
    @ApiModelProperty(value = "结算规则id")
    private String settleRules;
    /**
     * 商品图片分号分隔第一张为首图
     */
    @ApiModelProperty(value = "商品图片分号分隔第一张为首图")
    @Size(max = 500, message = "商品图片长度最多500个字符")
    @NotEmpty(message = "商品图片不能为空")
    private String img;
    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    @NotNull(message = "商品价格不能为空")
    @Digits(integer = 7, fraction = 2, message = "整数上限是7位，小数上限是2位")
    private BigDecimal price;
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

}
