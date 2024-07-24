package com.byx.pub.bean.xhs;

import lombok.Data;

/**
 * @Author Jump
 * @Date 2024/1/5 22:52
 */
@Data
public class XhsCarriageTemplateVo {
    /**
     * 模板id
     */
    private String templateId;

    /**
     * 模板名
     */
    private String templateName;

    /**
     * 运费模板类型 1-自定义邮费 2-卖家承担邮费
     */
    private String templateType;

    /**
     * 计费类型 1-按件计费 2-按重量计费
     */
    private String costType;

}
