package com.htfate.databasecenter.utils;


import com.htfate.databasecenter.amain.entity.dto.CreateDBO;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;

/**
 * @Author 杨海涛
 * @Date 2019-04-02 22:14
 * @Version 1.0.0
 **/
public class CommonUtil {
    /**
     * 自动生成类的属性设置
     *
     * @param createDBO 自动生成类
     */
    public static void setCreateDBO(CreateDBO createDBO) {
        if (YHTUtils.isNotEmpty(createDBO.getPackageName())) {
            String src = "src/main/java/com/htframe/";
            String packageSrc = "com.htframe.";

            createDBO.setControllerPackage(packageSrc + "controller." + createDBO.getPackageName());
            createDBO.setServicePackage(packageSrc + "service." + createDBO.getPackageName());
            createDBO.setServiceImplPackage(packageSrc + "service." + createDBO.getPackageName() + ".impl");
            createDBO.setEntityPackage(packageSrc + "entity.po." + createDBO.getPackageName());
            createDBO.setDaoPackage(packageSrc + "dao." + createDBO.getPackageName());
            createDBO.setControllerPackageSt(src + "controller/" + createDBO.getPackageName());
            createDBO.setServicePackageSt(src + "service/" + createDBO.getPackageName());
            createDBO.setServiceImplPackageSt(src + "service/" + createDBO.getPackageName() + "/impl");
            createDBO.setDaoPackageSt(src + "dao/" + createDBO.getPackageName());
            createDBO.setEntityPackageSt(src + "entity/po/" + createDBO.getPackageName());
            createDBO.setXmlPackageSt("src/main/resources/mapper/" + createDBO.getPackageName());
            createDBO.setListPackageSt("src/main/webapp/WEB-INF/jsp/" + createDBO.getPackageName());
            createDBO.setTempSrc(src + "temp");
        } else {
            throw new MyException("自动生成类所需参数为空！");
        }
    }
}
