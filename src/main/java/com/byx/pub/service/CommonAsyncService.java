package com.byx.pub.service;

import org.springframework.scheduling.annotation.Async;

/**
 * @Author Jump
 * @Date 2023/10/22 12:55
 */
public interface CommonAsyncService {
    /**
     * 给会员分组跑数据
     * @param groupId
     */
    @Async
    void runningGroupMember(String groupId);
}
