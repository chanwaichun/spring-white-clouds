package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddHelpRecordBo;
import com.byx.pub.bean.qo.PageHelpListQo;
import com.byx.pub.bean.vo.HelpListVo;
import com.byx.pub.bean.vo.PageHomeHelpVo;

/**
 * @author Jump
 * @date 2023/8/16 15:17:39
 */
public interface HelpListService {
    /**
     * 保存互帮信息
     * @param addHelpRecordBo
     */
    void saveHelpRecord(AddHelpRecordBo addHelpRecordBo);

    /**
     * 首页互帮榜信息
     * @param adminId
     * @return
     */
    PageHomeHelpVo getPageHomeHelpVo(String adminId);

    /**
     * 分页条件查询互帮榜
     * @param qo
     * @return
     */
    Page<HelpListVo> pageHelpList(PageHelpListQo qo);
}
