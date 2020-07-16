package com.htfate.databasecenter.amain.service.manageCenter.iml;

import com.alibaba.fastjson.JSON;
import com.htfate.databasecenter.amain.entity.dto.CreateDBO;
import com.htfate.databasecenter.amain.entity.dto.GenerateInfo;
import com.htfate.databasecenter.amain.service.manageCenter.GenerateService;
import com.htfate.databasecenter.utils.auto.AutoCreateClass;
import com.htfate.databasecenter.amain.mapper.IMySqlDefineDao;
import com.htfate.databasecenter.utils.CommonUtil;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author 杨海涛
 * @Date 2019-04-09 21:48
 * @Version 1.0.0
 **/
@Service
public class GenerateIml implements GenerateService {
    @Autowired
    private IMySqlDefineDao dao;
    /**
     * 生成代码
     *
     * @param params 参数
     * @return 结果
     */
    @Override
    public String generate(Map<String, Object> params) {
        List<GenerateInfo> generateList = JSON.parseArray(params.get("generateInfo").toString(), GenerateInfo.class);

        CreateDBO createDBO;
        for (GenerateInfo o : generateList) {
            // 设置状态
            String id = o.getTableId();
            if (YHTUtils.isEmpty(id)) {
                throw new MyException("id is null");
            }
            dao.execute("UPDATE table_info SET code_flag=1 WHERE id='"+id+"'");

            createDBO = new CreateDBO();
            String tableName = o.getTableName();
            String[] arr = tableName.split("_");
            String packageName = YHTUtils.arrayToString(arr, "", false);
            String entityName = YHTUtils.arrayToString(arr, "", true);
            createDBO.setTableName(tableName);
            createDBO.setAuthor(o.getTableAuthor());
            createDBO.setDescription(o.getTableRemark());
            createDBO.setDate(YHTUtils.getNowDate());
            createDBO.setPackageName(packageName);
            createDBO.setEntityName(entityName);
            createDBO.setEntityVariableName(packageName);
            CommonUtil.setCreateDBO(createDBO);

            try {
                if (o.getController()) {
                    AutoCreateClass.createControllerClass(createDBO);
                }
                if (o.getEntity()) {
                    AutoCreateClass.entityClass(createDBO);
                }
                if (o.getService()) {
                    AutoCreateClass.createServiceClass(createDBO);
                    AutoCreateClass.createServiceImplClass(createDBO);
                }
                if (o.getDao()) {
                    AutoCreateClass.createDaoClass(createDBO);
                    AutoCreateClass.xmlClass(createDBO);
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(e.getMessage());
            }
        }
        return "success";
    }
}
