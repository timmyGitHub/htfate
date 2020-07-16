package com.htfate.databasecenter.amain.service.manageCenter.iml;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.htfate.databasecenter.amain.entity.vo.VOTableAndField;
import com.htfate.databasecenter.amain.mapper.TableInfoDao;
import com.htfate.databasecenter.amain.service.dofield.DOFieldService;
import com.htfate.databasecenter.amain.service.manageCenter.ModelManageService;
import com.htfate.databasecenter.amain.service.tableinfo.TableInfoService;
import com.htfate.databasecenter.amain.entity.po.DOField;
import com.htfate.databasecenter.amain.entity.po.TableInfo;
import com.htfate.databasecenter.amain.entity.po.modelManage.FormInfo;
import com.htfate.databasecenter.amain.entity.po.modelManage.JavaVariable;
import com.htfate.databasecenter.amain.entity.po.modelManage.ModelManageField;
import com.htfate.databasecenter.amain.mapper.DOFieldDao;
import com.htfate.databasecenter.amain.mapper.IMySqlDefineDao;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@SuppressWarnings("ALL")
@Service
@Transactional
@Log4j2
public class MdelManageIml implements ModelManageService {

    @Value("${spring.datasource.url}")
    String dbName;
    @Value("${spring.datasource.username}")
    String dbUsername;
    @Value("${spring.datasource.password}")
    String dbPassword;
    @Value("${custom.DATE_TYPE}")
    String dateType;

    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private TableInfoDao tableInfoDao;
    @Autowired
    private DOFieldService doFieldService;
    @Autowired
    private DOFieldDao doFieldDao;
    @Autowired
    private IMySqlDefineDao mySqlDao;

    @Override
    public Object test() {

        return null;
    }

    /**
     * 保存表信息
     *
     * @param flag   0-保存1-保存修改(也是tableid)
     * @param params 参数
     * @return 结果
     */
    @Override
    public String save(Long flag, Map<String, Object> params) {
        // 保存表信息
        String tableInfostr = params.get("tableInfo").toString();
        TableInfo table = JSON.parseObject(params.get("tableInfo").toString(), TableInfo.class);
        if (null == table)
            throw new MyException("表信息为空");
        table.setUuid(YHTUtils.getUUID());
        table.setCodeFlag(0);
        table.setCreateDate(new Timestamp(System.currentTimeMillis()));
        table.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // 校验
        tableValidator(table);
        tableInfoService.save(table);
        // 保存表字段信息
        // from 信息，数组
        List<FormInfo> formList = JSON.parseArray(params.get("formArr").toString(), FormInfo.class);
        if (null == formList)
            throw new MyException("form 信息为空");
        // java 信息，数组 (暂时不用)
        /*List javaList = YHTUtils.json2list(params.get("javaFieldArr"), JavaVariable.class);
        if (null == javaList)
            throw new MyException("java 信息为空");*/
        // 字段信息， 数组
        List<ModelManageField> fieldList = JSON.parseArray(params.get("fieldArr").toString(), ModelManageField.class);
        if (null == fieldList)
            throw new MyException("字段信息为空");
        // 保存所有字段信息
        List<DOField> doFieldList = new ArrayList<DOField>();

        DOField doField;
        FormInfo form;
        JavaVariable java;
        ModelManageField field;
        int display = 1;
        for (int i = 0; i < fieldList.size(); i++, display++) {
            form = formList.get(i);
//            java = (JavaVariable) javaList.get(i);
//            javaValidator(java);
            field = fieldList.get(i);
            fieldValidator(field);
            doField = new DOField();
            doField.setUuid(YHTUtils.getUUID());
            doField.setTableId(table.getId());
            doField.setFieldKey(field.getFieldKey());
            doField.setFieldName(field.getFieldName());
            doField.setFieldType(field.getFieldType());
            doField.setFieldRemark(field.getFieldRemark());
//            doField.setJavaVariable(java.getPageJavaType());
//            doField.setJavaType(java.getPageJavaType());
//            doField.setDisplay(field.getFieldDisplay());
            doField.setDisplay(display);
            doField.setFormDefaultValue(form.getDefaultValue());
            doField.setFormValue(form.getPageFormValue());
            doField.setFormVerify(form.getPageFormVerify());
            doField.setFormPlaceholder(form.getPageFormPlaceholder());
            doField.setFormType(form.getPageFormType());
            doField.setCreateDate(new Timestamp(System.currentTimeMillis()));
            doField.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            doFieldList.add(doField);
        }
        if (doFieldList.size() > 0) {
            if (0 == flag) {
//                tableInfoService.save(table);
                doFieldService.saveBatch(doFieldList);
            } else {
                /*
                 * 将旧的数据 delflag 置为 2 待用（如果旧数据中有旧数据则先删除），
                 * 将新数据的 id 保存到旧数据，
                 * */
                // 旧数据
                TableInfo tableInfo = tableInfoService.getById(flag);
                if (null == tableInfo) {
                    throw new MyException("没有找到数据 " + flag);
                }
                // 如果还没有创建表则删除旧数据，保存新数据
                if (0 == tableInfo.getDbFlag()) {
                    mySqlDao.execute("DELETE a,b FROM table_info a LEFT JOIN dofield b ON a.id = b.table_id where a.id = '" + flag + "'");
//                    tableInfoService.save(table);
                    doFieldService.saveBatch(doFieldList);
                } else {
                    // 若创建了表则替换
                    // 若不为空则存在旧数据
                    if (YHTUtils.isNotEmpty(tableInfo.getParentId())) {
                        // 删除旧旧数据
                        mySqlDao.execute("DELETE a,b FROM table_info a LEFT JOIN dofield b ON a.id = b.table_id where a.id = '" + tableInfo.getParentId() + "'");
                        // 将旧数据置为 2
                        mySqlDao.execute("UPDATE table_info a,dofield b SET a.delflag = 2, b.delflag = 2 WHERE a.id = b.table_id and a.id = '" + flag + "'");
                        // 保存新数据
                        table.setDbFlag(1);
                        table.setCodeFlag(2);
                        table.setParentId(flag);
                        tableInfoService.saveOrUpdate(table);
                        doFieldService.saveBatch(doFieldList);
                    } else {
                        // 将旧数据置为 2
                        mySqlDao.execute("UPDATE table_info a,dofield b SET a.delflag = 2, b.delflag = 2 WHERE a.id = b.table_id and a.id = '" + flag + "'");
                        // 保存新数据,若数据库已创建，则需设置状态
                        if (0 != tableInfo.getDbFlag()) {
                            table.setDbFlag(1);
                            table.setCodeFlag(2);
                        }
                        table.setParentId(flag);
                        tableInfoService.saveOrUpdate(table);
                        doFieldService.saveBatch(doFieldList);
                    }
                }
            }
        }
        return "success";
    }

    /**
     * 删除表单
     *
     * @param ids tableIds
     * @return 结果
     */
    @Override
    public String deleteTable(String ids) {
        if (YHTUtils.isEmpty(ids)) {
            throw new MyException("tableId 为空！");
        }
        List<TableInfo> tableInfoList = tableInfoService.getObjByIds(ids);  // 删除前复制一份！
        StringBuilder tableNames = new StringBuilder();
        StringBuilder tableIds = new StringBuilder();
        StringBuilder parentIds = new StringBuilder();
        int length = tableInfoList.size();
        for (int i = 0; i < length; i++) {
            // 若没有创建数据库表则不需要删除
            if (0 != tableInfoList.get(i).getDbFlag()) {
                tableNames.append(tableInfoList.get(i).getTableName());
                if (i + 1 != length) {
                    tableNames.append(",");
                }
            }
            tableIds.append("'").append(tableInfoList.get(i).getId()).append("'");
            parentIds.append("'").append(tableInfoList.get(i).getParentId()).append("'");
            if (i + 1 != length) {
                tableIds.append(",");
                parentIds.append(",");
            }
        }
        if (parentIds.length() > 0) {
            tableIds.append(",").append(parentIds);
        }
        // 删除记录
        String delSql = "DELETE a,b FROM table_info a LEFT JOIN dofield b ON a.id = b.table_id where a.id in (" + tableIds.toString() + ")";
        mySqlDao.execute(delSql);
        // 删表
        if (0 < tableNames.length()) {
            mySqlDao.dropTable(tableNames.toString());
        }
        return "success";
    }

    /**
     * 查找表单
     *
     * @param params
     * @return 集合
     */
    @Override
    public List<TableInfo> search(Map<String, Object> params) {
        YHTUtils.setPage(params);
        List<TableInfo> tableInfos = new ArrayList<TableInfo>();
        List<TableInfo> tableInfoList = tableInfoService.getList(params);
        String[] dbTableArr = mySqlDao.dbTableArr();
        // 检查是否存在所需的表，校正！
        for (TableInfo tableInfo : tableInfoList) {
            for (int i = 0, len = dbTableArr.length; i < len; i++) {
                if (!tableInfo.getTableName().equalsIgnoreCase(dbTableArr[i])) {
                    if (len == i + 1) {
                        if (0 != tableInfo.getDbFlag()) {
                            tableInfo.setDbFlag(0);
                            tableInfos.add(tableInfo);
                        }
                    }
                } else {
                    break;
                }
            }
        }
        // 如果存在不同步的记录进行矫正
        if (tableInfos.size() > 0) {
            tableInfoService.saveOrUpdateBatch(tableInfos);
        }
        return tableInfoList;
    }

    /**
     * 根据 tableId 查找数据
     *
     * @param id tableId
     * @return 表和表字段信息
     */
    @Override
    public VOTableAndField formInfo(Long id) {
        Map map = new HashMap();
        TableInfo tableInfo = tableInfoService.getById(id);
        if (null == tableInfo)
            throw new MyException("没有此表的信息");
        map.put("tableId", tableInfo.getId().toString());
        List<DOField> doField = doFieldService.getList(map);
        if (null == tableInfo && null == doField)
            throw new MyException("没有此表的信息");
        FormInfo formInfo;
        ModelManageField modelManageField;
        List<FormInfo> formInfoList = new ArrayList<>();
        List<ModelManageField> modelManageFieldList = new ArrayList<>();
        for (DOField field : doField) {
            formInfo = new FormInfo();
            modelManageField = new ModelManageField();
            formInfo.setPageFormType(field.getFormType());
            formInfo.setDefaultValue(field.getFormDefaultValue());
            formInfo.setPageFormValue(field.getFormValue());
            formInfo.setPageFormVerify(field.getFormVerify());
            formInfo.setPageFormPlaceholder(field.getFormPlaceholder());
            modelManageField.setFieldKey(field.getFieldKey());
            modelManageField.setFieldName(field.getFieldName());
            modelManageField.setFieldType(field.getFieldType());
            modelManageField.setFieldRemark(field.getFieldRemark());
            modelManageField.setFieldDisplay(field.getDisplay());
            formInfoList.add(formInfo);
            modelManageFieldList.add(modelManageField);
        }
        VOTableAndField voTableAndField = new VOTableAndField();
        voTableAndField.setTableInfo(tableInfo);
        voTableAndField.setDbField(modelManageFieldList);
        voTableAndField.setPageField(formInfoList);
        return voTableAndField;
    }

    /**
     * 创建或同步数据库
     *
     * @param sign 0-创建1-同步
     * @param id   tableId
     * @return true or false
     */

    @Override
    public String create(int sign, Long id) {
        switch (sign) {
            case 0:
                createDB(id);
                break;
            case 1:
                syncDB(id);
                break;
            default:
                throw new MyException("No have sign!");
        }
        return "success";
    }

    /**
     * 数据库创建表
     *
     * @param id tableId
     */
    public void createDB(Long id) {
        // 连接数据库名
        String name = dbName.substring(dbName.lastIndexOf('/') + 1, dbName.indexOf('?'));
        Connection con = YHTUtils.openDB(dbName, name, dbUsername, dbPassword);
        PreparedStatement ps = null;
        try {
            // 表信息
            TableInfo table = tableInfoService.getById(id);
            if (null == table)
                throw new MyException("没有找到此表！");
            // 表字段信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tableId", Joiner.on(",").join(Lists.newArrayList(id)));
            List<DOField> doFieldList = doFieldService.getList(params);
            List<ModelManageField> list = new ArrayList<ModelManageField>();
            ModelManageField modelManageField;
            for (DOField doField : doFieldList) {
                modelManageField = new ModelManageField();
                modelManageField.setFieldName(doField.getFieldName());
                modelManageField.setFieldRemark(doField.getFieldRemark());
                modelManageField.setFieldType(doField.getFieldType());
                modelManageField.setFieldKey(doField.getFieldKey());
                list.add(modelManageField);
            }
            if (0 == list.size())
                throw new MyException("field 信息为空");
            // 删表操作
            // 先校验
            tableValidator(table);
            String dropSql = "DROP TABLE IF EXISTS " + table.getTableName();
            ps = con.prepareStatement(dropSql);
            ps.execute();
            // 建表操作
            StringBuilder sql = new StringBuilder();
            sql.append("create table ").append(table.getTableName()).append(" (");
            String pk = null;
            for (ModelManageField field : list) {
                // 校验
                fieldValidator(field);
                sql.append(field.getFieldName()).append(" ");
                if ("timestamp".equals(field.getFieldType()) || "datetime".equalsIgnoreCase(field.getFieldType())) {
                    if ("update".equalsIgnoreCase(field.getFieldName())
                            || "update_date".equalsIgnoreCase(field.getFieldName())
                            || "updateDate".equalsIgnoreCase(field.getFieldName())
                            || "update_time".equalsIgnoreCase(field.getFieldName())) {
                        sql.append("datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP");
                    } else {
                        sql.append("datetime DEFAULT NULL");
                    }
                } else if ("bigint".equalsIgnoreCase(field.getFieldType())) {
                    if ("id".equalsIgnoreCase(field.getFieldName())) {
                        sql.append("bigint UNSIGNED NOT NULL AUTO_INCREMENT ");
                    } else {
                        sql.append("bigint");
                    }
                } else {
                    sql.append(field.getFieldType());
                }
                if ("true".equals(field.getFieldKey())) {
                    pk = field.getFieldName();
                    sql.append(" comment '");
                } else {
                    sql.append(" comment '");
                }
                sql.append(field.getFieldRemark()).append("',");
            }
            if (null == pk) {
                throw new MyException("没有选择主键");
            }
            sql.append("PRIMARY KEY (").append(pk).append(") USING BTREE");
            sql.append(") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '");
            sql.append(table.getTableRemark());
            sql.append("' ROW_FORMAT = Dynamic");
            YHTUtils.log(sql.toString());
            ps = con.prepareStatement(sql.toString());
            ps.execute();
            // 设置状态，2-已同步
            table.setDbFlag(2);
            tableInfoService.saveOrUpdate(table);
        } catch (SQLException e) {
            throw new MyException(e.getMessage());
        } finally {
            YHTUtils.closeDB(con, ps);
        }
    }

    /**
     * 同步数据库
     *
     * @param id tableId
     */
    private void syncDB(Long id) {
        // 查找出新旧数据，之后进行比较，异同进行修改
        TableInfo newTableInfo = tableInfoService.getById(id);
        TableInfo oldTableInfo = tableInfoDao.getAdminObjById(newTableInfo.getParentId());

        if (YHTUtils.isEmpty(newTableInfo) || YHTUtils.isEmpty(oldTableInfo)) {
            throw new MyException("同步数据库失败！");
        }

        Map<String, Object> param = new HashMap();
        param.put("tableId", newTableInfo.getId().toString());
        List<DOField> newDOFieldList = doFieldService.getList(param);
        List<DOField> newDOFieldListTemp = new ArrayList<>(newDOFieldList);  // 备用
        param.put("tableId", oldTableInfo.getId().toString());
        List<DOField> oldDOFieldList = doFieldDao.getAdminList(param);

        // 进行数据比较
        // table 修改
        if (newTableInfo.getParentId().equals(oldTableInfo.getId())) {
            // 更改表名
            if (!newTableInfo.getTableName().equals(oldTableInfo.getTableName())) {
                mySqlDao.execute("RENAME " + oldTableInfo.getTableName() + " TO " + newTableInfo.getTableName());
            }
            // 更改表说明
            if (!newTableInfo.getTableRemark().equals(oldTableInfo.getTableRemark())) {
                mySqlDao.execute("ALTER TABLE " + newTableInfo.getTableName() + " COMMENT '" + newTableInfo.getTableRemark() + "'");
            }
        }
        /*
            字段的修改
         */
        List<String> dateTypeList = Arrays.asList(dateType.split(";"));
        StringBuilder alterAddSQL = new StringBuilder("ALTER TABLE ");
        StringBuilder alterDropSQL = new StringBuilder("ALTER TABLE ");
        StringBuilder alterChangeSQL = new StringBuilder("ALTER TABLE ");
        alterAddSQL.append(newTableInfo.getTableName()).append(" ");
        alterDropSQL.append(newTableInfo.getTableName()).append(" ");
        alterChangeSQL.append(newTableInfo.getTableName()).append(" ");
        int alterAddSQLLength = alterAddSQL.length();
        int alterDropSQLLength = alterDropSQL.length();
        int alterChangeSQLLength = alterChangeSQL.length();
        Iterator<DOField> oldIterator = oldDOFieldList.iterator();
        // 修改列
        int count = 0;
        while (oldIterator.hasNext()) {
            int i = 0;
            boolean bRight = false;
            DOField oldDOField = oldIterator.next();
            Iterator<DOField> newIterator = newDOFieldList.iterator();
            while (newIterator.hasNext()) {
                DOField doField = newIterator.next();
                if (oldDOField.getDisplay() == doField.getDisplay()) {
                    if (!("id".equalsIgnoreCase(doField.getFieldName()))) {
                        if (count != 0) {
                            alterChangeSQL.append(", ");
                        }
                        alterChangeSQL.append("CHANGE ").append(oldDOFieldList.get(i).getFieldName()).append(" ");
                        alterChangeSQL.append(doField.getFieldName()).append(" ");
                        if (dateTypeList.contains(doField.getFieldType())) {
                            alterChangeSQL.append("datetime(0) NULL DEFAULT NULL ");
                        } else {
                            alterChangeSQL.append(doField.getFieldType()).append(" ");
                        }
                        alterChangeSQL.append("COMMENT '").append(doField.getFieldRemark()).append("'");
                        ++count;
                    }
                    newIterator.remove();
                    bRight = true;
                }
                ++i;
            }
            if (bRight) {
                oldIterator.remove();
            }
        }
        // 添加列
        for (int i = 0; i < newDOFieldList.size(); i++) {
            // 判断是否有重名
            count = 0;
            for (DOField doField : newDOFieldListTemp) {
                if (newDOFieldList.get(i).getFieldName().equals(doField.getFieldName())) {
                    if (count >= 2) {
                        throw new MyException("错误：" + doField.getFieldName() + " 字段重名!");
                    }
                    count++;
                }
            }
            // 添加列
            if (i != 0 && i + 2 != newDOFieldList.size()) {
                alterAddSQL.append(", ");
            }
            alterAddSQL.append("ADD ").append(newDOFieldList.get(i).getFieldName()).append(" ");
            if (dateTypeList.contains(newDOFieldList.get(i).getFieldType())) {
                alterAddSQL.append("timestamp(0) NULL DEFAULT NULL ");
            } else {
                alterAddSQL.append(newDOFieldList.get(i).getFieldType()).append(" ");
            }
            alterAddSQL.append("COMMENT '").append(newDOFieldList.get(i).getFieldRemark()).append("'");
        }
        // 删除列
        for (int i = 0; i < oldDOFieldList.size(); i++) {
            if (i != 0 && i + 2 != oldDOFieldList.size()) {
                alterDropSQL.append(", ");
            }
            alterDropSQL.append("DROP ").append(oldDOFieldList.get(i).getFieldName());
        }
        /* 执行SQL */
        if (alterChangeSQLLength != alterChangeSQL.length()) {
            mySqlDao.execute(alterChangeSQL.toString());
        }
        if (alterAddSQLLength != alterAddSQL.length()) {
            mySqlDao.execute(alterAddSQL.toString());
        }
        if (alterDropSQLLength != alterDropSQL.length()) {
            mySqlDao.execute(alterDropSQL.toString());
        }
        mySqlDao.execute("update table_info set db_flag = 2 where id = '" + id + "'");
    }

    /**
     * 表信息验证
     *
     * @param table 表信息
     */
    private void tableValidator(TableInfo table) {
        if (!YHTUtils.isEN(table.getTableName())) {
            throw new MyException("表名得是字母的");
        }
        if (!YHTUtils.isEN(table.getClazzName())) {
            throw new MyException("类名得是字母的");
        }
    }

    /**
     * 字段信息验证
     *
     * @param field 字段信息
     */
    private void fieldValidator(ModelManageField field) {
        if (!YHTUtils.isEN(field.getFieldName())) {
            throw new MyException("字段名得是字母的");
        }
    }

    /**
     * java 校验
     *
     * @param javaVariable
     */
    private void javaValidator(JavaVariable javaVariable) {
        if (!YHTUtils.isEN(javaVariable.getPageJavaVariable())) {
            throw new MyException("java 变量得是字母的");
        }
    }

}
