
package com.byx.pub.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置
 * @Author: Jump
 * @Date: 2023年5月8日
 */
@Configuration
@MapperScan({"com.byx.pub.plus.com"})
public class BaseMyBatisPlusConfiguration {

    /**
     * 自定义注入字段
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new IMetaObjectHandler();
    }
}