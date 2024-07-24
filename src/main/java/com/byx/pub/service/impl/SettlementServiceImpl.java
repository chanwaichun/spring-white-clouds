package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.ImportChangeDetailExcelBo;
import com.byx.pub.bean.qo.PageMainSettlementQo;
import com.byx.pub.bean.qo.PagePersonSettlementQo;
import com.byx.pub.bean.qo.PageSettlementDetailQo;
import com.byx.pub.bean.vo.PageMainSettlementVo;
import com.byx.pub.bean.vo.PagePersonSettlementVo;
import com.byx.pub.bean.vo.PageSettlementDetailListVo;
import com.byx.pub.bean.vo.ProductDetailVo;
import com.byx.pub.enums.OrderStatusEnum;
import com.byx.pub.enums.RuleTypeEnum;
import com.byx.pub.enums.SettlementTypeEnum;
import com.byx.pub.exception.ApiException;
import com.byx.pub.filter.RedisLock;
import com.byx.pub.mapper.OrdersSqlMapper;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 结算服务
 * 1.3版本更改为 基于 商品做结算
 * @Author Jump
 * @Date 2023/6/16 22:00
 */
@Slf4j
@Service
public class SettlementServiceImpl implements SettlementService {
    @Resource
    AdminService adminService;
    @Resource
    ProductService productService;
    @Resource
    FrontOrderService orderService;
    @Resource
    OrdersPlusDao ordersPlusDao;
    @Resource
    SettlementRulesPlusDao rulesPlusDao;
    @Resource
    SettlementRulesRangePlusDao rangePlusDao;
    @Resource
    SettlementMainPlusDao mainPlusDao;
    @Resource
    SettlementDetailPlusDao detailPlusDao;
    @Resource
    BusinessService businessService;
    @Resource
    FrontUserService userService;
    @Resource
    OrdersSqlMapper ordersSqlMapper;


    /**
     * 结算账单
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmSettlementBill(String id){
        SettlementMain main = this.mainPlusDao.getById(id);
        if(Objects.isNull(main)){
            throw new ApiException("未找到数据");
        }
        if(main.getSettlementStatus()){
            throw new ApiException("该账单已结算");
        }
        this.mainPlusDao.updateById(new SettlementMain()
                .setId(id)
                .setSettlementStatus(Boolean.TRUE)
                .setBillDate(DateUtil.dateTimeToString(main.getCreateTime())+DateUtil.newDateToString())
        );
        this.detailPlusDao.lambdaUpdate()
                .set(SettlementDetail::getSettlementStatus,Boolean.TRUE)
                .eq(SettlementDetail::getMainId,id)
                .update();
    }


    /**
     * 分页条件查询结算单
     * @param qo
     * @return
     */
    public Page<PageMainSettlementVo> pageMainSettlementList(PageMainSettlementQo qo){
        Page<PageMainSettlementVo> resVo = new Page<>();
        QueryWrapper<SettlementMain> where = new QueryWrapper<>();
        if(StringUtil.notEmpty(qo.getProductName())){
            where.lambda().like(SettlementMain::getProductName,qo.getProductName());
        }
        if(StringUtil.notEmpty(qo.getBusinessName())){
            where.lambda().like(SettlementMain::getBusinessName,qo.getBusinessName());
        }
        if(StringUtil.notEmpty(qo.getStartTime()) && StringUtil.notEmpty(qo.getEndTime())){
            where.lambda().ge(SettlementMain::getCreateTime,qo.getStartTime());
            where.lambda().le(SettlementMain::getCreateTime,qo.getEndTime());
        }
        if(Objects.nonNull(qo.getSettlementStatus())){
            where.lambda().eq(SettlementMain::getSettlementStatus,qo.getSettlementStatus());
        }
        where.lambda().orderByAsc(SettlementMain::getSettlementStatus);
        IPage<SettlementMain> page = this.mainPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resVo);
        if(0 == page.getTotal()){
            return resVo;
        }
        List<PageMainSettlementVo> resList = new ArrayList<>();
        for(SettlementMain main : page.getRecords()){
            PageMainSettlementVo vo = new PageMainSettlementVo();
            BeanUtils.copyProperties(main,vo);
            vo.setBalanceAmount(vo.getIncomeAmount().subtract(vo.getSettlementAmount()));
            resList.add(vo);
        }
        resVo.setRecords(resList);
        return resVo;
    }


    /**
     * 分页查询个人在服务的分成
     * @param qo
     * @return
     */
    public Page<PagePersonSettlementVo> pagePersonSettlementList(PagePersonSettlementQo qo){
        return this.ordersSqlMapper.pagePersonSettlementList(new Page(qo.getPageNum(),qo.getPageSize()),qo);
    }

    /**
     * 分页条件查询结算明细
     * @param qo
     * @return
     */
    public Page<PageSettlementDetailListVo> pageDetailList(PageSettlementDetailQo qo){
        Page<PageSettlementDetailListVo> resPage = new Page<>();
        QueryWrapper<SettlementDetail> where = new QueryWrapper<>();
        if(StringUtil.notEmpty(qo.getMainId())){
            where.lambda().eq(SettlementDetail::getMainId,qo.getMainId());
        }
        if(StringUtil.notEmpty(qo.getUserId())){
            where.lambda().eq(SettlementDetail::getSettlerUserId,qo.getUserId());
        }
        if(StringUtil.notEmpty(qo.getAdminId())){
            where.lambda().eq(SettlementDetail::getSettlerAdminId,qo.getAdminId());
        }
        if(StringUtil.notEmpty(qo.getOrderSn())){
            where.lambda().eq(SettlementDetail::getOrderSn,qo.getOrderSn());
        }
        if(StringUtil.notEmpty(qo.getBusinessName())){
            where.lambda().like(SettlementDetail::getBusinessName,qo.getBusinessName());
        }
        if(StringUtil.notEmpty(qo.getNickName())){
            where.lambda().like(SettlementDetail::getNickName,qo.getNickName());
        }
        IPage<SettlementDetail> page = this.detailPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        List<PageSettlementDetailListVo> resList = new ArrayList<>();
        for(SettlementDetail detail : page.getRecords()){
            PageSettlementDetailListVo resVo = new PageSettlementDetailListVo();
            BeanUtils.copyProperties(detail,resVo);
            resVo.setCreateTime(DateUtil.dateTimeToString(detail.getCreateTime()));
            resList.add(resVo);
        }
        resPage.setRecords(resList);
        return resPage;
    }

    /**
     * 导入变更修改结算金额
     * @param list
     */
    public Integer importChangeDetailAmount(List<ImportChangeDetailExcelBo> list){
        Integer resNum = 0;
        if(CollectionUtils.isEmpty(list)){
            return resNum;
        }
        for(ImportChangeDetailExcelBo bo : list){
            if(StringUtil.isEmpty(bo.getId()) || Objects.isNull(bo.getSettlementAmount())
                    || BigDecimal.ZERO.compareTo(bo.getSettlementAmount()) > 0){
                continue;
            }
            SettlementDetail detail = this.detailPlusDao.getById(bo.getId());
            //未找到明细、明细已结算、金额没变动 跳过
            if(Objects.isNull(detail) || detail.getSettlementStatus()
                    ||detail.getSettlementAmount().compareTo(bo.getSettlementAmount()) == 0){
                continue;
            }
            //如果调整是增加 则校验 订单设置结算金额超过 跳过
            if(detail.getSettlementAmount().compareTo(bo.getSettlementAmount()) < 0){
                BigDecimal sumOrderAmount = this.ordersSqlMapper.sumSettlementAmountByOrderSn(detail.getMainId(), detail.getOrderSn());
                //新合计金额 = 原合计金额 - 原明细金额 + 调整金额
                sumOrderAmount = sumOrderAmount.subtract(detail.getSettlementAmount()).add(bo.getSettlementAmount());
                if(sumOrderAmount.compareTo(detail.getOrderAmount()) > 0){
                    continue;
                }
            }
            //如果未找到主单也跳过
            SettlementMain main = this.mainPlusDao.getById(detail.getMainId());
            if(Objects.isNull(main)){
                continue;
            }
            //变更明细
            this.detailPlusDao.updateById(new SettlementDetail().setId(bo.getId()).setSettlementAmount(bo.getSettlementAmount()));
            //变更总单 新总单结算金额 = 原总单结算金额 - 原明细金额 + 调整金额
            BigDecimal newMainAmount = main.getSettlementAmount().subtract(detail.getSettlementAmount()).add(bo.getSettlementAmount());
            this.mainPlusDao.updateById(new SettlementMain().setId(main.getId()).setSettlementAmount(newMainAmount));
            resNum = resNum + 1;
        }
        return resNum;
    }


    /**
     * 变更明细金额
     * @param id
     * @param amount
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#{id}",prefix ="byx:scheduled:settlement:change:amount",isWaitForLock = false)
    public void changeDetailAmount(String id,BigDecimal amount){
        SettlementDetail detail = this.detailPlusDao.getById(id);
        if(Objects.isNull(detail)){
            throw new ApiException("未找到明细信息");
        }
        if(detail.getSettlementStatus()){
            throw new ApiException("已结算明细不能修改");
        }
        //如果没有变动 则不改变
        if(detail.getSettlementAmount().compareTo(amount) == 0){
            return;
        }
        //如果调整变多 则需要校验 新的合计就算金额是否超过订单实收金额
        if(detail.getSettlementAmount().compareTo(amount) < 0){
            BigDecimal sumOrderAmount = this.ordersSqlMapper.sumSettlementAmountByOrderSn(detail.getMainId(), detail.getOrderSn());
            //新合计金额 = 原合计金额 - 原明细金额 + 调整金额
            sumOrderAmount = sumOrderAmount.subtract(detail.getSettlementAmount()).add(amount);
            if(sumOrderAmount.compareTo(detail.getOrderAmount()) > 0){
                throw new ApiException("订单结算分成总额已超过订单实收金额");
            }
        }
        //变更明细
        this.detailPlusDao.updateById(new SettlementDetail().setId(id).setSettlementAmount(amount));
        //变更总单
        SettlementMain main = this.mainPlusDao.getById(detail.getMainId());
        if(Objects.isNull(main)){
            throw new ApiException("未找到主订单");
        }
        // 新总单结算金额 = 原总单结算金额 - 元明细金额 + 调整金额
        BigDecimal newMainAmount = main.getSettlementAmount().subtract(detail.getSettlementAmount()).add(amount);
        this.mainPlusDao.updateById(new SettlementMain().setId(main.getId()).setSettlementAmount(newMainAmount));
    }













/********************************************************* 以下定时任务结算 *************************************************************/



    /**
     * 每天晚上凌晨结算符合条件的订单
     */
    @RedisLock(constantKey = "settlementOrder",prefix ="byx:scheduled:settlement:order",isWaitForLock = false)
    public void settlementOrder(){
        log.info("--------进入到结算订单定时任务-------");
        //1.查询所有待结算订单
        List<Orders> list = ordersPlusDao.lambdaQuery().gt(Orders::getPaidAmount,BigDecimal.ZERO)
                .eq(Orders::getXhsOrderId,"")
                .eq(Orders::getStatus, OrderStatusEnum.ORDER_SUCCESS.getCode()).list();
        if(CollectionUtils.isEmpty(list)){
            log.info("--------没有待结算订单而结束-------");
            return;
        }
        for(Orders order : list){
            settlement(order);
        }
        log.info("--------结算订单定时任务处理完成，共:{}条订单-------",list.size());
    }

    /**
     * 订单抢锁执行定时任务
     * @param order
     */
    public void settlement(Orders order){
        //2.循环结算订单
        log.info("-----进入到抢锁执行结算-------订单id{}",order.getId());
        //获取订单详情
        OrderDetail detail = this.orderService.getOrderDetailByOrderSn(order.getOrderSn());
        if(Objects.isNull(detail)){
            log.error("-----订单结算没有找到订单详情数据，订单号:{}",order.getOrderSn());
            return;
        }
        //获取商品信息，查看绑定规则id
        ProductDetailVo product = this.productService.getProductById(detail.getProductId());
        if(Objects.isNull(product) || StringUtil.isEmpty(product.getSettleRules())){
            return;
        }
        //如果规则不存在 跳过
        SettlementRules rule = this.rulesPlusDao.getById(product.getSettleRules());
        if(Objects.isNull(rule)){
            log.error("-----订单结算没有找到结算规则数据，结算规则id:{}",product.getSettleRules());
            return;
        }
        LocalDate orderDoneDate = DateUtil.dateToLocalDate(order.getPaymentTime());
        //校验规则 订单完成时间 早于 规则生效时间则跳过
        if(orderDoneDate.isBefore(rule.getEffectiveDate())){
            log.error("-----订单完成时间早于规则生效时间 不参与结算，订单完成日期:{},规则生效日期:{}"
                    ,orderDoneDate.toString(),rule.getEffectiveDate().toString());
            return;
        }
        //如果订单不符合 T+N 则跳过 如订单 完成时间 10-01，规则 T+3，则今天如果是大于等于10-04 则结算
        if(orderDoneDate.plusDays(rule.getSettlementCycle()).isBefore(LocalDate.now())){
            log.error("-----订单不符合结算规则T+N 不参与结算，订单完成日期:{},规则T+:{}"
                    ,orderDoneDate.toString(),rule.getSettlementCycle().toString());
            return;
        }
        //如果订单没有 拉新人、促成人 则百分百收入归商家
        if(StringUtil.isEmpty(order.getPullUid()) && StringUtil.isEmpty(order.getFacilitateUid())){
            createSjIncome(order,detail);
            return;
        }
        //处理提成
        SettlementRulesRange pullRang = null,facilitateRang = null;
        //处理拉新人的提成
        if(StringUtil.notEmpty(order.getPullUid())){
            User pullUser = this.userService.getUserById(order.getPullUid());
            if(Objects.nonNull(pullUser)){
                //查询合适的范围
                pullRang = this.getRuleRang(rule.getId(),SettlementTypeEnum.PULL.getValue(), pullUser);
                //如果找到对应的规则范围 则增加明细
                if(Objects.nonNull(pullRang)){
                    createPersonIncome(order,detail,pullRang,pullUser);
                }
            }
        }
        //处理促成人
        if(StringUtil.notEmpty(order.getFacilitateUid())){
            User facilitateUser = this.userService.getUserById(order.getFacilitateUid());
            if(Objects.nonNull(facilitateUser)){
                //查询合适的范围
                facilitateRang = this.getRuleRang(rule.getId(), SettlementTypeEnum.FACILITATE.getValue(), facilitateUser);
                //如果找到对应的规则范围 则增加明细
                if(Objects.nonNull(facilitateRang)){
                    createPersonIncome(order,detail,facilitateRang,facilitateUser);
                }
            }
        }
        //如果没有合适的 拉新人范围 且 没有合适的 促成人范围 还是算商家的
        if(Objects.isNull(pullRang) && Objects.isNull(facilitateRang)){
            createSjIncome(order,detail);
            return;
        }

    }



    /**
     * 查询合适的规则范围
     * @param ruleId
     * @param type
     * @param user
     * @return
     */
    public SettlementRulesRange getRuleRang(String ruleId,Integer type,User user){
        //1.优先查个人
        SettlementRulesRange personRang = this.rangePlusDao.lambdaQuery()
                .eq(SettlementRulesRange::getRuleId, ruleId)
                .eq(SettlementRulesRange::getDataStatus,Boolean.TRUE)
                .eq(SettlementRulesRange::getRuleType, RuleTypeEnum.PERSON_TARGET.getValue())
                .eq(SettlementRulesRange::getTargetId, user.getId())
                .eq(SettlementRulesRange::getSettlementType, type).last("limit 1").one();
        if(Objects.nonNull(personRang)){
            return personRang;
        }
        //2.再查角色
        return this.rangePlusDao.lambdaQuery()
                .eq(SettlementRulesRange::getDataStatus,Boolean.TRUE)
                .eq(SettlementRulesRange::getRuleId, ruleId)
                .eq(SettlementRulesRange::getRuleType, RuleTypeEnum.ROLE_TARGET.getValue())
                .eq(SettlementRulesRange::getTargetId, user.getRoleId())
                .eq(SettlementRulesRange::getSettlementType, type).last("limit 1").one();
    }


    /**
     * 保存个人结算记录
     * @param order
     * @param orderDetail
     * @param ruleRang
     * @param user
     */
    public void createPersonIncome(Orders order,OrderDetail orderDetail,SettlementRulesRange ruleRang,User user){
        //应收金额
        BigDecimal incomeAmount = order.getPaidAmount();
        //获取账期主单 并累加结算金额、人
        SettlementMain main = this.getOrCreateMain(order.getBusinessId(), orderDetail.getProductId(), orderDetail.getProductName());
        SettlementMain updateMain = new SettlementMain();
        updateMain.setId(main.getId());
        updateMain.setIncomeAmount(main.getIncomeAmount().add(incomeAmount));
        //结算金额 = 应收 * 分成比列(需要/100)
        BigDecimal shareRate = customDivision(ruleRang.getShareRate(), new BigDecimal(100), 4);
        BigDecimal settlerAmount = incomeAmount.multiply(shareRate).setScale(2,BigDecimal.ROUND_HALF_UP);
        updateMain.setSettlementAmount(main.getSettlementAmount().add(settlerAmount));
        updateMain.setOrderNum(main.getOrderNum()+1);

        //2.新增明细
        SettlementDetail detail = new SettlementDetail();
        detail.setBusinessId(main.getBusinessId()).setBusinessName(main.getBusinessName())
                .setOrderSn(order.getOrderSn()).setOrderAmount(incomeAmount)
                .setProductId(orderDetail.getProductId()).setProductName(orderDetail.getProductName())
                .setUserId(order.getUserId()).setNickName(order.getUserName())
                .setSettlerUserId(user.getId()).setSettlerUserName(user.getNickName())
                .setShareType(ruleRang.getSettlementType()).setShareRate(ruleRang.getShareRate())
                .setSettlementId(ruleRang.getRuleId()).setRangeId(ruleRang.getId())
                .setSettlementAmount(settlerAmount).setMainId(main.getId());
        //结算人+1
        SettlementDetail settlerByMainId = getSettlerByMainId(main.getId(), user.getId());
        if(Objects.isNull(settlerByMainId)){
            updateMain.setSettlerNum(main.getSettlerNum()+1);
        }
        Admin admin = this.adminService.getAdminById(user.getAdminId());
        if(Objects.nonNull(admin)){
            detail.setSettlerAdminId(user.getAdminId());
            detail.setSettlerAdminName(admin.getTrueName());
        }
        this.mainPlusDao.updateById(updateMain);
        this.detailPlusDao.save(detail);
        //3.结算订单
        this.orderService.settlementOrder(order.getId());
    }


    /**
     * 生成商家100%收益数据
     * @param order
     * @param detail
     */
    public void createSjIncome(Orders order,OrderDetail detail){
        //获取商家当前服务主账单
        SettlementMain main = getOrCreateMain(order.getBusinessId(), detail.getProductId(), detail.getProductName());
        //商家100%收益订单 不需要设置明细
        SettlementMain updateMain = new SettlementMain();
        updateMain.setId(main.getId())
                .setIncomeAmount(main.getIncomeAmount().add(order.getPaidAmount()));
        this.mainPlusDao.updateById(updateMain);
        //更改订单状态
        orderService.settlementOrder(order.getId());
    }

    /**
     * 根据结算人获取计算明细
     * @param mainId
     * @param settlerUid
     * @return
     */
    public SettlementDetail getSettlerByMainId(String mainId,String settlerUid){
       return this.detailPlusDao.lambdaQuery()
                .eq(SettlementDetail::getMainId,mainId)
                .eq(SettlementDetail::getSettlerUserId,settlerUid)
                .last("limit 1").one();
    }

    /**
     * 获取或创建结算主单(不设置金额、人数)
     * @param bid
     * @param pid
     * @param productName
     * @return
     */
    public SettlementMain getOrCreateMain(String bid,String pid,String productName){
        //1.判断商家+服务是否有待结算订单 若有 则累加 如无则创建
        SettlementMain settlementMain = this.mainPlusDao.lambdaQuery()
                .eq(SettlementMain::getSettlementStatus, Boolean.FALSE)
                .eq(SettlementMain::getBusinessId, bid)
                .eq(SettlementMain::getProductId, pid).last("limit 1").one();
        if(Objects.nonNull(settlementMain)){
            return settlementMain;
        }
        //新增
        SettlementMain main = new SettlementMain();
        main.setBusinessId(bid);
        Business business = this.businessService.getBusinessById(bid);
        if(Objects.nonNull(business)){
            main.setBusinessName(business.getShortName());
        }
        //设置服务信息
        main.setProductId(pid).setProductName(productName);
        main.setBillDate(LocalDate.now().toString());
        this.mainPlusDao.save(main);
        return this.mainPlusDao.getById(main.getId());
    }



    /**
     * 自定义除法
     * @param fz
     * @param fm
     * @return
     */
    public BigDecimal customDivision(BigDecimal fz, BigDecimal fm, Integer scale){
        if(BigDecimal.ZERO.compareTo(fz) == 0){
            return BigDecimal.ZERO;
        }
        if(BigDecimal.ZERO.compareTo(fm) == 0){
            return BigDecimal.valueOf(1);
        }
        return fz.divide(fm,scale,BigDecimal.ROUND_HALF_UP);
    }



}
