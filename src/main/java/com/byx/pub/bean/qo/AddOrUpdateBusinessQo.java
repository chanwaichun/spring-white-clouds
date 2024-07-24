package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @Author Jump
 * @Date 2023/7/14 22:11
 */
@Data
public class AddOrUpdateBusinessQo {
    /**
     * 主键(修改必传)
     */
    @ApiModelProperty(value = "主键(修改必传)")
    private String id;
    /**
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    @NotEmpty(message = "请填写商家名称")
    @Size(max = 10,message = "商家名称最大10个字符")
    private String shortName;
    /**
     * 商家全称
     */
    @Size(max = 100,message = "商家全称最大100个字符")
    @ApiModelProperty(value = "商家全称")
    private String fullName;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "请填写联系方式")
    @Size(max = 11,message = "联系方式最大11个字符")
    private String telephone;

    /**
     * 商家性质(1：个人，2：机构)
     */
    @ApiModelProperty(value = "商家性质(1：个人，2：机构)")
    @NotNull(message = "请选择商家性质")
    private Integer businessType;

    /**
     * 套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)
     */
    @ApiModelProperty(value = "套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)")
    @NotNull(message = "请选择套餐类型")
    private Integer suitType;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    @NotNull(message = "请选择开始日期")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @NotNull(message = "请选择结束日期")
    private LocalDate endDate;

    /**
     * 企业微信corpid
     */
    @ApiModelProperty(value = "企业微信corpid")
    private String corpId;

    /**
     * 企业微信Secret
     */
    @ApiModelProperty(value = "企业微信Secret")
    private String secretSn;




}
