package com.htfate.oauth2center;

import com.htfate.utilcenter.entity.pojo.GenerateDataSource;
import com.htfate.utilcenter.entity.pojo.GenerateGlobal;
import com.htfate.utilcenter.entity.pojo.GeneratePackage;
import com.htfate.utilcenter.util.GenerateCode;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TestMybatisGenerateMain {

    public static void main(String[] args) throws UnsupportedEncodingException {

//        System.out.println(YHTUtils.getUUID());
//        System.out.println(URLEncoder.encode("client_id=哈哈", StandardCharsets.UTF_8));
        System.out.println(new BCryptPasswordEncoder().encode("timmy"));
        System.out.println(YHTUtils.getUUID());
        generateCode();
    }

    private static void generateCode() {
        GenerateGlobal global = new GenerateGlobal()
                .setAuthor("timmy")
                .setFileOverride(true)
                .setSwagger2(false);

        GenerateDataSource dataSource = new GenerateDataSource()
                .setUrl("jdbc:mysql://localhost:3306/htfate?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("htfate.com")
                .setTableName("mei_auth");

        GeneratePackage generatePackage = new GeneratePackage()
                .setModuleName("auth")
                .setParentName("com.htfate.oauth2center");

        GenerateCode.execute(global, dataSource, generatePackage);
    }
}
