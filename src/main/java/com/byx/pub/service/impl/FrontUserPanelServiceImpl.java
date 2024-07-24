package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.mapper.OrdersSqlMapper;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.AdminCardService;
import com.byx.pub.service.AdminService;
import com.byx.pub.service.FrontUserPanelService;
import com.byx.pub.service.FrontUserService;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户面板服务
 * @Author Jump
 * @Date 2023/6/15 22:38
 */
@Service
@Slf4j
public class FrontUserPanelServiceImpl implements FrontUserPanelService {
    @Resource
    ProductPlusDao productPlusDao;
    @Resource
    HotInfoPlusDao hotInfoPlusDao;
    @Resource
    OrdersSqlMapper ordersSqlMapper;
    @Resource
    HotAdminPlusDao hotAdminPlusDao;
    @Resource
    AdminCardService adminCardService;
    @Resource
    FrontUserService userService;

    /**
     * 获取首页商品列表
     * 默认展示销售人数最多的商品
     * 销售人数相同，展示最新的服务/商品
     * @return
     */
    @Override
    public List<PanelProductVo> queryPanelProducts(String userRole,String businessId){
        //如果是客户和平台人员 不展示推荐商品
        if(Integer.parseInt(userRole) < 3){
            return new ArrayList<>();
        }
        //如果是普通商家 查询自己的商品
        LambdaQueryWrapper<Product> where = new LambdaQueryWrapper<>();
        where.eq(Product::getDataStatus, Boolean.TRUE);
        where.eq(Product::getShelvesStatus, Boolean.TRUE);
        where.eq(Product::getBusinessId,businessId);
        where.orderByDesc(Product::getSaleNum).orderByDesc(Product::getCreateTime);
        IPage<Product> page = this.productPlusDao.page(new Page<>(1, 8), where);
        if(0 == page.getTotal()){
            return new ArrayList<>();
        }
        return CopyUtil.copyList(page.getRecords(),PanelProductVo.class);

        /*List<Product> list = this.productPlusDao.lambdaQuery()
                .eq(Product::getDataStatus, Boolean.TRUE).eq(Product::getShelvesStatus, Boolean.TRUE)
                .orderByDesc(Product::getSaleNum).orderByDesc(Product::getCreateTime)
                .last("limit 8").list();
        return CopyUtil.copyList(list,PanelProductVo.class);*/
    }

    /**
     * 获取资讯推荐
     * @return
     */
    @Override
    public List<HotInfo> queryHotInfoList(){
        return this.hotInfoPlusDao.lambdaQuery().eq(HotInfo::getDataStatus,Boolean.TRUE)
                .orderByDesc(HotInfo::getUpdateTime).list();
    }

    /**
     * 查询用户下过单的咨询师列表
     * @param userId
     * @return
     */
    public List<AdminShortCardVo> queryUserByOrderAdminList(String userId){
        return this.ordersSqlMapper.queryUserByOrderAdminList(userId);
    }

    /**
     * 获取推荐咨询师
     * 由晓东提供
     * @return
     */
    public List<ListHotAdminCardVo> getHostAdminList(String userRole,String businessId){
        List<ListHotAdminCardVo> resList = new ArrayList<>();
        //如果是商家 则 查询自己商家下的名片展示
        if(Integer.parseInt(userRole) > 2){
            //查询自己商家的名片
            List<AdminCard> list = this.adminCardService.getCardListBySj(businessId);
            if(!CollectionUtils.isEmpty(list)){
                for(AdminCard adminCard : list){
                    ListHotAdminCardVo vo = new ListHotAdminCardVo();
                    BeanUtils.copyProperties(adminCard,vo);
                    vo.setCardId(adminCard.getId());
                    //获取证书列表
                    List<CardPaper> paperList = adminCardService.getPaperList(adminCard.getId(),null);
                    if(!CollectionUtils.isEmpty(paperList)){
                        vo.setPapers(paperList);
                    }
                    User user = this.userService.getUserByAdminId(adminCard.getAdminId());
                    if(Objects.nonNull(user)){
                        vo.setAdminPhoto(user.getUserImg());
                    }
                    resList.add(vo);
                }
                return resList;
            }
        }
        //如果是普通用户或平台员工 或者 商家没有名片 则展示推荐名片
        List<HotAdmin> list = this.hotAdminPlusDao.lambdaQuery().orderByAsc(HotAdmin::getSortNum).list();
        if(CollectionUtils.isEmpty(list)){
            return resList;
        }
        for(HotAdmin hotAdmin :list){
            ListHotAdminCardVo vo = new ListHotAdminCardVo();
            //查询名片信息
            AdminCard card = this.adminCardService.getAdminCardById(hotAdmin.getCardId());
            if(Objects.nonNull(card)){
                BeanUtils.copyProperties(card,vo);
                vo.setCardId(card.getId());
                //获取证书列表
                List<CardPaper> paperList = adminCardService.getPaperList(card.getId(),null);
                if(!CollectionUtils.isEmpty(paperList)){
                    vo.setPapers(paperList);
                }
                User user = this.userService.getUserByAdminId(card.getAdminId());
                if(Objects.nonNull(user)){
                    vo.setAdminPhoto(user.getUserImg());
                }
                resList.add(vo);
            }
        }
        return resList;
    }

    /**
     * 查询名片信息
     * @param cardQo
     * @return
     */
    public Page<PageSearchCardVo> searchAdminCard(PageAdminCardQo cardQo){
        Page<PageSearchCardVo> resPage = new Page<>(cardQo.getPageNum(),cardQo.getPageSize());
        //条件分页查询
        if(StringUtil.isEmpty(cardQo.getTrueName().trim())){//如果搜索条件为空返回空
            return resPage;
        }
        Page<PageAdminCardVo> page = this.adminCardService.pageListCard(cardQo);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        List<PageSearchCardVo> resList = new ArrayList<>();
        //循环处理数据
        for(PageAdminCardVo adminCard : page.getRecords()){
            PageSearchCardVo vo = new PageSearchCardVo();
            BeanUtils.copyProperties(adminCard,vo);
            vo.setCardId(adminCard.getId());
            //获取证书列表
            List<CardPaper> paperList = adminCardService.getPaperList(adminCard.getId(),null);
            if(!CollectionUtils.isEmpty(paperList)){
                vo.setPapers(paperList);
            }
            //获取标签列表
            List<CardTag> cardTags = adminCardService.getCardTags(adminCard.getId());
            if(!CollectionUtils.isEmpty(cardTags)){
                vo.setTagList(cardTags);
            }
            //谁推荐过他
            List<AdminCard> recommendMeList = this.adminCardService.getCardByRecommendMe(adminCard.getId());
            if(!CollectionUtils.isEmpty(recommendMeList)){
                AdminCard recommendMe = recommendMeList.get(0);
                vo.setReference(StringUtil.notEmpty(recommendMe.getNickName())?recommendMe.getNickName():recommendMe.getTrueName());
            }
            resList.add(vo);
        }
        resPage.setRecords(resList);
        return resPage;
    }






}
