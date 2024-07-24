package com.byx.pub.bean.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jump
 * @date 2023/8/16 17:42:14
 */
@Data
public class HelpListVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 帮助者Id(admin表id)
     */
    @ApiModelProperty(value = "帮助者Id(admin表id)")
    private String helperId;

    /**
     * 帮助者名称
     */
    @ApiModelProperty(value = "帮助者名称")
    private String helper;

    /**
     * 帮助者头像
     */
    @ApiModelProperty(value = "帮助者头像")
    private String helperImg;

    /**
     * 帮助TA人数
     */
    @ApiModelProperty(value = "帮助TA人数")
    private Integer helpTo;

    /**
     * 受益者Id(admin表id)
     */
    @ApiModelProperty(value = "受益者Id(admin表id)")
    private String beneficiaryId;

    /**
     * 受益者名称
     */
    @ApiModelProperty(value = "受益者名称")
    private String beneficiary;

    /**
     * 受益者头像
     */
    @ApiModelProperty(value = "受益者头像")
    private String beneficiaryImg;

    /**
     * TA帮我的数量
     */
    @ApiModelProperty(value = "TA帮我的数量")
    private Integer toHelp;


}
