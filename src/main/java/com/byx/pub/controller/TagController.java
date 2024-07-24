package com.byx.pub.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byx.pub.bean.qo.TagPageQo;
import com.byx.pub.plus.entity.Tag;
import com.byx.pub.service.TagService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * [管理后台]-[标签]管理Api
 * @Author Jump
 * @Date 2023/10/3 15:54
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/tag")
@Api(value = "[管理后台]-[标签]管理Api",tags = "[管理后台]-[标签]管理Api")
public class TagController {
    @Resource
    TagService tagService;

    /**
     * 保存标签
     * @param tagName 标签命
     * @return
     */
    @GetMapping("/save")
    @ApiOperation(value = "保存标签")
    public CommonResult<Void> saveTag(
        @ApiParam(required = true, value = "标签命") @RequestParam("tagName") String tagName
    ){
        tagService.saveTag(tagName);
        return CommonResult.success();
    }

    /**
     * 修改标签
     * @param id 标签id
     * @param tagName 标签命
     * @return
     */
    @GetMapping("/update")
    @ApiOperation(value = "修改标签")
    public CommonResult<Void> updateTag(
        @ApiParam(required = true, value = "标签id") @RequestParam("id") String id,
        @ApiParam(required = true, value = "标签命") @RequestParam("tagName") String tagName
    ){
        tagService.updateTag(id,tagName);
        return CommonResult.success();
    }

    /**
     * 分页查询标签
     * @param qo
     * @return
     */
    @PostMapping("/page/list")
    @ApiOperation(value = "分页查询")
    public CommonResult<IPage<Tag>> pageList(
        @Validated @RequestBody TagPageQo qo
    ){
        return CommonResult.success(this.tagService.pageList(qo));
    }



}
