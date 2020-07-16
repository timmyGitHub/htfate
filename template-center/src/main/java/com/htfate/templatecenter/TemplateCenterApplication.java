package com.htfate.templatecenter;

import com.htfate.templatecenter.config.ErrorPageReg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@EnableFeignClients
@SpringBootApplication
public class TemplateCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateCenterApplication.class, args);
    }

    /**
     * 错误页面设置
     * */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageReg();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
