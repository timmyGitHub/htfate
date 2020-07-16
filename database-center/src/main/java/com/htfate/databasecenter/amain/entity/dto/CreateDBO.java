package com.htfate.databasecenter.amain.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author 杨海涛
 * @Date 2019-04-02 21:25
 * @Version 1.0.0
 **/
@Getter
@Setter
@ToString
public class CreateDBO {
    private String author;                  // 开发者
    private String date;                    // 创建日期
    private String description;             // 类描述
    private String packageName;             // 包名
    private String entityName;              // 实体类名
    private String entityVariableName;      // 实体变量名
    private String tableName;               // 数据库表名
    private String controllerPackage;       // controller 结构
    private String servicePackage;          // 生成service 结构
    private String serviceImplPackage;      // 生成serviceImpl结构
    private String entityPackage;           // 生成entity结构
    private String daoPackage;              // 生成dao结构
    private String controllerPackageSt;     // 生成 controller 路径
    private String servicePackageSt;        // 生成 service 路径
    private String serviceImplPackageSt;    // 生成 serviceImpl 路径
    private String daoPackageSt;            // 生成 dao 路径
    private String entityPackageSt;         // 生成 entity 路径
    private String xmlPackageSt;            // 生成 xml 路径
    private String listPackageSt;           // list.jsp 路径
    private String tempSrc;                 // 模板路径
}
