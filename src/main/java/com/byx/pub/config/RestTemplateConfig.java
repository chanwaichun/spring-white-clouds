package com.byx.pub.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * spring用于访问Rest服务的同步客户端
 * @author Jamin
 * @Date 2023/2/3 14:08
 */
@Configuration
public class RestTemplateConfig {
    private static final Log logger = LogFactory.getLog(RestTemplateConfig.class);

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30 * 1000);
        factory.setConnectTimeout(30 * 1000);
        return factory;
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory factory) {
        logger.info("系统外部调用客户端配置：restTemplate build");
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
