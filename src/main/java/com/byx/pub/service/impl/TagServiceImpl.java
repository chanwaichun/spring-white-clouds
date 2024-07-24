package com.byx.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.TagPageQo;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.CardTagPlusDao;
import com.byx.pub.plus.dao.TagPlusDao;
import com.byx.pub.plus.entity.CardTag;
import com.byx.pub.plus.entity.Tag;
import com.byx.pub.service.TagService;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/10/3 15:30
 */
@Slf4j
@Service
public class TagServiceImpl implements TagService {
    @Resource
    TagPlusDao tagPlusDao;
    @Resource
    CardTagPlusDao cardTagPlusDao;

    /**
     * 修改标签
     * @param id
     * @param tagName
     */
    public void updateTag(String id,String tagName){
        Tag byId = this.tagPlusDao.getById(id);
        if(Objects.isNull(byId)){
            throw new ApiException("未找到标签");
        }
        //校验名称是否存在
        if(Objects.nonNull(this.getTagByName(tagName))){
            throw new ApiException("标签名称已存在");
        }
        this.tagPlusDao.updateById(new Tag().setId(id).setTagName(tagName));
        //同步修改名片标签表
        this.cardTagPlusDao.lambdaUpdate()
                .eq(CardTag::getTagId,id)
                .set(CardTag::getTagName,tagName)
                .update();
    }

    /**
     * 保存标签
     * @param tagName
     */
    public void saveTag(String tagName){
        //校验名称是否存在
        if(Objects.nonNull(this.getTagByName(tagName))){
            throw new ApiException("标签名称已存在");
        }
        this.tagPlusDao.save(new Tag().setTagName(tagName));
    }

    /**
     * 根据标签名获取标签
     * @param tagName
     * @return
     */
    public Tag getTagByName(String tagName){
        return this.tagPlusDao.lambdaQuery().eq(Tag::getTagName, tagName).last("limit 1").one();
    }

    /**
     * 分页查询标签
     * @param qo
     * @return
     */
    public IPage<Tag> pageList(TagPageQo qo){
        QueryWrapper<Tag> where = new QueryWrapper<>();
        if(StringUtil.notEmpty(qo.getTagName())){
            where.lambda().like(Tag::getTagName,qo.getTagName());
        }
        return this.tagPlusDao.page(new Page<>(qo.getPageNum(),qo.getPageSize()),where);
    }


}
