package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jump
 * @Date 2023/7/31 21:15
 */
@Data
public class CardPaperBo {
    /**
     * 证书类型(1：官方证明，2：公司证明，3：其他证明)
     */
    @ApiModelProperty(value = "证书类型(1：官方证明，2：公司证明，3：其他证明)")
    private Integer paperType;
    /**
     * 证书名称
     */
    @ApiModelProperty(value = "证书名称")
    private String paperName;
    /**
     * 证书文件名
     */
    @ApiModelProperty(value = "证书文件名")
    private String imgName;
    /**
     * 证书文件路径
     */
    @ApiModelProperty(value = "证书文件路径")
    private String imgUrl;
    /**
     * 证书审核状态(1：待审核，2：审核不通过，3：已审核)
     */
    @ApiModelProperty(value = "证书审核状态(1：待审核，2：审核不通过，3：已审核)")
    private Integer paperStatus;
    /**
     * 拒审理由
     */
    @ApiModelProperty(value = "拒审理由")
    private String auditReason;
}
