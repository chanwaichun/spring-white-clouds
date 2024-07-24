package com.byx.pub.service.impl;


import com.byx.pub.config.CosConfig;
import com.byx.pub.service.ICosFileService;
import com.byx.pub.util.StringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author Jump
 * @Date 2023/9/25 22:18
 */
@Slf4j
@Service
public class ICosFileServiceImpl implements ICosFileService {
    @Resource
    private CosConfig cosConfig;
    @Value("${spring.servlet.multipart.location}")
    String locationStr;


    /**
     * 上传文件到腾讯云cos
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        COSClient cosClient = null;
        try {
            //获取文件名 并 重命名
            String fileName = StringUtil.getFileReName(Objects.requireNonNull(file.getOriginalFilename()));
            // 设置文件路径
            String filePath = "productUploadImg" + "/" + fileName;
            // 1 初始化用户身份信息（secretId, secretKey）。
            COSCredentials cred = new BasicCOSCredentials(cosConfig.getSecretId(), cosConfig.getSecretKey());
            // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
            Region region = new Region(cosConfig.getRegion());
            ClientConfig clientConfig = new ClientConfig(region);
            // 这里建议设置使用 https 协议 从 5.6.54 版本开始，默认使用了 https
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);
            // 指定要上传的文件
            File localFile = transferToFile(file);
            // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), filePath, localFile);
            cosClient.putObject(putObjectRequest);
            return cosConfig.getPath()+filePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return "";
    }


    public File transferToFile(MultipartFile multipartFile) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(multipartFile.getOriginalFilename(), "");
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return tempFile;
    }

    /**
     * 删除文件
     * @param fileName

    public void delete(String fileName) {
        cosConfig.cosClient();
        // 文件桶内路径
        String filePath = this.locationStr + File.separator + "productUploadImg" + File.separator + fileName;
        cosClient.deleteObject(cosConfig.getBucketName(), filePath);
    }*/

    /**
     * 生成文件路径
     * @param originalFileName 原始文件名称
     * @param folder 存储路径
     * @return

    private String getFilePath(String originalFileName, String folder) {
        // 获取后缀名
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 以文件后缀来存储在存储桶中生成文件夹方便管理
        String filePath = folder + "/";
        // 去除文件后缀 替换所有特殊字符
        String fileStr = StrUtil.removeSuffix(originalFileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
        filePath += new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
        log.info("filePath：" + filePath);
        return filePath;
    }*/





}
