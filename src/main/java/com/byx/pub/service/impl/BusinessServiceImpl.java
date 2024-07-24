package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateBusinessQo;
import com.byx.pub.bean.qo.PageBusinessQo;
import com.byx.pub.bean.vo.PageBusinessVo;
import com.byx.pub.bean.vo.SelectBusinessListVo;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.BusinessPlusDao;
import com.byx.pub.plus.entity.Business;
import com.byx.pub.service.BusinessService;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商家服务
 * @Author Jump
 * @Date 2023/7/14 22:08
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Resource
    BusinessPlusDao businessPlusDao;

    /**
     * 新增或修改商家
     * @param qo
     */
    @Override
    public void addOrUpdateBusiness(AddOrUpdateBusinessQo qo){
        //TODO 新增商家同时要 配置企业微信信息 corpid 和 corpsecret 并且 调用企业微信接口创建 根部门
        //新增
        if(StringUtil.isEmpty(qo.getId())){
            Business business = new Business();
            BeanUtils.copyProperties(qo,business);
            business.setBusinessCode(StringUtil.getSystemYwCode("SJ"));
            this.businessPlusDao.save(business);
            return;
        }
        //修改
        Business oldBusiness = this.businessPlusDao.getById(qo.getId());
        if(Objects.isNull(oldBusiness)){
            throw new ApiException("未找到商家信息");
        }
        this.businessPlusDao.updateById(CopyUtil.copy(qo,Business.class));
    }

    /**
     * 分页查询商家列表
     * @param qo
     * @return
     */
    @Override
    public Page<PageBusinessVo> pageList(PageBusinessQo qo){
        Page<PageBusinessVo> resPage = new Page<>();
        QueryWrapper<Business> where = new QueryWrapper<>();
        where.lambda().eq(Business::getDataStatus,Boolean.TRUE);
        if(StringUtil.notEmpty(qo.getShortName())){
            where.lambda().and(map -> {
                return map.like(Business::getShortName,qo.getShortName())
                        .or().like(Business::getFullName,qo.getShortName());
            });
        }
        if(StringUtil.notEmpty(qo.getTelephone())){
            where.lambda().like(Business::getTelephone,qo.getTelephone());
        }
        if(StringUtil.notEmpty(qo.getStartTime())){
            where.lambda().ge(Business::getCreateTime,qo.getStartTime());
        }
        if(StringUtil.notEmpty(qo.getEndTime())){
            where.lambda().le(Business::getCreateTime,qo.getEndTime());
        }
        where.lambda().orderByDesc(Business::getCreateTime);
        IPage<Business> page = this.businessPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        resPage.setRecords(CopyUtil.copyList(page.getRecords(),PageBusinessVo.class));
        return resPage;
    }

    /**
     * 获取并校验商家信息
     * @param id
     * @return
     */
    @Override
    public PageBusinessVo getSjInfo(String id){
        Business business = this.businessPlusDao.getById(id);
        if(Objects.isNull(business) || !business.getDataStatus()){
            throw new ApiException("当前商家已被禁用，请联系管理员");
        }
        LocalDate now =  LocalDate.now();
        if(now.isBefore(business.getStartDate())){
            throw new ApiException("商家还未到启用时间，请联系管理员");
        }
        if(now.isAfter(business.getEndDate())){
            throw new ApiException("商家使用时间已过期，请联系管理员");
        }
        return CopyUtil.copy(business,PageBusinessVo.class);
    }

    /**
     * 选择商家列表
     * @param roleId
     * @param sjId
     * @return
     */
    @Override
    public List<SelectBusinessListVo> getSjListByRole(String roleId, String sjId){
        List<SelectBusinessListVo> resList = new ArrayList<>();
        //如果是平台人员看所有
        if(StringUtil.isPlatformAdmin(roleId)){
            List<Business> list = this.businessPlusDao.lambdaQuery()
                    .eq(Business::getDataStatus, Boolean.TRUE)
                    .orderByDesc(Business::getCreateTime).list();
            if(CollectionUtils.isEmpty(list)){
                return resList;
            }
            for(Business sj : list){
                SelectBusinessListVo vo = new SelectBusinessListVo();
                vo.setBusinessId(sj.getId());
                vo.setBusinessName(sj.getShortName());
                resList.add(vo);
            }
            return resList;
        }
        //商家返回自己
        PageBusinessVo info = this.getSjInfo(sjId);
        SelectBusinessListVo vo = new SelectBusinessListVo();
        vo.setBusinessId(info.getId());
        vo.setBusinessName(info.getShortName());
        resList.add(vo);
        return resList;
    }

    /**
     * 根据id查询
     * @param sjId
     * @return
     */
    public Business getBusinessById(String sjId){
        return this.businessPlusDao.getById(sjId);
    }




}
