package com.java.ymjr.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }


    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("易铭金融后台管理系统API文档")
                .description("本文档描述了易铭金融后台管理系统的各个模块的接口的调用方式")
                .version("1.1")
                .contact(new Contact("Joker", "http://softeem.com", "admin@softeem.com"))
                .build();
    }


    @Bean
    public Docket webApiConfig(){
        List<Parameter> pars = new ArrayList<Parameter>();
        //参数构造器
        ParameterBuilder tokenPar = new ParameterBuilder();
        //构造类型为header的token参数
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build()
                .globalOperationParameters(pars);  //把pars设置成全局操作参数
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("易铭金融网站API文档")
                .description("本文档描述了易铭金融网站各个模块的接口的调用方式")
                .version("1.1")
                .contact(new Contact("Joker", "http://softeem.com", "admin@softeem.com"))
                .build();
    }
}
