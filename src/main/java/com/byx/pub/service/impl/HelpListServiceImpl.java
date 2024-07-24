package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddHelpRecordBo;
import com.byx.pub.bean.qo.PageHelpListQo;
import com.byx.pub.bean.vo.HelpListVo;
import com.byx.pub.bean.vo.PageHomeHelpVo;
import com.byx.pub.bean.vo.PageHomeHelperVo;
import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.plus.dao.HelpListPlusDao;
import com.byx.pub.plus.dao.HelpListRecordPlusDao;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Jump
 * @date 2023/8/16 15:17:25
 */
@Slf4j
@Service
public class HelpListServiceImpl implements HelpListService {
    @Resource
    HelpListPlusDao helpListPlusDao;
    @Resource
    HelpListRecordPlusDao recordPlusDao;
    @Resource
    AdminService adminService;
    @Resource
    FrontUserService userService;
    @Resource
    InteractiveClickService clickService;
    @Resource
    AdminCardService adminCardService;
    @Resource
    ProductService productService;

    /**
     * 保存互帮信息(记录保存，次数去重)
     * @param bo
     */
    public void saveHelpRecord(AddHelpRecordBo bo){
        //如果帮助人和受益人是同一人 不存
        if(bo.getHelperId().equals(bo.getBeneficiaryId())){
            return;
        }
        //记录点击数据
        if(1 == bo.getHelpType()){
            //查询名片信息
            AdminCard adminCard = this.adminCardService.getAdminCardById(bo.getHelperId());
            if(Objects.isNull(adminCard)){
                return;
            }
            clickService.saveLog(bo.getHelpId(),PromotionTypeEnum.SHARE_CARD.getValue()
                    ,bo.getBeneficiaryId(),bo.getUserId(),adminCard.getBusinessId());
        }else{
            //查询商品信息
            Product product = this.productService.getProductDbById(bo.getHelperId());
            if(Objects.isNull(product)){
                return;
            }
            clickService.saveLog(bo.getHelpId(),PromotionTypeEnum.SHARE_GOODS.getValue()
                    ,bo.getBeneficiaryId(),bo.getUserId(),product.getBusinessId());
        }
        //获取 帮助者 和 受惠者 信息
        Admin helper = this.adminService.getAdminById(bo.getHelperId());
        if(Objects.isNull(helper)){
            return;
        }
        Admin beneficiary = this.adminService.getAdminById(bo.getBeneficiaryId());
        if(Objects.isNull(beneficiary)){
            return;
        }
        //获取 客户 信息
        User user = this.userService.getUserById(bo.getUserId());
        if(Objects.isNull(user)){
            return;
        }
        //1.保存记录表
        HelpListRecord addRecord = new HelpListRecord();
        BeanUtils.copyProperties(bo,addRecord);
        addRecord.setHelper(helper.getTrueName()).setNickName(user.getNickName()).setBeneficiary(beneficiary.getTrueName());
        this.recordPlusDao.save(addRecord);
        //2.查插互帮主表 没有帮助过 肯定是不存在需要去重
        HelpList helpTo = getHelpByHelper(bo.getHelperId(), bo.getBeneficiaryId());
        //新增
        if(Objects.isNull(helpTo)){
            HelpList help = new HelpList();
            BeanUtils.copyProperties(bo,help);
            help.setHelper(helper.getTrueName()).setHelperImg(helper.getUserImg())
                    .setBeneficiary(beneficiary.getTrueName()).setBeneficiaryImg(beneficiary.getUserImg())
                    .setHelpTo(1);
            //反查他帮过我的
            HelpList toHelp = getHelpByHelper(bo.getBeneficiaryId(),bo.getHelperId());
            if(Objects.nonNull(toHelp)){
                help.setToHelp(toHelp.getHelpTo());
            }
            this.helpListPlusDao.save(help);
            return;
        }
        //修改 去重已经被引流过的客户 记录保存
        Integer num = getHelpRecordByUserId(bo.getHelperId(),
                bo.getBeneficiaryId(), bo.getUserId());
        if(0 < num){
            return;
        }
        HelpList updateHelpTo = new HelpList();
        updateHelpTo.setId(helpTo.getId()).setHelpTo(helpTo.getHelpTo()+1);
        //反查他帮过我的
        HelpList toHelp = getHelpByHelper(bo.getBeneficiaryId(),bo.getHelperId());
        if(Objects.nonNull(toHelp)){
            updateHelpTo.setToHelp(toHelp.getHelpTo());
        }
        this.helpListPlusDao.updateById(updateHelpTo);
    }

    /**
     * 获取互帮榜信息
     * @param helperId
     * @param beneficiaryId
     * @return
     */
    public HelpList getHelpByHelper(String helperId,String beneficiaryId){
        return this.helpListPlusDao.lambdaQuery()
                .eq(HelpList::getDataStatus, Boolean.TRUE)
                .eq(HelpList::getHelperId, helperId)
                .eq(HelpList::getBeneficiaryId, beneficiaryId)
                .last("limit 1").one();
    }


    /**
     * 获取引流人
     * @param helperId
     * @param beneficiaryId
     * @param userId
     * @return
     */
    public Integer getHelpRecordByUserId(String helperId,String beneficiaryId,String userId){
        return this.recordPlusDao.lambdaQuery()
                .eq(HelpListRecord::getHelperId,helperId)
                .eq(HelpListRecord::getBeneficiaryId,beneficiaryId)
                .eq(HelpListRecord::getUserId,userId)
                .count();
    }

    /**
     * 首页互帮榜信息
     * @param adminId
     * @return
     */
    public PageHomeHelpVo getPageHomeHelpVo(String adminId){
        PageHomeHelpVo resVo = new PageHomeHelpVo();
        //1.查询 帮我 最多的三人
        List<HelpList> beneficiaryList = getHelpListByBeneficiaryId(adminId);
        if(!CollectionUtils.isEmpty(beneficiaryList)){
            resVo.setBeneficiaryVos(CopyUtil.copyList(beneficiaryList,PageHomeHelperVo.class));
        }
        //2.查询 我帮 最多的三人
        List<HelpList> helpList = getHelpListByHelperId(adminId);
        if(!CollectionUtils.isEmpty(helpList)){
            resVo.setHelpListVos(CopyUtil.copyList(helpList,PageHomeHelperVo.class));
        }
        return resVo;
    }

    /**
     * 帮我 最多的三个人
     * @param beneficiaryId
     * @return
     */
    public List<HelpList> getHelpListByBeneficiaryId(String beneficiaryId){
        return this.helpListPlusDao.lambdaQuery().eq(HelpList::getBeneficiaryId, beneficiaryId)
                .eq(HelpList::getDataStatus, Boolean.TRUE)
                .orderByDesc(HelpList::getHelpTo).last("limit 3").list();
    }

    /**
     * 我帮助 最多的三个人
     * @param helperId
     * @return
     */
    public List<HelpList> getHelpListByHelperId(String helperId){
        return this.helpListPlusDao.lambdaQuery().eq(HelpList::getHelperId, helperId)
                .eq(HelpList::getDataStatus, Boolean.TRUE)
                .orderByDesc(HelpList::getHelpTo).last("limit 3").list();
    }

    /**
     * 分页条件查询互帮榜
     * @param qo
     * @return
     */
    public Page<HelpListVo> pageHelpList(PageHelpListQo qo){
        Page<HelpListVo> resPage = new Page<>();
        QueryWrapper<HelpList> where = new QueryWrapper<>();
        where.lambda().eq(HelpList::getDataStatus,Boolean.TRUE);
        //人助我
        if(Objects.isNull(qo.getHelpType()) || 1 == qo.getHelpType()){
            where.lambda().eq(HelpList::getBeneficiaryId,qo.getAdminId());
        }else{
            where.lambda().eq(HelpList::getHelperId,qo.getAdminId());
        }
        //排序
        if(Objects.isNull(qo.getOrderByType()) || 1 == qo.getOrderByType()){
            if(Objects.isNull(qo.getOrderByRule()) || 1 == qo.getOrderByRule()){
                where.lambda().orderByAsc(HelpList::getToHelp);
            }else {
                where.lambda().orderByDesc(HelpList::getToHelp);
            }
        }else{
            if(Objects.isNull(qo.getOrderByRule()) || 1 == qo.getOrderByRule()){
                where.lambda().orderByAsc(HelpList::getHelpTo);
            }else {
                where.lambda().orderByDesc(HelpList::getHelpTo);
            }
        }
        IPage<HelpList> page = this.helpListPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        resPage.setRecords(CopyUtil.copyList(page.getRecords(),HelpListVo.class));
        return resPage;
    }

}
