package com.byx.pub.service.impl;

import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.plus.dao.CardLikesRecordPlusDao;
import com.byx.pub.plus.entity.AdminCard;
import com.byx.pub.plus.entity.CardLikesRecord;
import com.byx.pub.plus.entity.User;
import com.byx.pub.service.AdminCardService;
import com.byx.pub.service.CardLikesService;
import com.byx.pub.service.FrontUserService;
import com.byx.pub.service.InteractiveClickService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/9/12 20:29
 */
@Service
@Slf4j
public class CardLikesServiceImpl implements CardLikesService {
    @Resource
    CardLikesRecordPlusDao likesRecordPlusDao;
    @Resource
    FrontUserService userService;
    @Resource
    AdminCardService cardService;
    @Resource
    InteractiveClickService clickService;

    /**
     * 点赞名片
     * @param cardId
     * @param likesUid
     */
    public void likesCard(String cardId,String likesUid){
        Boolean likes = this.isLikes(cardId, likesUid);
        if(likes){
            return;
        }
        CardLikesRecord record = new CardLikesRecord();
        record.setCardId(cardId).setLikedUid(likesUid);
        //查询名片信息
        AdminCard card = this.cardService.getAdminCardById(cardId);
        if(Objects.nonNull(card)){
            User userByAdminId = this.userService.getUserByAdminId(card.getAdminId());
            if(Objects.nonNull(userByAdminId)){
                record.setLikedUid(userByAdminId.getId())
                        .setLikedName(userByAdminId.getNickName())
                        .setLikedImg(userByAdminId.getUserImg());
            }
        }
        User user = this.userService.getUserById(likesUid);
        if(Objects.nonNull(user)){
            record.setLikesUid(user.getId())
                    .setLikesName(user.getNickName())
                    .setLikesImg(user.getUserImg());
        }
        this.likesRecordPlusDao.save(record);
        clickService.saveLog(cardId,PromotionTypeEnum.CARD_LIKES.getValue(),record.getLikedUid(),likesUid,card.getBusinessId());
    }

    /**
     * 判断是否点赞过名片
     * @param cardId
     * @param likesUid
     * @return
     */
    public Boolean isLikes(String cardId,String likesUid){
        CardLikesRecord likesRecord = this.likesRecordPlusDao.lambdaQuery()
                .eq(CardLikesRecord::getDataStatus, Boolean.TRUE)
                .eq(CardLikesRecord::getCardId, cardId)
                .eq(CardLikesRecord::getLikesUid, likesUid).last("limit 1").one();
        if(Objects.nonNull(likesRecord)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



}
