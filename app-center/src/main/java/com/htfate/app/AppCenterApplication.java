package com.htfate.app;

import com.htfate.app.config.ErrorPageReg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@SpringBootApplication
public class AppCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppCenterApplication.class, args);
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
        return new RestTemplate();
    }
}
