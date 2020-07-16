package com.htfate.usercenter;

import com.google.common.base.Strings;
import com.htfate.utilcenter.entity.pojo.GenerateDataSource;
import com.htfate.utilcenter.entity.pojo.GenerateGlobal;
import com.htfate.utilcenter.entity.pojo.GeneratePackage;
import com.htfate.utilcenter.util.GenerateCode;

import java.io.UnsupportedEncodingException;

public class TestMybatisGenerateMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = null;
        System.out.println(Strings.isNullOrEmpty(str));
//        generateCode();
    }

    private static void generateCode() {
        GenerateGlobal global = new GenerateGlobal()
                .setAuthor("timmy")
                .setFileOverride(true)
                .setSwagger2(false)
                .setParentModuleName("wang-editor");

        GenerateDataSource dataSource = new GenerateDataSource()
                .setUrl("jdbc:mysql://localhost:3306/htfate?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("timmy")
                .setTableName("tb_role");

        GeneratePackage generatePackage = new GeneratePackage()
                .setModuleName("role")
                .setParentName("com.htfate.wangeditor");

        GenerateCode.execute(global, dataSource, generatePackage);
    }
}
