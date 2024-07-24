package com.byx.pub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description 白云乡管理后台服务
 * @Author Jump
 * @Version 1.0
 **/
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = {"com.byx.pub.plus.mapper","com.byx.pub.mapper"})
@ServletComponentScan(basePackages = {"com.byx.pub.config","com.byx.pub.filter"})
@EnableAsync
public class whiteCloudsApplication {


    public static void main(String [] args){

        SpringApplication.run(whiteCloudsApplication.class,args);
    }


}

