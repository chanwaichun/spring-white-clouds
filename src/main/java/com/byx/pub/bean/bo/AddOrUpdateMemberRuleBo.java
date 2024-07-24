package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * @Author Jump
 * @Date 2023/10/22 11:52
 */
@Data
public class AddOrUpdateMemberRuleBo {
    /**
     * 用户行为(1：订单状态，2：电子传单，3：购买服务)
     */
    @ApiModelProperty(value = "用户行为(1：订单状态，2：电子传单，3：购买服务)")
    private Integer ruleType;

    /**
     * 行为值(订单状态：0：取消，1：待支付，2：支付定金，4：已完成)，传单id，服务id
     */
    @ApiModelProperty(value = "行为值(订单状态：0：取消，1：待支付，2：支付定金，4：已完成)，传单id，服务id")
    private String ruleValue;

    /**
     * 行为文案(状态名称，传单名称，服务名称)
     */
    @ApiModelProperty(value = "行为文案(状态名称，传单名称，服务名称)")
    private String ruleTxt;

}
