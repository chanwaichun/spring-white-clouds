package com.byx.pub.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Jump
 * @Date 2023/6/17 14:43
 */
@Data
@Accessors(chain = true)
public class SelectAdminListVo {
    /**
     * 咨询师id
     */
    private String adminId;
    /**
     * 咨询师名字
     */
    private String trueName;

}
