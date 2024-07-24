package com.byx.pub.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.*;
import com.byx.pub.bean.qo.AddOrUpdateCardBaseQo;
import com.byx.pub.bean.qo.PageAdminCardQo;
import com.byx.pub.bean.qo.PageTagListQo;
import com.byx.pub.bean.vo.*;
import com.byx.pub.plus.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/7/16 21:16
 */
public interface AdminCardService {
    /**
     * 分页条件查询名片列表
     * @param cardQo
     * @return
     */
    Page<PageAdminCardVo> pageListCard(PageAdminCardQo cardQo);

    /**
     * 获取名片信息
     * @param cardId
     * @return
     */
    AdminCardVo getCardInfo(String cardId);

    /**
     * 获取商家下的名片列表
     * @param sjId
     * @return
     */
    List<AdminCard> getCardListBySj(String sjId);

    /**
     * 根据id查询名片
     * @param carId
     * @return
     */
    AdminCard getAdminCardById(String carId);

    /**
     * 根据adminId获取名片
     * @param adminId
     * @return
     */
    AdminCard getCardByAdminId(String adminId,String sjId);

    /**
     * 修改名片（内部同步）
     * @param updateBean
     */
    void updateCard(AdminCard updateBean);

    /**
     * 保存名片与产品关系(新建产品推送)
     * @param cardProduct
     */
    void sendCardProduct(CardProduct cardProduct);

    /**
     * 新增或修改名片基础信息
     * @param qo
     * @return
     */
    String addOrUpdateCardBaseInfo(AddOrUpdateCardBaseQo qo);

    /**
     * 分页查询标签库
     * @param tagListQo
     * @return
     */
    IPage<Tag> pageTagList(PageTagListQo tagListQo);

    /**
     * 新增或修改名片标签
     * @param cardId
     * @param tagList
     */
     void saveOrUpdateTag(String cardId,List<CardTagBo> tagList);

    /**
     * 新增或修改名片联系人
     * @param cardId
     * @param contactList
     */
    void addOrUpdateCardContacts(String cardId,List<CardContactBo> contactList);

    /**
     * 新增或修改证书
     * @param cardId
     * @param paperList
     */
    void addOrUpdatePaper(String cardId,List<CardPaperBo> paperList);

    /**
     * 新增或修改名片产品信息
     * @param bo
     */
    void addOrUpdateCardProduct(CardProductBo bo);

    /**
     * 获取名片审核状态
     * @param cardId
     * @return 1：待审核，2：不通过，3：已审核
     */
    Integer getPaperAuditStatus(String cardId);

    /**
     * 获取名片证书列表
     * @param cardId
     * @param paperType
     * @return
     */
    List<CardPaper> getPaperList(String cardId, Integer paperType);

    /**
     * 审核单个证书
     * @param id
     * @param auditStatus
     * @param reason
     */
    void auditSinglePaper(String id,Integer auditStatus,String reason);

    /**
     * 全量审核证书
     * @param cardId
     * @param auditStatus
     * @param reason
     */
    void auditAllPaper(String cardId,Integer auditStatus,String reason);

    /**
     * 新增或修改名片广告
     * @param posterBo
     */
    void addOrUpdateCardPoster(CardPosterBo posterBo);

    /**
     * 查询名片产品页信息
     * @param cardId
     * @return
     */
    CardProductPageVo queryCardProductPageVo(String cardId);

    /**
     * 保存分享文件(单个)
     * @param shareBo
     */
    void addOrUpdateShare(SaveShareFileBo shareBo);

    /**
     * 删除分享文件
     * @param id
     */
    void delShareFile(String id);

    /**
     * 获取分享文件列表
     * @param cardId
     * @return
     */
    List<SaveShareFileBo> getShareFileList(String cardId);

    /**
     * 获取分享文件信息
     * @param id
     * @return
     */
    SaveShareFileBo getShareFileById(String id);

    /**
     * 获取分享合集下的文件
     * @param id
     * @param shareType
     * @return
     */
    List<SaveShareFileBo> getShareFolderFile(String id,Integer shareType);

    /**
     * 保存分享老师信息
     * @param bo
     */
    void addOrUpdateShareTeacher(BatchTeacherBo bo);

    /**
     * 删除分享老师
     * @param id
     */
    void delShareTeacher(String id);

    /**
     * 获取推荐老师列表
     * @param cardId
     * @return
     */
    List<CardShareTeacherVo> getShareTeacherList(String cardId);

    /**
     * 保存分享商品
     * @param bo
     */
    void saveOrUpdateShareProduct(BatchShareProductBo bo);

    /**
     * 删除分享商品
     * @param id
     */
    void delShareProduct(String id);

    /**
     * 查询分享商品
     * @param cardId
     * @return
     */
    List<ProductPageListVo> getShareProductList(String cardId);

    /**
     * 查询分享页数据
     * @param cardId
     * @return
     */
    ManageCardShareVo getCardShareVo(String cardId);

    /**
     * 管理后台新增名片第一页信息
     * @param bo
     */
    String manageAddOrUpdateCardInfo(ManageAddOrUpdateCardBo bo);

    /**
     * 管理后台保存名片产品页
     * @param bo
     */
    void manageAddOrUpdateCardProduct(ManageAddOrUpdateProduct bo);

    /**
     * 管理后台保存名片分享页
     * @param bo
     */
    void manageAddOrUpdateCardShare(ManageAddOrUpdateShareBo bo);

    /**
     * 获取咨询师标签列表
     * @param cardId
     * @return
     */
    List<CardTag> getCardTags(String cardId);

    /**
     * 获取推荐我的名片列表
     * @param cardId
     * @return
     */
    List<AdminCard> getCardByRecommendMe(String cardId);


    /**
     * 互帮榜跳转逻辑
     * @param adminId
     * @param sjId
     * @return
     */
    CardJumpVo toPage(String adminId,String sjId);

    /**
     * 初始化名片二维码
     */
    void infoCardQrCode();

    /**
     * 删除名片
     * @param id
     */
    void delAdminCard(String id);

    /**
     * 获取并新增名片信息
     * @param adminId
     * @return

    AdminCardVo getOrAddCardInfo(String adminId,String sjId);*/
}

