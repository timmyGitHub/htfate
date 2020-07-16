package com.htfate.utilcenter.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

public class SwaggerDefaultConfig {
    /**
     * 该套 API 说明，包含作者、简介、版本、host、服务URL
     * @return
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("默认的配置")
                .contact(new Contact("Timmy","purerland.com","1274988642@qq.com"))
                .version("1.0")
                .termsOfServiceUrl("")
                .description("默认的描述")
                .build();
    }
}
