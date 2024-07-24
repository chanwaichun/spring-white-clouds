package com.byx.pub.controller.front;

import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.service.OrderProtocolService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * [客户端]-[咨询师、用户]-[订单]协议服务Api
 * @author Jump
 * @date 2023/5/30 16:57:07
 */
@RestController
@RequestMapping("/white/clouds/front/v1/protocol")
@Api(value = "[客户端]-[咨询师、用户]-[订单]协议服务Api",tags = "[客户端]-[咨询师、用户]-[订单]协议服务Api")
public class FrontProtocolController {
    @Resource
    OrderProtocolService protocolService;
    @Value("${spring.servlet.multipart.location}")
    String locationStr;


    /**
     * 上传协议
     * @param orderSn 订单号
     * @param pictures 图片集合
     * @return
     */
    @PostMapping( value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传协议")
    public CommonResult<String> uploadImgList(
            @ApiParam(value = "订单号") @RequestParam("orderSn")String orderSn,
            @ApiParam(value = "图片集合")@RequestParam("pictures") MultipartFile[] pictures
    ) {
        if(pictures.length == 0 || pictures.length > 5){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"上传协议照片数量1-5张");
        }
        return CommonResult.success(protocolService.setProtocol(orderSn,pictures));
    }

    /**
     * 下载协议
     * @param resp
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载协议")
    public void downloadProtocol(HttpServletResponse resp)throws IOException {
        this.protocolService.downloadCheck(resp);
    }

    /**
     * 预览协议
     * @param resp
     */
    @GetMapping("/preview")
    @ApiOperation(value = "预览协议")
    public void previewProtocol(HttpServletResponse resp) throws IOException {
        this.protocolService.previewCheck(resp);
    }

    /**
     * 上传用户头像
     * @param userId
     * @param img
     * @return
     */
    @PostMapping("/upload/user/img")
    @ApiOperation(value = "上传用户头像")
    public CommonResult<String> upload(
            @ApiParam(value = "用户id") @RequestHeader(value = "user-id",required = false)String userId,
            @ApiParam(required = true, value = "上传图片") @RequestParam("img") MultipartFile img
    ) {
        try{
            //获取文件名 并 重命名
            String fileName = StringUtil.getFileReName(Objects.requireNonNull(img.getOriginalFilename()));
            //文件存储路径地址
            String destFileName = this.locationStr + File.separator + "productUploadImg" + File.separator + fileName;
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            img.transferTo(destFile);
            return CommonResult.success(fileName);
        } catch (Exception e) {
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"上传用户头像失败");
        }
    }



}
