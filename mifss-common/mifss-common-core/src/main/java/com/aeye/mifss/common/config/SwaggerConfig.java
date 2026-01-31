package com.aeye.mifss.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 公共配置
 * 通过配置文件自定义 API 文档标题和描述
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.title:MIFSS API}")
    private String title;

    @Value("${swagger.description:MIFSS API 文档}")
    private String description;

    @Value("${swagger.version:1.0}")
    private String version;

    @Value("${swagger.base-package:com.aeye.mifss}")
    private String basePackage;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(title)
                        .description(description)
                        .version(version)
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }
}
