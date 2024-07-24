package com.byx.pub.config;

/**
 *
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * API文档设置
 * com.qichuan.sugar.minaapi.SwaggerConfig
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket managerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .groupName("白云乡服务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.byx.pub.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiBInfo())
                .globalOperationParameters(setHeaderToken());
    }


    private ApiInfo apiBInfo() {

        return new ApiInfoBuilder()
                .title("白云乡服务API-1.5.1")
                .description("白云乡服务API")
                .contact(new Contact("东流心理咨询（广州）有限公司", "https://www.dongliuxinli.com/", "chenxiaodong@dongliuxinli.com"))
                .version("1.0")
                .build();
    }

    /**
     * 设置公共header参数
     * @return
     */
    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();
        //token
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("x-token").description("校验TOKEN").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        pars.add(tokenPar.build());
        /*//source
        ParameterBuilder sourcePar = new ParameterBuilder();
        sourcePar.name("x-source").description("用户来源").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        pars.add(sourcePar.build());*/
        return pars;
    }

}