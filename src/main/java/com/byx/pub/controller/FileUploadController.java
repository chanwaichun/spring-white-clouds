package com.byx.pub.controller;

import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.service.ICosFileService;
import com.byx.pub.util.CommonResult;
import com.byx.pub.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * [管理后台]-[文件上传服务]管理Api
 * @Author Jump
 * @Date 2023/6/17 17:55
 */
@RestController
@RequestMapping("/white/clouds/manage/v1/file")
@Api(value = "[管理后台]-[文件上传服务]管理Api",tags = "[管理后台]-[文件上传服务]管理Api")
public class FileUploadController {
    @Value("${spring.servlet.multipart.location}")
    String locationStr;
    @Resource
    ICosFileService iCosFileService;

    /**
     * 上传文件到腾讯云cos
     * @param file
     * @return
     */
    @PostMapping("/cos/upload/file")
    @ApiOperation(value = "上传文件到腾讯云cos")
    public CommonResult<String> uploadCos(
            @ApiParam(required = true, value = "上传图片") @RequestParam("img") MultipartFile file
    ) {
        return CommonResult.success(iCosFileService.upload(file));
    }

    /**
     * 上传商品图片
     * @param img
     * @return
     */
    @PostMapping("/upload/img")
    @ApiOperation(value = "上传商品图片")
    public CommonResult<String> upload(
            @ApiParam(required = true, value = "上传图片") @RequestParam("img") MultipartFile img
    ){
        return CommonResult.success(uploadImg(img));
    }

    /**
     * 多图片上传
     * @param pictures 图片集合
     * @return
     */
    @PostMapping(value = "/upload/img/list",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "多图片上传")
    public CommonResult<String> uploadImgList(
            @ApiParam(value = "图片集合")@RequestParam("pictures") MultipartFile[] pictures
    ) {
        //上传图片的路径集合
        List<String> imgUrls = new ArrayList<>();
        //最多上传5张图片
        if(pictures.length == 0 || pictures.length > 5){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"最少一张，最多五张图片");
        }
        //循环上传
        for( MultipartFile img : pictures){
            imgUrls.add(uploadImg(img));
        }
        return CommonResult.success(String.join(",", imgUrls));
    }


    public String uploadImg(MultipartFile img){
        try{
            //获取文件名 并 重命名
            String fileName = StringUtil.getFileReName(Objects.requireNonNull(img.getOriginalFilename()));
            //文件存储路径地址
            String destFileName = this.locationStr + File.separator + "productUploadImg" + File.separator + fileName;
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            img.transferTo(destFile);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"上传图片失败");
        }
    }


}
