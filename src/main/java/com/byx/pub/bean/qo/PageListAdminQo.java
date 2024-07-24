package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Jump
 * @date 2023/5/7 16:03:07
 */
@Data
@Accessors(chain = true)
public class PageListAdminQo {

    /**
     * 商家id(前端不用传)
     */
    @ApiModelProperty(value = "商家id(前端不用传)")
    private String businessId;

    /**
     * 角色列表(前端不传)
     */
    @ApiModelProperty(value = "角色列表(前端不传)")
    private List<Integer> ruleList;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String adminName;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "页面条数最小值为1")
    @NotNull(message = "页面条数不能为空")
    @ApiModelProperty(value = "页面大小", required = true)
    private Integer pageSize;
    /**
     * 页码
     */
    @Min(value = 1, message = "页码最小值为1")
    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

}
