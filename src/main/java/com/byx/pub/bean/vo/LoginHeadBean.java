package com.byx.pub.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jump
 * @date 2020/11/27 19:31
 */
@Accessors(chain = true)
@Data
public class LoginHeadBean {

    /**
     * 客户端类型:manage->管理后端,front->前端
     */
    @ApiModelProperty(value = "客户端类型:manage->管理后端,front->前端")
    String clientType;

/****************************************************************************************************************************************************************************/

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    String userId;

    /**
     * 当前角色:1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者
     */
    @ApiModelProperty(value = "当前角色:1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者")
    String userRole;

    /**
     * 是否商家:true->是,false->否
     */
    @ApiModelProperty(value = "是否商家:true->是,false->否")
    Boolean isBusiness = Boolean.FALSE;

/****************************************************************************************************************************************************************************/

    /**
     * 管理后台id
     */
    @ApiModelProperty(value = "管理后台id")
    String adminId;

    /**
     * 管理后台角色:1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人
     */
    @ApiModelProperty(value = "管理后台角色:1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人")
    String adminRole;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private String businessId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String businessName;

}
