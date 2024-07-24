package com.byx.pub.service;

/**
 * @Author Jump
 * @Date 2023/9/12 20:30
 */
public interface CardLikesService {

    /**
     * 点赞名片
     * @param cardId
     * @param likesUid
     */
    void likesCard(String cardId,String likesUid);
    /**
     * 判断是否点赞过名片
     * @param cardId
     * @param likesUid
     * @return
     */
    Boolean isLikes(String cardId,String likesUid);
}
