package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.bo.AddHelpRecordBo;
import com.byx.pub.bean.qo.PageHelpListQo;
import com.byx.pub.bean.vo.HelpListVo;
import com.byx.pub.service.HelpListService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [客户端]-[商家]-[互帮榜]服务Api
 * @author Jump
 * @date 2023/8/16 17:23:51
 */
@RestController
@RequestMapping("/white/clouds/front/business/v1/help/list")
@Api(value = "[客户端]-[商家]-[互帮榜]服务Api",tags = "[客户端]-[商家]-[互帮榜]服务Api")
public class ConsultantHelpListController {
    @Resource
    HelpListService helpListService;

    /**
     * 保存帮助记录
     * @param addHelpRecordBo
     * @return
     */
    @PostMapping("/save/record")
    @ApiOperation(value = "保存帮助记录")
    public CommonResult<Void> saveRecord(
            @Validated @RequestBody AddHelpRecordBo addHelpRecordBo
    ){
        this.helpListService.saveHelpRecord(addHelpRecordBo);
        return CommonResult.success();
    }

    /**
     * 分页条件查询互帮榜
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页条件查询互帮榜")
    public CommonResult<Page<HelpListVo>> pageList(
        @ApiParam(value = "咨询师id(请求头)") @RequestHeader(value = "admin-id",required = false) String adminId,
        @Validated @RequestBody PageHelpListQo qo
    ){
        qo.setAdminId(adminId);
        return CommonResult.success(this.helpListService.pageHelpList(qo));
    }



}
