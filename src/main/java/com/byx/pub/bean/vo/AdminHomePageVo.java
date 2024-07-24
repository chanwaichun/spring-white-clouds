package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @Author Jump
 * @Date 2023/8/16 23:11
 */
@Data
@Accessors(chain = true)
public class AdminHomePageVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String adminId;

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
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家简称
     */
    @ApiModelProperty(value = "商家简称")
    private String shortName;

    /**
     * 商家全称
     */
    @ApiModelProperty(value = "商家全称")
    private String fullName;

    /**
     * 套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)
     */
    @ApiModelProperty(value = "套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)")
    private Integer suitType;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

}
