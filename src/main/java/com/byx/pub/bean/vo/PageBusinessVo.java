package com.byx.pub.bean.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Author Jump
 * @Date 2023/7/14 22:26
 */
@Data
@Accessors(chain = true)
public class PageBusinessVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String businessCode;
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
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String telephone;
    /**
     * 商家性质(1：个人，2：机构)
     */
    @ApiModelProperty(value = "商家性质(1：个人，2：机构)")
    private Integer businessType;
    /**
     * 套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)
     */
    @ApiModelProperty(value = "套餐类型(1：个人版，2：小组版，3：团队版，4：企业版，5：社群版，6：合伙人版)")
    private Integer suitType;
    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
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
