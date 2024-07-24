package com.byx.pub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Jump
 * @Date 2023/9/25 22:11
 */
@Component
@ConfigurationProperties(prefix = "cos")
@Data
public class CosConfig {
    /**
     * 腾讯云账号秘钥
     */
    private String secretId;
    /**
     * 密码秘钥
     */
    private String secretKey;
    /**
     * 存储桶地区
     */
    private String region;
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 存储桶访问路径
     */
    private String path;
}
