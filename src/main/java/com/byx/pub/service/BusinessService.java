package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateBusinessQo;
import com.byx.pub.bean.qo.PageBusinessQo;
import com.byx.pub.bean.vo.PageBusinessVo;
import com.byx.pub.bean.vo.SelectBusinessListVo;
import com.byx.pub.plus.entity.Business;

import java.util.List;

/**
 * @Author Jump
 * @Date 2023/7/14 22:09
 */
public interface BusinessService {
    /**
     * 新增或修改商家
     * @param qo
     */
    void addOrUpdateBusiness(AddOrUpdateBusinessQo qo);

    /**
     * 分页查询商家列表
     * @param qo
     * @return
     */
    Page<PageBusinessVo> pageList(PageBusinessQo qo);

    /**
     * 获取并校验商家信息
     * @param id
     * @return
     */
    PageBusinessVo getSjInfo(String id);

    /**
     * 选择商家列表
     * @param roleId
     * @param sjId
     * @return
     */
    List<SelectBusinessListVo> getSjListByRole(String roleId, String sjId);

    /**
     * 根据id查询
     * @param sjId
     * @return
     */
     Business getBusinessById(String sjId);
}
