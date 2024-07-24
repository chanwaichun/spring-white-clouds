package com.byx.pub.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byx.pub.bean.qo.TagPageQo;
import com.byx.pub.plus.entity.Tag;

/**
 * @Author Jump
 * @Date 2023/10/3 15:31
 */
public interface TagService {
    /**
     * 保存标签
     * @param tagName
     */
    void saveTag(String tagName);

    /**
     * 修改标签
     * @param id
     * @param tagName
     */
    void updateTag(String id,String tagName);

    /**
     * 分页查询标签
     * @param qo
     * @return
     */
    IPage<Tag> pageList(TagPageQo qo);
}
