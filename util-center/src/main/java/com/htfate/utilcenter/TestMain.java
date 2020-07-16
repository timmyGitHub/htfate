package com.htfate.utilcenter;


import com.htfate.utilcenter.entity.pojo.GenerateDataSource;
import com.htfate.utilcenter.entity.pojo.GenerateGlobal;
import com.htfate.utilcenter.entity.pojo.GeneratePackage;
import com.htfate.utilcenter.util.GenerateCode;
import com.htfate.utilcenter.util.YHTUtils;

public class TestMain {

    public static void main(String[] args) {
        System.out.println(YHTUtils.isEmpty(""));
        generateCode();
    }
    private static void generateCode() {
        GenerateGlobal global = new GenerateGlobal()
                .setAuthor("timmy")
                .setFileOverride(true)
                .setSwagger2(false)
                .setParentModuleName("oauth2-center");

        GenerateDataSource dataSource = new GenerateDataSource()
                .setUrl("jdbc:mysql://localhost:3306/purerland?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("timmy")
                .setTableName("tb_role");

        GeneratePackage generatePackage = new GeneratePackage()
                .setModuleName("role")
                .setParentName("com.purerland.oauth2center");

        GenerateCode.execute(global, dataSource, generatePackage);
    }

}
