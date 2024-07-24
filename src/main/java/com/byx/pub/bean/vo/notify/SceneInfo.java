package com.byx.pub.bean.vo.notify;

import lombok.Data;

/**
 * 支付场景信息描述
 * @author Jump
 */
@Data
public class SceneInfo {
    /**
     * 终端设备号（门店号或收银设备ID）。
     * 示例值：013467007045764
     */
    private String device_id;
}