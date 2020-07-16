package com.htfate.app;

import com.google.common.collect.Lists;
import com.htfate.utilcenter.entity.pojo.GenerateDataSource;
import com.htfate.utilcenter.entity.pojo.GenerateGlobal;
import com.htfate.utilcenter.entity.pojo.GeneratePackage;
import com.htfate.utilcenter.util.GenerateCode;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class TestMybatisGenerateMain {

    public static void main(String[] args) throws UnsupportedEncodingException {

//        generateCode();
        test();
    }
    public static void test(){
        System.out.println(new BCryptPasswordEncoder().encode("timmy"));
        System.out.println(YHTUtils.getBase64Encoder("htfate:htfate"));
    }

    private static void generateCode() {
        GenerateGlobal global = new GenerateGlobal()
                .setAuthor("timmy")
                .setFileOverride(true)
                .setSwagger2(false)
                .setParentModuleName("wang-editor");

        GenerateDataSource dataSource = new GenerateDataSource()
                .setUrl("jdbc:mysql://localhost:3306/purerland?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("timmy")
                .setTableName("tb_role");

        GeneratePackage generatePackage = new GeneratePackage()
                .setModuleName("role")
                .setParentName("com.purerland.wangeditor");

        GenerateCode.execute(global, dataSource, generatePackage);
    }
}
