package com.byx.pub.service;

import com.byx.pub.bean.qo.PageManageOrdersQo;
import com.byx.pub.bean.vo.WorkPanelVo;

/**
 * 首页、工作面板服务
 * @Author Jump
 * @Date 2023/6/14 21:23
 */
public interface WorkPanelService {
    /**
     * 管理后台获取看板数据
     * @param qo
     * @return
     */
    WorkPanelVo queryPanelData(PageManageOrdersQo qo);



}
