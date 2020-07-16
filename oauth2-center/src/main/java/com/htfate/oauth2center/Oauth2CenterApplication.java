package com.htfate.oauth2center;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@MapperScan("com.htfate.oauth2center.mapper")
public class Oauth2CenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2CenterApplication.class, args);
    }

    /*@Bean
    public FilterRegistrationBean filterDemo3Registration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(loginFilter());
        registration.addUrlPatterns("/*");
        registration.setName("loginFilter");
        registration.setOrder(-1);
        return registration;
    }

    @Bean
    public Filter loginFilter() {
        return new PreFilter();
    }*/
}
