package com.htfate.wangeditor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.purerland.wangeditor.dao")
@EnableHystrix
@EnableFeignClients
public class WangEditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangEditorApplication.class, args);
    }

}
