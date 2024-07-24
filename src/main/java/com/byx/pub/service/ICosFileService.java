package com.byx.pub.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Jump
 * @Date 2023/9/25 22:18
 */
public interface ICosFileService {

    /**
     * 上传文件到腾讯云cos
     * @param file
     * @return
     */
    String upload(MultipartFile file);

}
