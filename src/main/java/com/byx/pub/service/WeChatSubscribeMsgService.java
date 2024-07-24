package com.byx.pub.service;

import com.byx.pub.plus.entity.SmsgAuthRecord;

/**
 * @Author Jump
 * @Date 2023/9/2 17:38
 */
public interface WeChatSubscribeMsgService {

    /**
     * 新增授权信息
     * @param adminId
     * @param tmplId
     * @param alwaysStatus
     */
    void addOrUpdate(String adminId,String tmplId,Boolean alwaysStatus);

    /**
     * 获取咨询师模板授权信息
     * @param adminId
     * @param tmplId
     * @return
     */
    SmsgAuthRecord getRecordByAdminId(String adminId, String tmplId);

    /**
     * 用户下单订阅消息推送
     * @param orderSn
     */
    void sendUserPayOrderMsg(String orderSn);

}
