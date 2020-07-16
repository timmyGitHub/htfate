package com.htfate.databasecenter.utils.auto;


import com.htfate.databasecenter.amain.entity.dto.CreateDBO;
import com.htfate.databasecenter.utils.CommonUtil;
import com.htfate.utilcenter.util.YHTUtils;

import java.io.File;

/**
 * 类描述：java开发自动生成action,service,serviceImpl,dao,daoImpl,jtml,entity,js等
 */
@SuppressWarnings("ALL")
public class AutoCreateClass {

    //注释 作者
    /*public static String author = "杨海涛";
    //注释 创建时间
    public static String date = YHTUtils.getNowDate();
    //注释  描述信息
    public static String description = "模板管理中的表信息";
    // 包名
    public static String model = "dofield";
    // 类名
    public static String entityClass = "DOField";
    //实体类的变量名称
    public static String lowerEntity = "dofield";
    //表名
    public static String tableName = "dofield";

    //引入的包的路径
    //生成controller包结构
    public static String controllerPackage = ";
    //生成service包结构
    public static String servicePackage = ;
    //生成serviceImpl结构
    public static String serviceImplPackage = ;
    //生成entity结构
    public static String eitityPackage = ;
    //生成dao结构
    public static String daoPackage = ;
    //生成类存放的路径
    //生成controller包结构
    public static String controllerPackageSt = ;
    //生成service包结构
    public static String servicePackageSt = ;
    //生成serviceImpl结构
    public static String serviceImplPackageSt = ;
    //生成dao结构
    public static String daoPackageSt = ;
    //生成entity结构
    public static String entityPackageSt = ;
    //生成xml结构
    public static String xmlPackageSt = ;
    //list.jsp结构
    public static String listPackageSt = "src/main/webapp/WEB-INF/jsp/" + model;
    // 模板路径
    private static String tempSrc = ;*/

    /**
     * 创建controller(通过一个模板 生成一个实体类)  创建其他的都一样了
     */
    public static void createControllerClass(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getControllerPackageSt()) + createDBO.getEntityName() + "Controller.java";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "controller.txt");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
//			}
        }
    }

    /**
     * service 创建
     */
    public static void createServiceClass(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getServicePackageSt()) + createDBO.getEntityName() + "Service.java";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "service.txt");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }

    /**
     * serviceImpl 创建
     */
    public static void createServiceImplClass(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getServiceImplPackageSt()) + createDBO.getEntityName() + "ServiceImpl.java";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "serviceImpl.txt");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }

    /**
     * dao创建
     */
    public static void createDaoClass(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getDaoPackageSt()) + createDBO.getEntityName() + "Dao.java";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "dao.txt");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }

    /**
     * 实体类创建
     */
    public static void entityClass(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getEntityPackageSt()) + createDBO.getEntityName() + ".java";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "entity.txt");
        //拿到表对应的属性和getSet
        String entityStr = GenEntity.entityStr(createDBO.getTableName(), "entity", "");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent + entityStr + "\r\n}", newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent + entityStr + "\r\n}", newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }

    /**
     * 实体xml
     */
    public static void xmlClass(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getXmlPackageSt()) + createDBO.getEntityVariableName() + "Mapper" + ".xml";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "xml.txt");
        //拿到表对应的属性和getSet
        String entityStr = GenEntity.entityStr(createDBO.getTableName(), "xml", createDBO.getEntityPackage() + "." + createDBO.getEntityName());
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent + entityStr + "\r\n</mapper>", newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent + entityStr + "\r\n</mapper>", newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }


    /**
     * 生成list.jsp页面
     */
    public static void listjsp(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getListPackageSt()) + createDBO.getEntityVariableName() + "/list.jsp";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "list.txt");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            System.err.println("实体类已经存在并将其覆盖");
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }

    /**
     * 生成saveOrUpdatePage.jsp页面
     */
    public static void savejsp(CreateDBO createDBO) throws Exception {
        String newClassName = getHomeDir(createDBO.getListPackageSt()) + createDBO.getEntityVariableName() + "/saveOrUpdatePage.jsp";
        //获取对应的模板
        String templateContent = YHTUtils.readFile(getHomeDir(createDBO.getTempSrc()) + "saveOrUpdatePage.txt");
        //判断目录是否存在 不存在就创建
        new File(newClassName).getParentFile().mkdirs();
        if (!YHTUtils.isExist(newClassName)) {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[创建实体类]" + newClassName + "成功了!!!!!");
        } else {
            buildClass(createDBO, templateContent, newClassName, createDBO.getEntityPackage());
            System.out.println("[覆盖实体类]" + newClassName + "成功了!!!!!");
        }
    }

    /**
     * 获取工程的目录
     *
     * @param path
     */
    public static String getHomeDir(String path) {
        if (!YHTUtils.isEmpty(path)) {
            return coversionSpecialCharacters(System.getProperty("user.dir")) + "/" + path + "/";
        } else {
            return System.getProperty("user.dir");
        }
    }

    /**
     * 转换路径中的斜杠
     */
    public static String coversionSpecialCharacters(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * 模板内容替换
     */
    public static void buildClass(CreateDBO createDBO, String tmplateContent, String newFilePath, String pkg) {
        tmplateContent = tmplateContent
                .replaceAll("\\[author\\]", createDBO.getAuthor())
                .replaceAll("\\[date\\]", createDBO.getDate())
                .replaceAll("\\[description\\]", createDBO.getDescription())
                .replaceAll("\\[entityClass\\]", createDBO.getEntityName())
                .replaceAll("\\[lowerEntity\\]", createDBO.getEntityVariableName())
                .replaceAll("\\[controllerPackage\\]", createDBO.getControllerPackage())
                .replaceAll("\\[servicePackage\\]", createDBO.getServicePackage())
                .replaceAll("\\[serviceImplPackage\\]", createDBO.getServiceImplPackage())
                .replaceAll("\\[daoPackage\\]", createDBO.getDaoPackage())
                .replaceAll("\\[model\\]", createDBO.getPackageName())
                .replaceAll("\\[entityPackage\\]", createDBO.getEntityPackage());
        YHTUtils.writerFileByLine(tmplateContent, newFilePath);
    }

    public static void generatorCode() {
        try {
            // 生成 tableInfo 自动类
            CreateDBO createDBO = new CreateDBO();
            createDBO.setAuthor("杨海涛");
            createDBO.setDate(YHTUtils.getNowDate());
            createDBO.setDescription("模版管理");
            createDBO.setPackageName("tableinfo");
            createDBO.setEntityName("TableInfo");
            createDBO.setEntityVariableName("tableInfo");
            createDBO.setTableName("table_info");
            CommonUtil.setCreateDBO(createDBO);

            createControllerClass(createDBO);   // controller 层
            createServiceClass(createDBO);      // service 层
            createServiceImplClass(createDBO);  // serviceImp 层
            createDaoClass(createDBO);          // dao 层
            entityClass(createDBO);             // 实体类
            //listjsp(createDBO);
            xmlClass(createDBO);                // mapper XML
            //savejsp(createDBO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generatorCode();
    }
}
