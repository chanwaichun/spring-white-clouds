package com.byx.pub.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jump
 * @date 2023/5/30 16:56:21
 */
public interface OrderProtocolService {
    /**
     * 上传协议
     * @param orderSn
     * @param pictures
     */
    String setProtocol(String orderSn,MultipartFile[] pictures);

    /**
     * 下载协议模板
     * @param response
     */
    void downloadCheck(HttpServletResponse response) throws IOException;

    /**
     * 预览协议
     * @param response
     * @throws IOException
     */
    void previewCheck(HttpServletResponse response) throws IOException;

}
