package com.htfate.databasecenter;

import com.htfate.databasecenter.config.ErrorPageReg;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
@MapperScan("com.htfate.databasecenter.*.mapper")
public class DatabaseCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseCenterApplication.class, args);
    }

    /**
     * 错误页面设置
     * */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageReg();
    }
}
