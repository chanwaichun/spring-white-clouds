package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddOrUpdateFlyerInfoBo;
import com.byx.pub.bean.bo.ShareFlyerBo;
import com.byx.pub.bean.bo.ShareFlyerClickBo;
import com.byx.pub.bean.qo.PageFlyerInfoListQo;
import com.byx.pub.bean.qo.PageFlyerPullListQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.PromotionTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.DateUtil;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Jump
 * @date 2023/10/7 18:17:44
 */
@Slf4j
@Service
public class FlyerInfoServiceImpl implements FlyerInfoService {
    @Resource
    AdminService adminService;
    @Resource
    FrontUserService userService;
    @Resource
    FlyerInfoPlusDao flyerInfoPlusDao;
    @Resource
    FlyerSharePlusDao sharePlusDao;
    @Resource
    FlyerPullRecordPlusDao pullRecordPlusDao;
    @Resource
    InteractiveClickService clickService;
    @Resource
    FlyerRelayPlusDao relayPlusDao;
    @Resource
    FlyerRelayRecordPlusDao relayRecordPlusDao;
    @Resource
    BusinessService businessService;

    /**
     * 新增或修改电子传单
     * @param flyerInfoBo
     */
    public void addOrUpdateFlyer(AddOrUpdateFlyerInfoBo flyerInfoBo){
        //新增
        if(StringUtil.isEmpty(flyerInfoBo.getId())){
            this.flyerInfoPlusDao.save(CopyUtil.copy(flyerInfoBo, FlyerInfo.class));
            return;
        }
        //修改
        this.flyerInfoPlusDao.updateById(CopyUtil.copy(flyerInfoBo, FlyerInfo.class));
    }

    /**
     * 分页条件查询传单
     * @param qo
     * @return
     */
    public Page<PageFlyerInfoListVo> pageList(PageFlyerInfoListQo qo){
        Page<PageFlyerInfoListVo> resPage = new Page<>();
        QueryWrapper<FlyerInfo> where = new QueryWrapper<>();
        where.lambda().eq(FlyerInfo::getDataStatus,Boolean.TRUE);
        if(StringUtil.notEmpty(qo.getBusinessId())){
            where.lambda().eq(FlyerInfo::getBusinessId,qo.getBusinessId());
        }
        if(StringUtil.notEmpty(qo.getFlyerName())){
            where.lambda().like(FlyerInfo::getFlyerName,qo.getFlyerName());
        }
        if(StringUtil.notEmpty(qo.getBusinessName())){
            where.lambda().like(FlyerInfo::getBusinessName,qo.getBusinessName());
        }
        where.lambda().orderByDesc(FlyerInfo::getCreateTime);
        IPage<FlyerInfo> page = this.flyerInfoPlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        return resPage.setRecords(CopyUtil.copyList(page.getRecords(),PageFlyerInfoListVo.class));
    }

    /**
     * 查询传单详情
     * @param id
     * @return
     */
    public FlyerInfoVo getFlyerInfoDetail(String id){
        FlyerInfo info = this.flyerInfoPlusDao.getById(id);
        if(Objects.isNull(info) || !info.getDataStatus()){
            throw new ApiException("未找到传单数据");
        }
        return CopyUtil.copy(info,FlyerInfoVo.class);
    }

    /**
     * 删除传单信息
     * @param id
     */
    public void delFlyer(String id){
        FlyerInfo info = this.flyerInfoPlusDao.getById(id);
        if(Objects.isNull(info) || !info.getDataStatus()){
            throw new ApiException("未找到传单数据");
        }
        this.flyerInfoPlusDao.updateById(new FlyerInfo().setId(id).setDataStatus(Boolean.FALSE));
    }

    /**
     * 分享传单
     * 1.当前人分享所属商家的传单 有则返回 无则新增
     * 2.用户分享则返回空对象 (这样就不会影响原来的分享)
     * 3.同一个商家下A助教分享给B助教，B助教点进去再分享给a用户，这时分享对象已替换成B的。所以业绩算B的
     * 4.不同商家下A、B助教如3，则算A的，返回A的链接
     * 5.助教分享给用户A，用户A分享给用户B，绩效算助教的，但记录A\B的关系
     *
     * 转发传单(新逻辑)用户只保留转发记录 不需要拉新数据
     * 1.分享人 是传单的源头 商家咨询师
     * 2.供应商 是转发传单 其他商家咨询师
     * 3.转发人 可能是商家咨询师  可能是 用户
     * @param bo
     * @return
     */
    public FlyerShareVo shareFlyer(ShareFlyerBo bo){
        FlyerShareVo resVo = new FlyerShareVo();
        User user = this.userService.getUserById(bo.getNowUid());
        if(Objects.isNull(user)){
            throw new ApiException("未找到用户数据");
        }
        FlyerInfo flyer = this.flyerInfoPlusDao.getById(bo.getFlyerId());
        if(Objects.isNull(flyer) || !flyer.getDataStatus()){
            throw new ApiException("未找到传单数据");
        }
        //如果当前人是 传单商家的咨询师(分享)
        if(user.getRoleId() != 0 && user.getBusinessId().equals(flyer.getBusinessId())){
            //如果当前人是 咨询师
            Admin admin = adminService.getAdminById(user.getAdminId());
            if(Objects.isNull(admin)){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到当前人咨询师身份信息");
            }
            //查询当前人是否分享过
            FlyerShare flyerShare = this.sharePlusDao.lambdaQuery()
                    .eq(FlyerShare::getFlyerId, bo.getFlyerId()).eq(FlyerShare::getUserId, bo.getNowUid()).last("limit 1").one();
            //如果没分享过 则新增
            if(Objects.isNull(flyerShare)){
                FlyerShare addShare = new FlyerShare();
                addShare.setFlyerId(bo.getFlyerId()).setFlyerName(flyer.getFlyerName()).setUserId(user.getId())
                        .setAdminId(admin.getAdminId()).setTrueName(admin.getTrueName())
                        .setRoleId(user.getRoleId()).setImg(user.getUserImg()).setShareType(bo.getShareType());
                this.sharePlusDao.save(addShare);
                flyerShare = this.sharePlusDao.getById(addShare.getId());
            }
            //替换数据 返回
            resVo.setShareId(flyerShare.getId()).setFlyerId(bo.getFlyerId()).setRelayId("");
            return resVo;
        }
        //如果当前人是用户 或 非传单商家(转发)
        if(StringUtil.isEmpty(bo.getShareId())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户转发需要传入分享主键id");
        }
        FlyerShare flyerShare = this.sharePlusDao.getById(bo.getShareId());
        if(Objects.isNull(flyerShare) || !flyerShare.getDataStatus()){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到传单原分享信息");
        }
        //设置返回数据
        resVo.setShareId(flyerShare.getId()).setFlyerId(bo.getFlyerId());
        //如果是用户 替换转发人
        if(0 == user.getRoleId()){
            //如果转发id是空表示用户转分享人的
            if(StringUtil.isEmpty(bo.getRelayId())){
                FlyerRelay relay = getRelayInfo(bo.getFlyerId(),flyerShare.getUserId(),flyerShare.getUserId(),bo.getNowUid());
                //如果存在
                if(Objects.nonNull(relay)){
                    resVo.setRelayId(relay.getId());
                    return resVo;
                }
                //如果不存在 新增
                FlyerRelay addRelay = new FlyerRelay();
                addRelay.setFlyerBid(flyer.getBusinessId()).setFlyerId(flyer.getId()).setFlyerName(flyer.getFlyerName());//传单信息
                addRelay.setShareRid(flyerShare.getRoleId()).setShareUid(flyerShare.getUserId()).setShareName(flyerShare.getTrueName());//分享人信息
                addRelay.setSupplierBid(flyer.getBusinessId()).setSupplierRid(flyerShare.getRoleId())
                        .setSupplierUid(flyerShare.getUserId()).setSupplierName(flyerShare.getTrueName());//供应商信息
                addRelay.setRelayBid("").setRelayRid(user.getRoleId())
                        .setRelayUid(user.getId()).setRelayName(user.getNickName());//转发人信息
                this.relayPlusDao.save(addRelay);
                resVo.setRelayId(addRelay.getId());
                return resVo;
            }
            //如果转发id不为空表示转的其他转发人的(用户、供应商)
            FlyerRelay relay = this.relayPlusDao.getById(bo.getRelayId());
            if(Objects.isNull(relay)){
                throw new ApiException("未找到转发数据信息");
            }
            FlyerRelay relayInfo = getRelayInfo(bo.getFlyerId(),flyerShare.getUserId(),relay.getSupplierUid(),bo.getNowUid());
            if(Objects.isNull(relayInfo)){
                FlyerRelay addRelay = new FlyerRelay();
                addRelay.setFlyerBid(flyer.getBusinessId()).setFlyerId(flyer.getId()).setFlyerName(flyer.getFlyerName());//传单信息
                addRelay.setShareRid(flyerShare.getRoleId()).setShareUid(flyerShare.getUserId()).setShareName(flyerShare.getTrueName());//分享人信息
                addRelay.setSupplierBid(relay.getSupplierBid()).setSupplierRid(relay.getSupplierRid())
                        .setSupplierUid(relay.getSupplierUid()).setSupplierName(relay.getSupplierName());//供应商信息
                addRelay.setRelayBid("").setRelayRid(user.getRoleId())
                        .setRelayUid(user.getId()).setRelayName(user.getNickName());//转发人信息
                this.relayPlusDao.save(addRelay);
                resVo.setRelayId(addRelay.getId());
                return resVo;
            }
            resVo.setRelayId(relayInfo.getId());
            return resVo;
        }
        //供应商
        Admin admin = adminService.getAdminById(user.getAdminId());
        if(Objects.isNull(admin)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到当前人咨询师身份信息");
        }
        FlyerRelay relayInfo = getRelayInfo(bo.getFlyerId(),flyerShare.getUserId(),bo.getNowUid(),bo.getNowUid());
        if(Objects.isNull(relayInfo)){
            FlyerRelay addRelay = new FlyerRelay();
            addRelay.setFlyerBid(flyer.getBusinessId()).setFlyerId(flyer.getId()).setFlyerName(flyer.getFlyerName());//传单信息
            addRelay.setShareRid(flyerShare.getRoleId()).setShareUid(flyerShare.getUserId()).setShareName(flyerShare.getTrueName());//分享人信息
            addRelay.setSupplierBid(admin.getBusinessId()).setSupplierRid(user.getRoleId())
                    .setSupplierUid(user.getId()).setSupplierName(admin.getTrueName());//供应商信息
            addRelay.setRelayBid(admin.getBusinessId()).setRelayRid(user.getRoleId())
                    .setRelayUid(user.getId()).setRelayName(admin.getTrueName());//转发人信息
            this.relayPlusDao.save(addRelay);
            resVo.setRelayId(addRelay.getId());
            return resVo;
        }
        resVo.setRelayId(relayInfo.getId());
        return resVo;
    }

    /**
     * 获取转发信息
     * @param flyerId
     * @param shareUid
     * @param supplierUid
     * @param relayUid
     * @return
     */
    public FlyerRelay getRelayInfo(String flyerId,String shareUid,String supplierUid,String relayUid){
        return this.relayPlusDao.lambdaQuery().eq(FlyerRelay::getDataStatus, true)
                .eq(FlyerRelay::getFlyerId, flyerId)
                .eq(FlyerRelay::getShareUid, shareUid)
                .eq(FlyerRelay::getSupplierUid,supplierUid)
                .eq(FlyerRelay::getRelayUid, relayUid)
                .last("limit 1").one();
    }

    /**
     * 保存拉新记录
     * @param bo
     */
    public void addClickRecord(ShareFlyerClickBo bo){
        //分享信息
        FlyerShare shareInfo = this.sharePlusDao.getById(bo.getShareId());
        if(Objects.isNull(shareInfo)){
            return;
        }
        //查询传单信息
        FlyerInfo flyerInfo = this.flyerInfoPlusDao.getById(shareInfo.getFlyerId());
        if(Objects.isNull(flyerInfo)){
            return;
        }
        //获取被邀请人信息
        User user = this.userService.getUserById(bo.getInviteeUid());
        if(Objects.isNull(user)){
            return;
        }
        //保存点击记录
        clickService.saveLog(shareInfo.getFlyerId(), PromotionTypeEnum.DZ_FLYER.getValue(),shareInfo.getUserId(),bo.getInviteeUid(),flyerInfo.getBusinessId());
        //保存分享邀请记录
        saveShareRecord(bo,flyerInfo,shareInfo,user);
        //判断是否有 转发id
        if(StringUtil.notEmpty(bo.getRelayId())){
            //获取转发信息
            FlyerRelay relay = this.relayPlusDao.getById(bo.getRelayId());
            if(Objects.isNull(relay)){
                return;
            }
            //如果转发人是普通用户 不保存
            if(relay.getRelayRid() == 0){
                return;
            }
            //保存转发邀请记录
            saveRelayRecord(relay,flyerInfo,user);
        }
    }

    /**
     * 保存转发邀请记录da
     * @param relay
     * @param flyerInfo
     * @param user
     */
    public void saveRelayRecord(FlyerRelay relay,FlyerInfo flyerInfo,User user){
        //过滤自己点自己的
        if(relay.getRelayUid().equals(user.getId())){
            return;
        }
        //校验
        Integer count = this.relayRecordPlusDao.lambdaQuery()
                .eq(FlyerRelayRecord::getRelayId, relay.getId())
                .eq(FlyerRelayRecord::getFlyerId, flyerInfo.getId())
                .eq(FlyerRelayRecord::getInviteeUid, user.getId()).count();
        if(count > 0){
            return;
        }
        //查询商家名称
        Business business = this.businessService.getBusinessById(relay.getSupplierBid());
        if(Objects.isNull(business)){
            return;
        }
        //保存
        FlyerRelayRecord relayRecord = new FlyerRelayRecord();
        relayRecord.setRelayId(relay.getId()).setFlyerBid(flyerInfo.getBusinessId());
        relayRecord.setFlyerId(flyerInfo.getId()).setFlyerName(flyerInfo.getFlyerName());
        relayRecord.setRelaySjName(business.getShortName());
        relayRecord.setRelayUid(relay.getRelayUid()).setRelayName(relay.getRelayName());
        relayRecord.setInviteeUid(user.getId()).setInviteeName(user.getNickName());
        this.relayRecordPlusDao.save(relayRecord);
    }

    /**
     * 保存分享邀请记录
     * @param bo
     * @param flyerInfo
     * @param shareInfo
     * @param user
     */
    public void saveShareRecord(ShareFlyerClickBo bo,FlyerInfo flyerInfo,FlyerShare shareInfo,User user){
        //校验记录是否存在 存在不记录
        Integer count = this.pullRecordPlusDao.lambdaQuery()
                .eq(FlyerPullRecord::getShareId, bo.getShareId())
                .eq(FlyerPullRecord::getInviteeUid, bo.getInviteeUid())
                .count();
        if(count > 0){
            return;
        }
        FlyerPullRecord record = new FlyerPullRecord();
        record.setShareId(shareInfo.getId());
        record.setFlyerId(flyerInfo.getId());
        record.setFlyerName(flyerInfo.getFlyerName());
        record.setShareUid(shareInfo.getUserId());
        record.setShareName(shareInfo.getTrueName());
        record.setInviteeUid(user.getId());
        record.setInviteeName(user.getNickName());
        record.setBusinessId(flyerInfo.getBusinessId());
        this.pullRecordPlusDao.save(record);
    }


    /**
     * 分页查询引流统计
     * @param qo
     * @return
     */
    public Page<PageFlyerPullListVo> pageFlyerPullList(PageFlyerPullListQo qo){
        Page<PageFlyerPullListVo> resPage = new Page<>();
        //先查询出这个传单下的分享列表
        QueryWrapper<FlyerShare> where = new QueryWrapper<>();
        where.lambda().eq(FlyerShare::getDataStatus,Boolean.TRUE);
        where.lambda().eq(FlyerShare::getFlyerId,qo.getFlyerId());
        IPage<FlyerShare> page = this.sharePlusDao.page(new Page<>(qo.getPageNum(), qo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(0 == page.getTotal()){
            return resPage;
        }
        //查询每一个分享人的引流数据
        List<FlyerShare> records = page.getRecords();
        List<PageFlyerPullListVo> resList = new ArrayList<>();
        for (FlyerShare share : records){
            PageFlyerPullListVo vo = new PageFlyerPullListVo();
            BeanUtils.copyProperties(share,vo);
            //统计拉新人数
            qo.setDealStatus(null);
            qo.setShareId(share.getId());
            vo.setPullNum(this.countPullNumByDate(qo));
            //统计成交人数
            qo.setDealStatus(Boolean.TRUE);
            vo.setDealNum(this.countPullNumByDate(qo));
            resList.add(vo);
        }
        resPage.setRecords(resList);
        return resPage;
    }


    /**
     * 获取某短时间内引流人数
     * @param qo
     * @return
     */
    public Integer countPullNumByDate(PageFlyerPullListQo qo){
        QueryWrapper<FlyerPullRecord> where = new QueryWrapper<>();
        where.lambda().eq(FlyerPullRecord::getShareId,qo.getShareId());
        //如果是只查成交人数
        if(Objects.nonNull(qo.getDealStatus()) && qo.getDealStatus()){
            where.lambda().eq(FlyerPullRecord::getDealStatus,qo.getDealStatus());
        }
        if(Objects.nonNull(qo.getStartDate()) && Objects.nonNull(qo.getEndDate())){
            where.lambda().ge(FlyerPullRecord::getCreateTime,qo.getStartDate().atStartOfDay());
            where.lambda().le(FlyerPullRecord::getCreateTime, LocalDateTime.of(qo.getEndDate(), LocalTime.MAX));
        }
        return this.pullRecordPlusDao.count(where);
    }

    /**
     * 校验并成交引流记录
     * 1.下单商家与引流商家同一个
     * 2.分享id不能是空(为空表示不是商家的员工)
     * 3.拿最早引流记录(区别多助教引流同一个人)
     * @param orders
     * @param detail
     */
    @Async
    public void checkAndDeal(Orders orders,OrderDetail detail){
        //查询用户是否有被传单引流，引流人是谁
        FlyerPullRecord record = this.pullRecordPlusDao.lambdaQuery()
                .eq(FlyerPullRecord::getBusinessId, orders.getBusinessId())
                .ne(FlyerPullRecord::getShareId,"")
                .eq(FlyerPullRecord::getInviteeUid, orders.getUserId())
                .orderByAsc(FlyerPullRecord::getCreateTime)
                .last("limit 1").one();
        if(Objects.nonNull(record)){
            this.pullRecordPlusDao.updateById(new FlyerPullRecord().setId(record.getId()).setDealStatus(Boolean.TRUE));
        }
        //供应商引流成交 拿最早引流记录
        FlyerRelayRecord relayRecord = this.relayRecordPlusDao.lambdaQuery()
                .eq(FlyerRelayRecord::getDealStatus,Boolean.FALSE)
                .eq(FlyerRelayRecord::getFlyerBid, orders.getBusinessId()).eq(FlyerRelayRecord::getInviteeUid, orders.getUserId())
                .orderByAsc(FlyerRelayRecord::getCreateTime).last("limit 1").one();
        if(Objects.nonNull(relayRecord)){
            this.relayRecordPlusDao.updateById(
                    new FlyerRelayRecord()
                    .setId(relayRecord.getId())
                    .setDealStatus(Boolean.TRUE)
                    .setOrderSn(orders.getOrderSn())
                    .setOrderProduct(detail.getProductName())
                    .setInviteePhone(orders.getUserMobile())
                    .setDealTime(DateUtil.dateTimeToTimeString(new Date()))
            );
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public FlyerInfo getFlyerById(String id){
        return this.flyerInfoPlusDao.getById(id);
    }

    /**
     * 获取转发成交明细数据
     * @return
     */
    public List<FlyerDealDetailVo> getRelayDealDetailList(){
        List<FlyerRelayRecord> list = this.relayRecordPlusDao.lambdaQuery()
                .eq(FlyerRelayRecord::getDealStatus, true)
                .orderByAsc(FlyerRelayRecord::getCreateTime).list();
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return CopyUtil.copyList(list,FlyerDealDetailVo.class);
    }


    /**
     * 统计转发数据
     * @return
     */
    public List<SupplierRelayVo> listRelay(){
        List<SupplierRelayVo> resList = new ArrayList<>();
        //1.查询转发list
        List<FlyerRelay> list = this.relayPlusDao.lambdaQuery().eq(FlyerRelay::getDataStatus, true)
                .orderByAsc(FlyerRelay::getCreateTime).list();
        if(CollectionUtils.isEmpty(list)){
            return resList;
        }
        for(FlyerRelay relay : list){
            Business business = this.businessService.getBusinessById(relay.getRelayBid());
            if(Objects.isNull(business)){
                continue;
            }
            SupplierRelayVo vo = new SupplierRelayVo();
            vo.setFlyerName(relay.getFlyerName());
            vo.setFlyerSjName(business.getShortName());
            vo.setSupplierName(relay.getRelayName());
            vo.setPullNum(getRelayDetail(relay.getId(),null));
            vo.setDealNum(getRelayDetail(relay.getId(),true));
            resList.add(vo);
        }
        return resList;
    }


    public String getRelayDetail(String relayId,Boolean dealStatus){
        LambdaQueryWrapper<FlyerRelayRecord> where = new LambdaQueryWrapper<>();
        where.eq(FlyerRelayRecord::getRelayId,relayId);
        if(Objects.nonNull(dealStatus)){
            where.eq(FlyerRelayRecord::getDealStatus,dealStatus);
        }
        int count = this.relayRecordPlusDao.count(where);
        return count+"";
    }




}
