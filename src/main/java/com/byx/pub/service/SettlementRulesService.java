package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.SaveSettlementRulesBo;
import com.byx.pub.bean.qo.PageSettlementRulesQo;
import com.byx.pub.bean.vo.PageSettlementRulesVo;

/**
 * @author Jump
 * @date 2023/8/18 14:37:08
 */
public interface SettlementRulesService {

    /**
     * 新增或修改商家规则
     * @param rulesBo
     */
    void saveSjSettlementRules(SaveSettlementRulesBo rulesBo);

    /**
     * 分页条件查询
     * @param qo
     * @return
     */
    Page<PageSettlementRulesVo> pageList(PageSettlementRulesQo qo);

    /**
     * 获取规则详情
     * @param id
     * @return
     */
    SaveSettlementRulesBo getRulesDetail(String id);
}
