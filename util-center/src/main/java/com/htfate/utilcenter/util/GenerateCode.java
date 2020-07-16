package com.htfate.utilcenter.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.htfate.utilcenter.entity.pojo.GenerateDataSource;
import com.htfate.utilcenter.entity.pojo.GenerateGlobal;
import com.htfate.utilcenter.entity.pojo.GeneratePackage;
import com.htfate.utilcenter.exception.MyException;

import java.util.ArrayList;
import java.util.List;

public class GenerateCode {

    public static void execute(GenerateGlobal global, GenerateDataSource dataSource, GeneratePackage generatePackage) {
        // 数据校验
        checkDate(global, dataSource, generatePackage);
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 可能还需要 模块名 的名称
//        String artifactId = global.getParentModuleName();
//        String projectPath = System.getProperty("user.dir") + "/" + artifactId;
        String projectPath = global.getPath();
        gc.setOutputDir(projectPath + "/src/main/java/");
        if (YHTUtils.isNotEmpty(global.getAuthor())) {
                gc.setAuthor(global.getAuthor());
        }
        gc.setOpen(false);
        gc.setFileOverride(global.isFileOverride());
        gc.setSwagger2(global.isSwagger2()); // 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://localhost:3306/purerland?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8");
        dsc.setUrl(dataSource.getUrl());
        // dsc.setSchemaName("public");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setDriverName(dataSource.getDriverName());
//        dsc.setUsername("root");
        dsc.setUsername(dataSource.getUsername());
//        dsc.setPassword("timmy");
        dsc.setPassword(dataSource.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(generatePackage.getModuleName());
        pc.setParent(generatePackage.getParentName());
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(dataSource.getTableName().split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 数据校验
     */
    private static void checkDate(GenerateGlobal global, GenerateDataSource dataSource, GeneratePackage generatePackage) {
        if (YHTUtils.isEmpty(dataSource.getUrl())) {
            throw new MyException("数据源 url 不能为空");
        }
        if (YHTUtils.isEmpty(dataSource.getDriverName())) {
            throw new MyException("数据源 driverName 不能为空");
        }
        if (YHTUtils.isEmpty(dataSource.getUsername())) {
            throw new MyException("数据源 username 不能为空");
        }
        if (YHTUtils.isEmpty(dataSource.getPassword())) {
            throw new MyException("数据源 password 不能为空");
        }
        if (YHTUtils.isEmpty(dataSource.getTableName())) {
            throw new MyException("要生成表的名称不能为空");
        }
        if (YHTUtils.isEmpty(generatePackage.getModuleName())) {
            throw new MyException("模块名 不能为空");
        }
        if (YHTUtils.isEmpty(generatePackage.getParentName())) {
            throw new MyException("父模块名不能为空，例如：com.purerland.xxx");
        }
    }
}
