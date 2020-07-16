package com.htfate.databasecenter;

import com.htfate.utilcenter.entity.pojo.GenerateDataSource;
import com.htfate.utilcenter.entity.pojo.GenerateGlobal;
import com.htfate.utilcenter.entity.pojo.GeneratePackage;
import com.htfate.utilcenter.util.GenerateCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;

@Log4j2
public class TestMybatisGenerateMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(new BCryptPasswordEncoder().encode("timmy"));
//        generateCode();
    }

    private static void test(Integer is) {
        is++;
    }

    private static void generateCode() {
        GenerateGlobal global = new GenerateGlobal()
                .setAuthor("timmy")
                .setFileOverride(true)
                .setSwagger2(true)
                .setParentModuleName("generate-code");

        GenerateDataSource dataSource = new GenerateDataSource()
                .setUrl("jdbc:mysql://localhost:3306/htfate?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("htfate.com")
                .setTableName("mei_user");

        GeneratePackage generatePackage = new GeneratePackage()
                .setModuleName("user")
                .setParentName("com.htfate.usercenter");

        GenerateCode.execute(global, dataSource, generatePackage);
    }
}
