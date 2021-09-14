package com.bluewind.base.common.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxingyu01
 * @date 2021-09-14-19:53
 **/
public class SwaggerConfig extends Swagger2DocumentationConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {

        // 配置请求头，即在header里加上token，前后端分离时能用到
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("Authorization")
                .description("认证token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true) // 配置是否启用Swagger，如果是false，在浏览器将无法访问
                .globalOperationParameters(parameters)
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex("/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("bluewind-base接口文档") // 标题
                .contact(new Contact("liuxingyu01", "", "liuxingyu9725@foxmail.com")) // 作者
                .description("bluewind-base系统接口文档") // 简介
                .termsOfServiceUrl("无") // 服务条款url
                .version("1.0.0") // 版本
                .build();
    }
}
