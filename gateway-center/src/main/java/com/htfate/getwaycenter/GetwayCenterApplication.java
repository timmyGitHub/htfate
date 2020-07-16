package com.htfate.getwaycenter;

import com.htfate.getwaycenter.config.ErrorPageReg;
import com.htfate.getwaycenter.filter.MyGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GetwayCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetwayCenterApplication.class, args);
    }
    /**
     * 错误页面设置
     * */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageReg();
    }
    @Bean
    public GlobalFilter customFilter() {
        return new MyGatewayFilter();
    }
}
