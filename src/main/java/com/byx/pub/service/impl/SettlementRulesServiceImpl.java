package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.SaveRulesRangeBo;
import com.byx.pub.bean.bo.SaveSettlementRulesBo;
import com.byx.pub.bean.qo.PageSettlementRulesQo;
import com.byx.pub.bean.vo.PageBusinessVo;
import com.byx.pub.bean.vo.PageSettlementRulesVo;
import com.byx.pub.enums.SystemFinalCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.SettlementRulesPlusDao;
import com.byx.pub.plus.dao.SettlementRulesRangePlusDao;
import com.byx.pub.plus.entity.SettlementRules;
import com.byx.pub.plus.entity.SettlementRulesRange;
import com.byx.pub.service.BusinessService;
import com.byx.pub.service.SettlementRulesService;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 结算规则服务
 * @author Jump
 * @date 2023/8/18 14:36:45
 */
@Slf4j
@Service
public class SettlementRulesServiceImpl implements SettlementRulesService {
    @Resource
    SettlementRulesPlusDao rulesPlusDao;
    @Resource
    SettlementRulesRangePlusDao rangePlusDao;
    @Resource
    BusinessService businessService;

    /**
     * 新增或修改商家规则
     * @param rulesBo
     */
    public void saveSjSettlementRules(SaveSettlementRulesBo rulesBo){
        //校验 结算类型目标id不能重复
        checkBo(rulesBo.getRulesRangeBoList());
        //保存规则
        String ruleId = rulesBo.getId();
        PageBusinessVo sjInfo = businessService.getSjInfo(rulesBo.getBusinessId());
        SettlementRules rules = new SettlementRules();
        BeanUtils.copyProperties(rulesBo,rules);
        rules.setShortName(sjInfo.getShortName());
        //新增
        if(StringUtil.isEmpty(rulesBo.getId())){
            this.rulesPlusDao.save(rules);
            ruleId = rules.getId();
        }else{
            this.rulesPlusDao.updateById(rules);
        }
        //保存规则范围(类似于先删除 再插入，但保留之前的配置)
        this.rangePlusDao.lambdaUpdate()
                .set(SettlementRulesRange::getDataStatus,Boolean.FALSE)
                .eq(SettlementRulesRange::getRuleId,ruleId)
                .update();

        List<SaveRulesRangeBo> rulesRangeBoList = rulesBo.getRulesRangeBoList();
        if(CollectionUtils.isEmpty(rulesRangeBoList)){
            return;
        }
        List<SettlementRulesRange> rangeList = new ArrayList<>();
        for(SaveRulesRangeBo rangeBo : rulesRangeBoList){
            SettlementRulesRange range = new SettlementRulesRange();
            BeanUtils.copyProperties(rangeBo,range);
            range.setRuleId(ruleId);
            rangeList.add(range);
        }
        this.rangePlusDao.saveBatch(rangeList);
    }

    /**
     * 校验范围
     * @param list
     */
    public void checkBo(List<SaveRulesRangeBo> list){
        if(CollectionUtils.isEmpty(list)){
            throw new ApiException("请至少填写一个结算类型");
        }
        Map<String,String> targetMap = new HashMap<>();
        BigDecimal shareRate = BigDecimal.ZERO;
        for(SaveRulesRangeBo bo : list){
            //1.拉新
            if(targetMap.containsKey("1"+bo.getTargetId())){
                throw new ApiException("同一目标不能设置多个拉新规则");
            }else{
                targetMap.put("1"+bo.getTargetId(),"1");
            }
            //2.促成
            if(targetMap.containsKey("2"+bo.getTargetId())){
                throw new ApiException("同一目标不能设置多个促成规则");
            }else{
                targetMap.put("2"+bo.getTargetId(),"2");
            }
            //3.其他
            if(targetMap.containsKey("3"+bo.getTargetId())){
                throw new ApiException("同一目标不能设置多个其他规则");
            }else{
                targetMap.put("3"+bo.getTargetId(),"3");
            }
            shareRate = shareRate.add(bo.getShareRate());
        }
        //判断分成比例是否大于100
        if(SystemFinalCode.ONE_HUNDRED.compareTo(shareRate) < 0){
            throw new ApiException("分成比例累加不能大于100%");
        }
    }



    /**
     * 分页条件查询
     * @param qo
     * @return
     */
    public Page<PageSettlementRulesVo> pageList(PageSettlementRulesQo qo){
        Page<PageSettlementRulesVo> resPage = new Page<>();
        QueryWrapper<SettlementRules> where = new QueryWrapper<>();
        where.lambda().eq(SettlementRules::getDataStatus,Boolean.TRUE);
        if(StringUtil.notEmpty(qo.getBusinessId())){
            where.lambda().eq(SettlementRules::getBusinessId,qo.getBusinessId());
        }
        if(StringUtil.notEmpty(qo.getShortName())){
            where.lambda().like(SettlementRules::getShortName,qo.getShortName());
        }
        if(StringUtil.notEmpty(qo.getRuleName())){
            where.lambda().like(SettlementRules::getRuleName,qo.getRuleName());
        }
        if(StringUtil.notEmpty(qo.getStartTime())){
            where.lambda().ge(SettlementRules::getCreateTime,qo.getStartTime());
        }
        if(StringUtil.notEmpty(qo.getEndTime())){
            where.lambda().le(SettlementRules::getCreateTime,qo.getEndTime());
        }
        where.lambda().orderByDesc(SettlementRules::getCreateTime);
        IPage<SettlementRules> page = this.rulesPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        resPage.setRecords(CopyUtil.copyList(page.getRecords(),PageSettlementRulesVo.class));
        return resPage;
    }

    /**
     * 获取规则详情
     * @param id
     * @return
     */
    public SaveSettlementRulesBo getRulesDetail(String id){
        SaveSettlementRulesBo resVo = new SaveSettlementRulesBo();
        //规则信息
        SettlementRules rules = this.rulesPlusDao.getById(id);
        if(Objects.isNull(rules)){
            throw new ApiException("未找到数据信息");
        }
        BeanUtils.copyProperties(rules,resVo);
        //范围列表
        List<SaveRulesRangeBo> resRangeList = new ArrayList<>();
        List<SettlementRulesRange> list = this.rangePlusDao.lambdaQuery()
                .eq(SettlementRulesRange::getDataStatus,Boolean.TRUE)
                .eq(SettlementRulesRange::getRuleId, id)
                .orderByDesc(SettlementRulesRange::getId).list();
        if(CollectionUtils.isEmpty(list)){
            resVo.setRulesRangeBoList(resRangeList);
            return resVo;
        }
        resVo.setRulesRangeBoList(CopyUtil.copyList(list,SaveRulesRangeBo.class));
        return resVo;
    }




}
