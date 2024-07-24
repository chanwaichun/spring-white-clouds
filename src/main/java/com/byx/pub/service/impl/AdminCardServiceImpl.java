package com.byx.pub.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.byx.pub.bean.bo.*;
import com.byx.pub.bean.qo.AddOrUpdateCardBaseQo;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.qo.PageTagListQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.enums.*;
import com.byx.pub.exception.ApiException;
import com.byx.pub.filter.RedisLock;
import com.byx.pub.plus.dao.*;
import com.byx.pub.plus.entity.*;
import com.byx.pub.service.*;
import com.byx.pub.util.CopyUtil;
import com.byx.pub.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Jump
 * @Date 2023/7/16 21:16
 */
@Service
public class AdminCardServiceImpl implements AdminCardService {
    @Resource
    AdminCardPlusDao cardPlusDao;
    @Resource
    CardTagPlusDao cardTagPlusDao;
    @Resource
    CardContactPlusDao contactPlusDao;
    @Resource
    CardProductPlusDao cardProductPlusDao;
    @Resource
    CardPaperPlusDao paperPlusDao;
    @Resource
    AdminService adminService;
    @Resource
    BusinessService businessService;
    @Resource
    CardPosterPlusDao posterPlusDao;
    @Resource
    ProductPlusDao productPlusDao;
    @Resource
    CardShareFilePlusDao cardShareFilePlusDao;
    @Resource
    CardShareTeacherPlusDao shareTeacherPlusDao;
    @Resource
    TagPlusDao tagPlusDao;
    @Resource
    FrontUserService userService;
    @Resource
    WeChatApiService weChatApiService;


    /************************************************ 个人名片总信息 ****************************************************/

    /**
     * 获取名片信息
     * @param cardId
     * @return
     */
    public AdminCardVo getCardInfo(String cardId){
        AdminCardVo resVo = new AdminCardVo();
        AdminCard card = this.cardPlusDao.getById(cardId);
        if(Objects.isNull(card)){
            throw new ApiException("未找到名片信息");
        }
        BeanUtils.copyProperties(card,resVo);
        PageBusinessVo sjInfo = businessService.getSjInfo(card.getBusinessId());
        resVo.setFullName(sjInfo.getFullName());
        //获取标签
        List<CardTag> cardTags = this.getCardTags(card.getId());
        if(!CollectionUtils.isEmpty(cardTags)){
            resVo.setTags(cardTags);
        }
        //获取联系方式
        List<CardContact> cardIcons = this.getCardContacts(card.getId());
        if(!CollectionUtils.isEmpty(cardIcons)){
            resVo.setContacts(cardIcons);
        }
        //获取证书列表
        List<CardPaper> paperList = getPaperList(card.getId(),null);
        if(!CollectionUtils.isEmpty(paperList)){
            resVo.setPapers(paperList);
        }
        return resVo;
    }

    /**
     * 校验商家名片数量
     * @param sjId
     */
    public void checkSjCardNum(String sjId){
        PageBusinessVo sjInfo = this.businessService.getSjInfo(sjId);
        Integer count = this.getCardListBySj(sjId).size();
        //2023-10-18 团队版数量与小组版一致
        //如果是 个人版 限制一个
        if(sjInfo.getSuitType().equals(SuitTypeEnum.PERSON.getValue()) && count > SystemFinalCode.ZERO_VALUE){
            throw new ApiException("名片数量已达到上限，请联系管理员");
        }
        //如果是 小组版 限制一个
        if(sjInfo.getSuitType().equals(SuitTypeEnum.GROUP.getValue()) && count > SystemFinalCode.ZERO_VALUE){
            throw new ApiException("名片数量已达到上限，请联系管理员");
        }
        //如果是 团队版 限制三个
        if(sjInfo.getSuitType().equals(SuitTypeEnum.TEAM.getValue()) && count > SystemFinalCode.ZERO_VALUE){
            throw new ApiException("名片数量已达到上限，请联系管理员");
        }
    }


    /**
     * 新增或修改名片基础信息
     * @param qo
     * @return
     */
    public String addOrUpdateCardBaseInfo(AddOrUpdateCardBaseQo qo){
        String cardId = qo.getId();
        PageBusinessVo sjInfo = this.businessService.getSjInfo(qo.getBusinessId());
        qo.setShortName(sjInfo.getShortName());
        //新增
        if(StringUtil.isEmpty(qo.getId())) {
            //校验商家名片数量
            checkSjCardNum(qo.getBusinessId());
            //校验咨询师信息
            if(StringUtil.isEmpty(qo.getAdminId())){
                throw new ApiException("请选择咨询师");
            }
            Admin adminById = this.adminService.getAdminById(qo.getAdminId());
            if(Objects.isNull(adminById) || !adminById.getDataStatus()){
                throw new ApiException("未找到咨询师信息");
            }
            //校验咨询师是否已经有名片了
            AdminCard cardByAdminId = getCardByAdminId(qo.getAdminId(), qo.getBusinessId());
            if(Objects.nonNull(cardByAdminId)){
                throw new ApiException("当前咨询师已有新建名片");
            }
            AdminCard addCard = new AdminCard();
            BeanUtils.copyProperties(qo, addCard);
            cardId = IdWorker.getIdStr();
            addCard.setId(cardId);
            addCard.setFullName(sjInfo.getFullName());
            if(StringUtil.isEmpty(qo.getPhone())){
                addCard.setPhone(adminById.getMobile());
            }
            this.cardPlusDao.save(addCard);
            //生成二维码
            createCardQrCode(cardId);
        }else{
            AdminCard card = this.cardPlusDao.getById(qo.getId());
            if(Objects.isNull(card) || !card.getDataStatus()){
                throw new ApiException("未找到名片信息");
            }
            //如果更改了名片所属咨询师 需要校验咨询师是否之前就有名片了
            if(!qo.getAdminId().equals(card.getAdminId())){
                AdminCard cardByAdminId = getCardByAdminId(qo.getAdminId(), qo.getBusinessId());
                if(Objects.nonNull(cardByAdminId)){
                    throw new ApiException("当前咨询师已有名片");
                }
            }
            AdminCard updateCard = new AdminCard();
            BeanUtils.copyProperties(qo, updateCard);
            this.cardPlusDao.updateById(updateCard);
        }
        return cardId;
    }

    /**
     * 根据id查询名片
     * @param carId
     * @return
     */
    public AdminCard getAdminCardById(String carId){
        return this.cardPlusDao.getById(carId);
    }

    /**
     * 根据adminId获取名片
     * @param adminId
     * @return
     */
    public AdminCard getCardByAdminId(String adminId,String sjId){
         return this.cardPlusDao.lambdaQuery()
                 .eq(AdminCard::getDataStatus, Boolean.TRUE)
                 .eq(AdminCard::getBusinessId,sjId)
                 .eq(AdminCard::getAdminId, adminId).last("limit 1").one();
    }


    /**
     * 修改名片
     * @param updateBean
     */
    public void updateCard(AdminCard updateBean){
        this.cardPlusDao.updateById(updateBean);
    }

    /**
     * 获取商家下的名片列表
     * @param sjId
     * @return
     */
    public List<AdminCard> getCardListBySj(String sjId){
        LambdaQueryWrapper<AdminCard> where = new LambdaQueryWrapper<>();
        where.eq(AdminCard::getDataStatus,Boolean.TRUE);
        if(StringUtil.notEmpty(sjId)){
            where.eq(AdminCard::getBusinessId,sjId);
        }
        where.orderByDesc(AdminCard::getCreateTime);
        return cardPlusDao.list(where);
    }

    /**
     * 分页条件查询名片列表
     * @param cardQo
     * @return
     */
    public Page<PageAdminCardVo> pageListCard(PageAdminCardQo cardQo){
        Page<PageAdminCardVo> resPage = new Page<>();
        QueryWrapper<AdminCard> where = new QueryWrapper<>();
        where.lambda().eq(AdminCard::getDataStatus,Boolean.TRUE);
        if(StringUtil.notEmpty(cardQo.getBusinessId())){
            where.lambda().eq(AdminCard::getBusinessId,cardQo.getBusinessId());
        }
        if(StringUtil.notEmpty(cardQo.getTrueName())){
            where.lambda().and(wrapper -> {
                return wrapper.or().like(AdminCard::getTrueName, cardQo.getTrueName())
                        .or().like(AdminCard::getNickName, cardQo.getTrueName());
            });
        }
        where.lambda().orderByDesc(AdminCard::getCreateTime);
        IPage<AdminCard> page = this.cardPlusDao.page(new Page<>(cardQo.getPageNum(), cardQo.getPageSize()), where);
        BeanUtils.copyProperties(page,resPage);
        if(SystemFinalCode.ZERO_VALUE == page.getTotal()){
            return resPage;
        }
        resPage.setRecords(CopyUtil.copyList(page.getRecords(),PageAdminCardVo.class));
        return resPage;
    }

    /******************************************* 名片标签 *************************************************************/

    /**
     * 分页查询标签库
     * @param tagListQo
     * @return
     */
    public IPage<Tag> pageTagList(PageTagListQo tagListQo){
        QueryWrapper<Tag> where = new QueryWrapper<>();
        if(StringUtil.notEmpty(tagListQo.getTagName())){
            where.lambda().like(Tag::getTagName,tagListQo.getTagName());
        }
        where.lambda().orderByDesc(Tag::getCreateTime);
        return this.tagPlusDao.page(new Page<>(tagListQo.getPageNum(),tagListQo.getPageSize()),where);
    }

    /**
     * 获取咨询师标签列表
     * @param cardId
     * @return
     */
    public List<CardTag> getCardTags(String cardId){
        return this.cardTagPlusDao.lambdaQuery()
                .eq(CardTag::getCardId,cardId).list();
    }

    /**
     * 新增或修改名片标签
     * @param cardId
     * @param tagList
     */
    public void saveOrUpdateTag(String cardId,List<CardTagBo> tagList){
        if(CollectionUtils.isEmpty(tagList)){
            return;
        }
        //先删除
        QueryWrapper<CardTag> where = new QueryWrapper<>();
        where.lambda().eq(CardTag::getCardId,cardId);
        this.cardTagPlusDao.remove(where);
        //再插入
        List<CardTag> addList = new ArrayList<>();
        for(CardTagBo tag : tagList){
            addList.add(new CardTag()
                    .setCardId(cardId)
                    .setTagId(tag.getTagId())
                    .setTagName(tag.getTagName()));
        }
        if(!CollectionUtils.isEmpty(addList)){
            this.cardTagPlusDao.saveBatch(addList);
        }
    }

    /**
     * 获取推荐我的名片列表
     * @param cardId
     * @return
     */
    public List<AdminCard> getCardByRecommendMe(String cardId){
        List<AdminCard> resList = new ArrayList<>();
        List<CardShareTeacher> list = this.shareTeacherPlusDao.lambdaQuery()
                .eq(CardShareTeacher::getAdminCardId, cardId)
                .orderByDesc(CardShareTeacher::getId).list();
        if(CollectionUtils.isEmpty(list)){
            return resList;
        }
        List<String> ids = list.stream().map(CardShareTeacher::getCardId).collect(Collectors.toList());
        return this.cardPlusDao.lambdaQuery().in(AdminCard::getId,ids).list();
    }

    /******************************************* 名片联系方式 *************************************************************/

    /**
     * 获取联系方式列表
     * @param cardId
     * @return
     */
    public List<CardContact> getCardContacts(String cardId){
        return this.contactPlusDao.lambdaQuery()
                .eq(CardContact::getCardId,cardId)
                .orderByAsc(CardContact::getContactType).list();
    }

    /**
     * 新增或修改名片联系人
     * @param cardId
     * @param contactList
     */
    public void addOrUpdateCardContacts(String cardId,List<CardContactBo> contactList){
        if(CollectionUtils.isEmpty(contactList)){
            return;
        }
        //先删除
        QueryWrapper<CardContact> where = new QueryWrapper<>();
        where.lambda().eq(CardContact::getCardId,cardId);
        this.contactPlusDao.remove(where);
        //再插入
        List<CardContact> addList = new ArrayList<>();
        for(CardContactBo bo : contactList){
            CardContact addBean = new CardContact();
            BeanUtils.copyProperties(bo,addBean);
            addBean.setCardId(cardId);
            addList.add(addBean);
        }
        if(!CollectionUtils.isEmpty(addList)){
            this.contactPlusDao.saveBatch(addList);
        }
    }

    /******************************************* 名片证书 *************************************************************/

    /**
     * 新增或修改证书
     * @param cardId
     * @param paperList
     */
    public void addOrUpdatePaper(String cardId,List<CardPaperBo> paperList){
        if(CollectionUtils.isEmpty(paperList)){
            return;
        }
        //先删除
        QueryWrapper<CardPaper> where = new QueryWrapper<>();
        where.lambda().eq(CardPaper::getCardId,cardId);
        this.paperPlusDao.remove(where);
        //再插入
        List<CardPaper> addList = new ArrayList<>();
        for(CardPaperBo bo : paperList){
            CardPaper addBean = new CardPaper();
            BeanUtils.copyProperties(bo,addBean);
            addBean.setCardId(cardId);
            addList.add(addBean);
        }
        if(!CollectionUtils.isEmpty(addList)){
            this.paperPlusDao.saveBatch(addList);
        }
    }

    /**
     * 获取名片证书列表
     * @param cardId
     * @return
     */
    public List<CardPaper> getPaperList(String cardId,Integer paperType){
        LambdaQueryChainWrapper<CardPaper> queryChainWrapper = this.paperPlusDao.lambdaQuery()
                .eq(CardPaper::getCardId, cardId);
                if(Objects.nonNull(paperType)){
                    queryChainWrapper.eq(CardPaper::getPaperType,paperType);
                }
        return queryChainWrapper.orderByAsc(CardPaper::getId).list();
    }

    /**
     * 获取名片审核状态
     * @param cardId
     * @return 1：待审核，2：不通过，3：已审核
     */
    public Integer getPaperAuditStatus(String cardId){
        List<CardPaper> paperList = getPaperList(cardId,SystemFinalCode.ONE_VALUE);
        if(CollectionUtils.isEmpty(paperList)){
            return 3;
        }
        Long noPassNum = paperList.stream().filter(item -> item.getPaperStatus() == 2).count();
        Long passNum = paperList.stream().filter(item -> item.getPaperStatus() == 3).count();
        if(passNum == paperList.size()){
            return 3;
        }
        if(noPassNum == paperList.size()){
            return 2;
        }
        return 1;
    }

    /**
     * 审核单个证书
     * @param id
     * @param auditStatus
     * @param reason
     */
    public void auditSinglePaper(String id,Integer auditStatus,String reason){
        this.paperPlusDao.updateById(new CardPaper()
                .setId(id)
                .setPaperStatus(auditStatus)
                .setAuditReason(reason)
        );
    }

    /**
     * 全量审核证书
     * @param cardId
     * @param auditStatus
     * @param reason
     */
    public void auditAllPaper(String cardId,Integer auditStatus,String reason){
        QueryWrapper<CardPaper> where = new QueryWrapper<>();
        where.lambda().eq(CardPaper::getPaperType,SystemFinalCode.ONE_VALUE);
        where.lambda().eq(CardPaper::getCardId,cardId);
        CardPaper updateBean = new CardPaper();
        updateBean.setPaperStatus(auditStatus).setAuditReason(reason);
        this.paperPlusDao.update(updateBean,where);
    }



/************************************ 名片产品、广告 ********************************************************/

    /**
     * 保存名片与产品关系(新建产品推送)
     * @param cardProduct
     */
    public void sendCardProduct(CardProduct cardProduct){
        this.cardProductPlusDao.save(cardProduct);
    }

    /**
     * 新增或修改名片产品信息
     * @param bo
     */
    public void addOrUpdateCardProduct(CardProductBo bo){
        //先删除
        QueryWrapper<CardProduct> where = new QueryWrapper<>();
        where.lambda().eq(CardProduct::getCardId,bo.getCardId());
        this.cardProductPlusDao.remove(where);
        //再插入
        List<CardProduct> addList = new ArrayList<>();
        for(String productId : bo.getProductIdList()){
            CardProduct addBean = new CardProduct();
            addBean.setCardId(bo.getCardId());
            addBean.setProductId(productId);
            addBean.setSellType(SystemFinalCode.ONE_VALUE);
            addList.add(addBean);
        }
        if(!CollectionUtils.isEmpty(addList)){
            this.cardProductPlusDao.saveBatch(addList);
        }
    }

    /**
     * 新增或修改名片广告
     * @param posterBo
     */
    public void addOrUpdateCardPoster(CardPosterBo posterBo){
        //删除
        QueryWrapper<CardPoster> delWhere = new QueryWrapper<>();
        delWhere.lambda().eq(CardPoster::getCardId,posterBo.getCardId());
        this.posterPlusDao.remove(delWhere);
        //新增
        if(StringUtil.notEmpty(posterBo.getCardId())){
            this.posterPlusDao.save(CopyUtil.copy(posterBo,CardPoster.class));
        }
    }

    /**
     * 查询名片产品页信息
     * @param cardId
     * @return
     */
    public CardProductPageVo queryCardProductPageVo(String cardId){
        CardProductPageVo resVo = new CardProductPageVo();
        resVo.setCardId(cardId);
        //1.获取广告
        CardPoster poster = this.posterPlusDao.lambdaQuery().eq(CardPoster::getCardId, cardId).last("limit 1").one();
        if(Objects.nonNull(poster)){
            BeanUtils.copyProperties(poster,resVo);
        }
        //2.获取产品列表
        resVo.setCardProductList(getCardProductList(cardId,SystemFinalCode.ONE_VALUE));
        return resVo;
    }

    /**
     * 获取 名片产品\推荐产品 列表
     * @param cardId
     * @param sellType
     * @return
     */
    public List<ProductPageListVo> getCardProductList(String cardId,Integer sellType){
        List<ProductPageListVo> resList = new ArrayList<>();
        //1.查询商品
        List<CardProduct> list = this.cardProductPlusDao.lambdaQuery()
                .eq(CardProduct::getSellType, sellType).eq(CardProduct::getCardId, cardId)
                .orderByDesc(CardProduct::getCreateTime).list();
        if(CollectionUtils.isEmpty(list)){
            return resList;
        }
        //2.处理商品
        List<String> ids = list.stream().map(CardProduct::getProductId).collect(Collectors.toList());
        List<Product> productList = this.productPlusDao.lambdaQuery()
                .in(Product::getId,ids).orderByDesc(Product::getCreateTime).list();
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        for(CardProduct cardProduct : list){
            ProductPageListVo resItemVo = new ProductPageListVo();
            Product product = productMap.get(cardProduct.getProductId());
            if(Objects.isNull(product)){
                continue;
            }
            BeanUtils.copyProperties(product,resItemVo);
            resItemVo.setId(cardProduct.getId());
            resItemVo.setProductId(cardProduct.getProductId());
            resItemVo.setSellReason(cardProduct.getSellReason());
            //获取产品所属名片咨询师id
            if(StringUtil.notEmpty(product.getCardId())){
                AdminCard productCard = getAdminCardById(product.getCardId());
                if(Objects.nonNull(productCard)){
                    resItemVo.setAdminId(productCard.getAdminId());
                }
            }
            resList.add(resItemVo);
        }
        return resList;
    }


/************************************ 名片分享页 ********************************************************/

    /**
     * 保存分享文件
     * @param bo
     */
    public void addOrUpdateShare(SaveShareFileBo bo){
        //合集的处理方法
        if(bo.getFolderStatus()){
            addOrUpdateFolder(bo);
        }else{//单个分享
            addOrUpdateShareFile(bo);
        }
    }

    /**
     * 新增或修改单个分享
     * @param bo
     */
    public void addOrUpdateShareFile(SaveShareFileBo bo){
        //1.校验、设值
        if(1 == bo.getShareType()){//文件
            if(StringUtil.isEmpty(bo.getFileUrl())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请上传文件");
            }
            if(StringUtil.isEmpty(bo.getSuffixName())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请上传文件名后缀");
            }
        }
        if(2 == bo.getShareType()) {//微信文章
            if(StringUtil.isEmpty(bo.getJumpUrl())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请填写链接");
            }
        }
        if(3 == bo.getShareType() ) {//视频号
            if(StringUtil.isEmpty(bo.getWxVideoSn()) || StringUtil.isEmpty(bo.getVideoId())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请填写微信视频号id和视频id");
            }
        }
        if(3 < bo.getShareType()) {//4：快手视频，5：抖音视频，6：小红书，7：B站
            if(StringUtil.isEmpty(bo.getSearchName())
                    && StringUtil.isEmpty(bo.getJumpUrl())
                    && StringUtil.isEmpty(bo.getFileUrl())){
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"请至少填入一项分享信息");
            }
        }
        CardShareFile share = new CardShareFile();
        BeanUtils.copyProperties(bo,share);
        if(StringUtil.isEmpty(bo.getId())){//新增
            this.cardShareFilePlusDao.save(share);
        }else{
            share.setId(bo.getId());
            this.cardShareFilePlusDao.updateById(share);
        }
    }


    /**
     * 新增或修改合集
     * @param bo
     */
    public void addOrUpdateFolder(SaveShareFileBo bo){
        //校验、设值
        CardShareFile share = new CardShareFile();
        if(StringUtil.isEmpty(bo.getFileUrl())){
            throw new ApiException("请选择合集图标");
        }
        share.setCardId(bo.getCardId());
        share.setFolderStatus(bo.getFolderStatus());
        share.setFileName(bo.getFileName());
        share.setShareName(bo.getShareName());
        share.setFileUrl(bo.getFileUrl());
        share.setJumpUrl(bo.getJumpUrl());
        //新增
        if(StringUtil.isEmpty(bo.getId())){
            this.cardShareFilePlusDao.save(share);
            return;
        }
        //修改
        CardShareFile oldShare = this.cardShareFilePlusDao.getById(bo.getId());
        if(Objects.isNull(oldShare)){
            throw new ApiException("未找到信息");
        }
        share.setId(bo.getId());
        this.cardShareFilePlusDao.updateById(share);
    }

    /**
     * 删除分享文件
     * @param id
     */
    public void delShareFile(String id){
        CardShareFile oldShare = this.cardShareFilePlusDao.getById(id);
        if(Objects.isNull(oldShare)){
            throw new ApiException("未找到信息");
        }
        //删除合集 需要 删除下面文件
        if(oldShare.getFolderStatus()){
            QueryWrapper<CardShareFile> delWhere = new QueryWrapper<>();
            delWhere.lambda().eq(CardShareFile::getFolderId,id);
            this.cardShareFilePlusDao.remove(delWhere);
        }
        this.cardShareFilePlusDao.removeById(id);
    }

    /**
     * 获取分享文件列表
     * @param cardId
     * @return
     */
    public List<SaveShareFileBo> getShareFileList(String cardId){
        List<SaveShareFileBo> resList = new ArrayList<>();
        List<CardShareFile> list = this.cardShareFilePlusDao.lambdaQuery()
                .eq(CardShareFile::getCardId, cardId)
                .eq(CardShareFile::getFolderId,"")
                .orderByDesc(CardShareFile::getCreateTime).list();
        if(CollectionUtil.isEmpty(list)){
            return resList;
        }
        resList.addAll(CopyUtil.copyList(list,SaveShareFileBo.class));
        //循环设置集合下文件数量
        for(SaveShareFileBo bo : resList){
            //如果是集合
            if(bo.getFolderStatus()){
                List<CardShareFile> fileList = this.cardShareFilePlusDao.lambdaQuery().eq(CardShareFile::getFolderId, bo.getId())
                        .eq(CardShareFile::getFolderStatus, false).list();
                //记录集合下的文件类型
                Map<Integer, List<CardShareFile>> map = fileList.stream().collect(Collectors.groupingBy(CardShareFile::getShareType));
                bo.setShareTypeList(new ArrayList<>(map.keySet()));
                //统计集合下文件数量
                bo.setFileNum(fileList.size());
            }
        }
        return resList;
    }

    /**
     * 获取分享合集下的文件
     * @param id
     * @param shareType
     * @return
     */
    public List<SaveShareFileBo> getShareFolderFile(String id,Integer shareType){
        QueryWrapper<CardShareFile> where = new QueryWrapper<>();
        where.lambda().eq(CardShareFile::getFolderId, id);
        if(Objects.nonNull(shareType)){
            where.lambda().eq(CardShareFile::getShareType,shareType);
        }
        where.lambda().orderByDesc(CardShareFile::getCreateTime);
        List<CardShareFile> list = this.cardShareFilePlusDao.list(where);
        if(CollectionUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        return CopyUtil.copyList(list,SaveShareFileBo.class);
    }

    /**
     * 获取分享文件信息
     * @param id
     * @return
     */
    public SaveShareFileBo getShareFileById(String id){
        SaveShareFileBo resBo = new SaveShareFileBo();
        CardShareFile shareFile = this.cardShareFilePlusDao.getById(id);
        if(Objects.isNull(shareFile)){
            throw new ApiException("未找到信息");
        }
        BeanUtils.copyProperties(shareFile,resBo);
        //如果是集合
        if(resBo.getFolderStatus()){
            List<CardShareFile> fileList = this.cardShareFilePlusDao.lambdaQuery().eq(CardShareFile::getFolderId, resBo.getId())
                    .eq(CardShareFile::getFolderStatus, false).orderByDesc(CardShareFile::getCreateTime).list();
            //记录集合下的文件类型
            Map<Integer, List<CardShareFile>> map = fileList.stream().collect(Collectors.groupingBy(CardShareFile::getShareType));
            resBo.setShareTypeList(new ArrayList<>(map.keySet()));
            //统计集合下文件数量
            resBo.setFileNum(fileList.size());
        }
        return resBo;
    }


    /**
     * 保存分享老师信息
     * @param bo
     */
    public void addOrUpdateShareTeacher(BatchTeacherBo bo){
        QueryWrapper<CardShareTeacher> teacherWhere = new QueryWrapper<>();
        teacherWhere.lambda().eq(CardShareTeacher::getCardId,bo.getCardId());
        this.shareTeacherPlusDao.remove(teacherWhere);
        if(!CollectionUtils.isEmpty(bo.getTeacherBoList())){
            this.shareTeacherPlusDao.saveBatch(CopyUtil.copyList(bo.getTeacherBoList(),CardShareTeacher.class));
        }
    }

    /**
     * 删除分享老师
     * @param id
     */
    public void delShareTeacher(String id){
        this.shareTeacherPlusDao.removeById(id);
    }

    /**
     * 获取推荐老师列表
     * @param cardId
     * @return
     */
    public List<CardShareTeacherVo> getShareTeacherList(String cardId){
        List<CardShareTeacherVo> resList = new ArrayList<>();
        //查询推荐老师列表
        List<CardShareTeacher> list = this.shareTeacherPlusDao.lambdaQuery()
                .eq(CardShareTeacher::getCardId, cardId).orderByDesc(CardShareTeacher::getId).list();
        if(CollectionUtil.isEmpty(list)){
            return resList;
        }
        List<String> cardIdList = list.stream().map(CardShareTeacher::getAdminCardId).collect(Collectors.toList());
        List<AdminCard> cardList = this.cardPlusDao.lambdaQuery()
                .in(AdminCard::getId, cardIdList).eq(AdminCard::getDataStatus, Boolean.TRUE)
                .orderByDesc(AdminCard::getCreateTime).list();
        Map<String, AdminCard> cardMap = cardList.stream().collect(Collectors.toMap(AdminCard::getId, Function.identity()));
        for(CardShareTeacher shareTeacher : list){
            CardShareTeacherVo resVo = new CardShareTeacherVo();
            AdminCard adminCard = cardMap.get(shareTeacher.getAdminCardId());
            BeanUtils.copyProperties(adminCard,resVo);
            resVo.setTags(this.getCardTags(shareTeacher.getAdminCardId()));
            resVo.setId(shareTeacher.getId());
            resVo.setSellReason(shareTeacher.getSellReason());
            resVo.setAdminCardId(shareTeacher.getAdminCardId());
            resList.add(resVo);
        }
        return resList;
    }

    /**
     * 保存分享商品
     * @param bo
     */
    public void saveOrUpdateShareProduct(BatchShareProductBo bo){
        QueryWrapper<CardProduct> productWhere = new QueryWrapper<>();
        productWhere.lambda().eq(CardProduct::getCardId,bo.getCardId());
        productWhere.lambda().eq(CardProduct::getSellType,SystemFinalCode.MENU_LEVEL_2_VALUE);
        this.cardProductPlusDao.remove(productWhere);
        if(!CollectionUtils.isEmpty(bo.getProductBoList())){
            this.cardProductPlusDao.saveBatch(CopyUtil.copyList(bo.getProductBoList(),CardProduct.class));
        }
    }

    /**
     * 删除分享商品
     * @param id
     */
    public void delShareProduct(String id){
        this.cardProductPlusDao.removeById(id);
    }

    /**
     * 查询分享商品
     * @param cardId
     * @return
     */
    public List<ProductPageListVo> getShareProductList(String cardId){
        return getCardProductList(cardId,SystemFinalCode.MENU_LEVEL_2_VALUE);
    }

/*****************************************  管理后台 ********************************************************/

    /**
     * 管理后台新增名片第一页信息
     * @param bo
     */
    @RedisLock(key = "#bo.adminId",isWaitForLock = false)
    public String manageAddOrUpdateCardInfo(ManageAddOrUpdateCardBo bo){
        //操作基础信息
        AddOrUpdateCardBaseQo qo = new AddOrUpdateCardBaseQo();
        BeanUtils.copyProperties(bo,qo);
        //咨询师昵称
        User userByAdminId = userService.getUserByAdminId(bo.getAdminId());
        if(Objects.nonNull(userByAdminId)){
            qo.setNickName(userByAdminId.getNickName());
            qo.setUserImg(userByAdminId.getUserImg());
            qo.setBusinessId(userByAdminId.getBusinessId());
        }
        Business businessById = this.businessService.getBusinessById(userByAdminId.getBusinessId());
        if(Objects.nonNull(businessById)){
            qo.setShortName(businessById.getShortName());
        }
        String cardId = this.addOrUpdateCardBaseInfo(qo);
        //新增或修改标签信息
        this.saveOrUpdateTag(cardId,bo.getTagList());
        this.addOrUpdateCardContacts(cardId,bo.getContactList());
        this.addOrUpdatePaper(cardId,bo.getPaperList());
        return cardId;
    }

    /**
     * 管理后台保存名片产品页
     * @param bo
     */
    @Transactional(rollbackFor = Exception.class)
    public void manageAddOrUpdateCardProduct(ManageAddOrUpdateProduct bo){
        //广告
        CardPosterBo posterBo = new CardPosterBo();
        posterBo.setCardId(bo.getId())
                .setProductId(bo.getProductId())
                .setPosterUrl(bo.getPosterUrl());
        this.addOrUpdateCardPoster(posterBo);
        //产品
        CardProductBo productBo = new CardProductBo();
        productBo.setCardId(bo.getId()).setProductIdList(bo.getProductIdList());
        this.addOrUpdateCardProduct(productBo);
    }

    /**
     * 管理后台保存名片分享页
     * @param bo
     */
    @Transactional(rollbackFor = Exception.class)
    public void manageAddOrUpdateCardShare(ManageAddOrUpdateShareBo bo){
        //推荐老师
        QueryWrapper<CardShareTeacher> teacherWhere = new QueryWrapper<>();
        teacherWhere.lambda().eq(CardShareTeacher::getCardId,bo.getId());
        this.shareTeacherPlusDao.remove(teacherWhere);
        if(!CollectionUtils.isEmpty(bo.getTeacherBoList())){
            this.shareTeacherPlusDao.saveBatch(CopyUtil.copyList(bo.getTeacherBoList(),CardShareTeacher.class));
        }
        //推荐服务
        QueryWrapper<CardProduct> productWhere = new QueryWrapper<>();
        productWhere.lambda().eq(CardProduct::getCardId,bo.getId());
        productWhere.lambda().eq(CardProduct::getSellType,SystemFinalCode.MENU_LEVEL_2_VALUE);
        this.cardProductPlusDao.remove(productWhere);
        if(!CollectionUtils.isEmpty(bo.getProductBoList())){
            this.cardProductPlusDao.saveBatch(CopyUtil.copyList(bo.getProductBoList(),CardProduct.class));
        }
        //推荐资料
        QueryWrapper<CardShareFile> shareFileWhere = new QueryWrapper<>();
        shareFileWhere.lambda().eq(CardShareFile::getCardId,bo.getId());
        this.cardShareFilePlusDao.remove(shareFileWhere);
        if(!CollectionUtils.isEmpty(bo.getFileBoList())){
            this.cardShareFilePlusDao.saveBatch(CopyUtil.copyList(bo.getFileBoList(),CardShareFile.class));
        }
    }

    /**
     * 查询分享页数据
     * @param cardId
     * @return
     */
    public ManageCardShareVo getCardShareVo(String cardId){
        ManageCardShareVo resVo = new ManageCardShareVo();
        resVo.setTeacherVoList(this.getShareTeacherList(cardId));
        resVo.setProductVoList(this.getShareProductList(cardId));
        resVo.setFileVoList(this.getShareFileList(cardId));
        return resVo;
    }

    /**
     * 生成名片二维码
     * @param id
     * @return
     */
    public String createCardQrCode(String id){
        String scene = "cardId="+id+"&e=d";
        try {
            String qrCode = weChatApiService.createQrCodeAndUpload(SystemFinalCode.CARD_DETAIL_PAGE,scene);
            this.cardPlusDao.updateById(new AdminCard().setId(id).setQrCode(qrCode));
            return qrCode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("生成名片二维码失败");
        }
    }

    /**
     * 互帮榜跳转逻辑
     * @param adminId
     * @param sjId
     * @return
     */
    public CardJumpVo toPage(String adminId,String sjId){
        CardJumpVo resVo = new CardJumpVo();
        AdminCard card = this.getCardByAdminId(adminId, sjId);
        if(Objects.isNull(card)){
            resVo.setJumpType(2);
            return resVo;
        }
        resVo.setCardId(card.getId());
        resVo.setJumpType(1);
        return resVo;
    }

    /**
     * 初始化名片二维码
     */
    public void infoCardQrCode(){
        //1.查询所有没有二维码的名片
        List<AdminCard> list = this.cardPlusDao.lambdaQuery()
                .eq(AdminCard::getDataStatus, Boolean.TRUE).eq(AdminCard::getQrCode, "").list();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //2.生成二维码
        for(AdminCard card : list){
            String cardQrCode = createCardQrCode(card.getId());
            this.cardPlusDao.updateById(new AdminCard().setId(card.getId()).setQrCode(cardQrCode));
        }
    }

    /**
     * 删除名片
     * @param id
     */
    public void delAdminCard(String id){
        AdminCard card = this.cardPlusDao.getById(id);
        if(Objects.isNull(card)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到名片数据");
        }
        //1.删除名片
        this.cardPlusDao.updateById(new AdminCard().setId(id).setDataStatus(Boolean.FALSE));
        //2.解除绑定商品
        this.productPlusDao.lambdaUpdate().set(Product::getCardId,"").eq(Product::getCardId,id).update();
    }



    /**
     * 获取并新增名片信息
     * @param adminId
     * @return

     @RedisLock(key = "#adminId",isWaitForLock = false)
     public AdminCardVo getOrAddCardInfo(String adminId,String sjId){
     AdminCardVo resVo = new AdminCardVo();
     //根据咨询师id获取名片
     AdminCard cardByAdminId = this.getCardByAdminId(adminId,sjId);
     //如果没有则生成个新的名片返回
     if(Objects.isNull(cardByAdminId)){
     Admin admin = this.adminService.getAdminById(adminId);
     if(Objects.isNull(admin)){
     throw new ApiException("未找到咨询师信息");
     }
     if(admin.getRoleId().equals(RoleTypeEnum.SJ_ASSISTANT.getValue())){
     throw new ApiException("暂未对助教开放名片功能");
     }
     //校验商家名片数量
     checkSjCardNum(admin.getBusinessId());
     PageBusinessVo sjInfo = businessService.getSjInfo(admin.getBusinessId());
     AdminCard adminCard = new AdminCard();
     adminCard.setAdminId(adminId);
     adminCard.setBusinessId(admin.getBusinessId());
     adminCard.setShortName(sjInfo.getShortName());
     adminCard.setFullName(sjInfo.getFullName());
     adminCard.setTrueName(admin.getTrueName());
     this.cardPlusDao.save(adminCard);
     //生成二维码
     String cardQrCode = createCardQrCode(adminCard.getId());
     BeanUtils.copyProperties(adminCard,resVo);
     resVo.setId(adminCard.getId());
     resVo.setQrCode(cardQrCode);
     resVo.setFullName(sjInfo.getFullName());
     return resVo;
     }
     return getCardInfo(cardByAdminId.getId());
     }*/



}
