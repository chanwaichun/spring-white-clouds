package com.byx.pub.bean.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分页查询订单列表qo
 * @author Jump
 * @date 2023/5/29 17:59:41
 */

@Accessors(chain = true)
@Data
public class FrontPageOrdersQo {
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id(前端不必传)")
    private String businessId;
    /**
     * 咨询师id
     */
    @ApiModelProperty(value = "咨询师id(前端不必传)")
    private String adminId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id(前端不必传)")
    private String userId;

    /**
     * 管理后台角色:1->管理员,2->咨询师
     */
    @ApiModelProperty(value = "管理后台角色(前端不必传)")
    String adminRole;

    /**
     * 支付状态(0->未支付，1->部分支付，2->完成支付，3->退款单)
     */
    @ApiModelProperty(value = "支付状态(0->未支付，1->部分支付，2->完成支付，3->退款单)")
    private Integer payStatus;

    /**
     * 支付状态(多选)(0->未支付，1->部分支付，2->完成支付)
     */
    @ApiModelProperty(value = "支付状态(多选)(0->未支付，1->部分支付，2->完成支付)")
    private List<Integer> payStatusList;

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
