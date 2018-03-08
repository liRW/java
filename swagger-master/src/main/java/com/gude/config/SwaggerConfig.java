package com.gude.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author Gude.
 * @Date 2017/4/8.
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("api1").
                forCodeGeneration(true).
                select().
                apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api1/.*"))

                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket api2() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("api2").
                forCodeGeneration(true).
                select().
                apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api2/.*"))

                .build()
                .apiInfo(apiInfo());
    }


    //api接口作者相关信息

    /******
     public static final Contact DEFAULT_CONTACT = new Contact("", "", ""); 联系方式
     public static final ApiInfo DEFAULT;
     private final String version;版本
     private final String title;标题
     private final String description;描述
     private final String termsOfServiceUrl;
     private final String license;
     private final String licenseUrl;
     private final Contact contact;
     * @return
     */


    private ApiInfo apiInfo() {
        Contact contact =
                new Contact("Mr. Li", "http://www.seebon.com/", "services@seebon.com");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .license("Apache License Version 2.0")
                .title("九炎系统")
                .description("接口文档")
                .contact(contact)
                .version("1.0")
                .title("title")
                .termsOfServiceUrl("--termsOfServiceUrl")
                .build();
        return apiInfo;
    }
}
