package com.byx.pub.controller;

import com.byx.pub.bean.vo.FlyerDealDetailVo;
import com.byx.pub.bean.vo.SupplierRelayVo;
import com.byx.pub.service.FlyerInfoService;
import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * [管理后台]-[视图]管理Api
 * @Author Jump
 * @Date 2023/12/14 23:22
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/view")
@Api(value = "[管理后台]-[视图]管理Api",tags = "[管理后台]-[视图]管理Api")
public class ViewController {
    @Resource
    FlyerInfoService flyerInfoService;

    @RequestMapping(value = "/relay/list")
    public ModelAndView index(Model model) {
        List<SupplierRelayVo> relayList = this.flyerInfoService.listRelay();
        model.addAttribute("relayList",relayList);
        List<FlyerDealDetailVo> relayDealDetailList = this.flyerInfoService.getRelayDealDetailList();
        model.addAttribute("relayDealDetailList",relayDealDetailList);
        return new ModelAndView("index");
    }



}
