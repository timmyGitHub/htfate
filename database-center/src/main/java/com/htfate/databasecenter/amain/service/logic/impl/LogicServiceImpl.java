package com.htfate.databasecenter.amain.service.logic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.htfate.databasecenter.amain.entity.po.TableInfo;
import com.htfate.databasecenter.amain.entity.vo.TitleInfoVO;
import com.htfate.databasecenter.amain.mapper.IBaseDao;
import com.htfate.databasecenter.amain.service.logic.ILogicService;
import com.htfate.databasecenter.amain.service.tableinfo.TableInfoService;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Repository
@Log4j2
public class LogicServiceImpl implements ILogicService {
    @Autowired
    private IBaseDao dao;
    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private DataSource dataSource;

    @Override
    public List<LinkedHashMap<String, Object>> formValue(String tableName, String id, String parentName) {
        if (YHTUtils.isEmpty(tableName)) {
            throw new MyException("tableName is not null");
        }

        TableInfo tableInfo = getTable(tableName);

        String sql;
        if (YHTUtils.isNotEmpty(parentName)) {
            long id_;
            if (YHTUtils.isNotEmpty(id)) {
                try {
                    id_ = Long.parseLong(id);
                } catch (NumberFormatException e) {
                    throw new MyException("只支持 Long 类型 : " + id);
                }
                sql = "SELECT * FROM " + tableInfo.getTableName() + " WHERE delflag =0 AND " + parentName + "=(SELECT " + parentName + " FROM " + tableName + " WHERE delflag =0 AND id=" + id_ + ")";
            } else {
                sql = "select * from " + tableInfo.getTableName() + " where delflag=0";
            }
        } else {
            sql = "select * from " + tableInfo.getTableName() + " where delflag=0";
        }
        return dao.getList(sql);
    }

    @Override
    public LinkedHashMap<String, Object> search(String tableName, Map<String, Object> condition) {
        TableInfo tableInfo = getTable(tableName);
        List<String> fieldLists = getTableFieldList(tableInfo, null, false);
        Object pageO = condition.get("page");
        Object limitO = condition.get("limit");
        int page = 0,limit=10;
        if (YHTUtils.isNotEmpty(pageO)) {
            page = Integer.parseInt(pageO.toString());
        }
        if (YHTUtils.isNotEmpty(limitO)) {
            limit = Integer.parseInt(limitO.toString());
        }
        page = Math.max((page-1)*limit,0);
        limit = Math.min(limit, 1000);
        // 设置忽略的字段
        List<String> ignoreFieldList = Lists.newArrayList("display", "create_person", "update_person", "delflag");
        String sql = "select " + Joiner.on(",").join(getTableFieldList(tableInfo, ignoreFieldList, true)) + " from " + tableInfo.getTableName();
        String count = "select count(id) total from " + tableInfo.getTableName();

        List<String> likeList;
        List<Object> valueList;
        valueList = Lists.newArrayList();
        likeList = Lists.newArrayList("1=1");
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            if (YHTUtils.isNotEmpty(entry.getKey()) && fieldLists.contains(entry.getKey())) {
                if ("create_date".equalsIgnoreCase(entry.getKey()) || "update_date".equalsIgnoreCase(entry.getKey())) {
                    String[] dateArr = entry.getValue().toString().split(",");
                    if (2 == dateArr.length) {
                        likeList.add("(" + entry.getKey() + ">= ? or " + entry.getKey() + " <= ?)");
                        valueList.add(dateArr[0]);
                        valueList.add(dateArr[1]);
                    } else {
                        likeList.add(entry.getKey() + " LIKE ?");
                        valueList.add("%" + entry.getValue() + "%");
                    }
                } else if (YHTUtils.isEmpty(entry.getValue())) {
                    likeList.add(entry.getKey() + " IS NULL");
                } else {
                    likeList.add(entry.getKey() + " LIKE ?");
                    valueList.add("%" + entry.getValue() + "%");
                }

            }
        }

        sql += " where delflag=0 and " + Joiner.on(" and ").join(likeList) +
                " limit " + page + "," + limit;
        count += " where delflag=0 and " + Joiner.on(" and ").join(likeList);
        Connection c = null;
        PreparedStatement ps = null;
        LinkedHashMap<String, Object> result = Maps.newLinkedHashMap();

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(sql);
            setPS(ps, valueList);
            ResultSet rs = ps.executeQuery();
            result.put("data", rsToList(rs));

            ps = c.prepareStatement(count);
            setPS(ps, valueList);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.put("total", rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        } finally {
            YHTUtils.closeDB(c, ps);
        }

        return result;
    }

    @Override
    public LinkedHashMap<String, Object> exactSearch(String tableName, Map<String, Object> condition) {
        TableInfo tableInfo = getTable(tableName);
        List<String> fieldLists = getTableFieldList(tableInfo, null, false);
        Object pageO = condition.get("page");
        Object limitO = condition.get("limit");
        int page = 0,limit=10;
        if (YHTUtils.isNotEmpty(pageO)) {
            page = Integer.parseInt(pageO.toString());
        }
        if (YHTUtils.isNotEmpty(limitO)) {
            limit = Integer.parseInt(limitO.toString());
        }
        page = Math.max((page-1)*limit,0);
        limit = Math.min(limit, 200);
        // 设置忽略的字段
        List<String> ignoreFieldList = Lists.newArrayList("display", "create_person", "update_person", "delflag");
        String sql = "select " + Joiner.on(",").join(getTableFieldList(tableInfo, ignoreFieldList, true)) + " from " + tableInfo.getTableName();
        String count = "select count(id) total from " + tableInfo.getTableName();

        List<String> likeList;
        List<Object> valueList;
        valueList = Lists.newArrayList();
        likeList = Lists.newArrayList("1=1");
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            if (YHTUtils.isNotEmpty(entry.getKey()) && fieldLists.contains(entry.getKey())) {
                if ("create_date".equalsIgnoreCase(entry.getKey()) || "update_date".equalsIgnoreCase(entry.getKey())) {
                    String[] dateArr = entry.getValue().toString().split(",");
                    if (2 == dateArr.length) {
                        likeList.add("(" + entry.getKey() + ">= ? or " + entry.getKey() + " <= ?)");
                        valueList.add(dateArr[0]);
                        valueList.add(dateArr[1]);
                    } else {
                        likeList.add(entry.getKey() + " = ?");
                        valueList.add(entry.getValue());
                    }
                } else if (YHTUtils.isEmpty(entry.getValue())) {
                    likeList.add(entry.getKey() + " IS NULL");
                } else {
                    likeList.add(entry.getKey() + " = ?");
                    valueList.add(entry.getValue());
                }

            }
        }
        //
        LinkedHashMap<String, Object> result = Maps.newLinkedHashMap();
        if (1 == likeList.size()) {
            result.put("data", Lists.newArrayList());
            result.put("total", 0);
            return result;
        }

        sql += " where delflag=0 and " + Joiner.on(" and ").join(likeList) +
                " limit " + page + "," + limit;
        count += " where delflag=0 and " + Joiner.on(" and ").join(likeList);
        log.debug(sql);
        log.debug(count);
        Connection c = null;
        PreparedStatement ps = null;


        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(sql);
            setPS(ps, valueList);
            ResultSet rs = ps.executeQuery();
            result.put("data", rsToList(rs));

            ps = c.prepareStatement(count);
            setPS(ps, valueList);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.put("total", rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        } finally {
            YHTUtils.closeDB(c, ps);
        }

        return result;
    }

    @Override
    public List<LinkedHashMap<String, Object>> executeSql(Map<String,Object> params) {
        YHTUtils.checkParams(params, Lists.newArrayList("sql","params"));
        String sql = params.get("sql").toString();
        List param = (List) params.get("params");

        Connection con;
        PreparedStatement ps;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            setPS(ps, param);
            ResultSet rs = ps.executeQuery();
            return rsToList(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MyException(throwables.getMessage());
        }
    }

    @Override
    public LinkedHashMap<String, Object> executeSqlOne(Map<String, Object> params) {
        YHTUtils.checkParams(params, Lists.newArrayList("sql","params"));
        String sql = params.get("sql").toString();
        List param = (List) params.get("params");
        Connection con;
        PreparedStatement ps;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            setPS(ps, param);
            ResultSet rs = ps.executeQuery();
            List<LinkedHashMap<String,Object>> resList = rsToList(rs);
            if (resList.size() == 1) {
                return resList.get(0);
            } else if (resList.size() == 0) {
                return Maps.newLinkedHashMap();
            } else {
                throw new MyException("找到多条数据，请检查");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MyException(throwables.getMessage());
        }
    }

    @Override
    public Integer executeCount(Map<String, Object> params) {
        YHTUtils.checkParams(params, Lists.newArrayList("sql","params"));
        String sql = params.get("sql").toString();
        List param = (List) params.get("params");
        Connection con;
        PreparedStatement ps;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            setPS(ps, param);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = rs.getInt("total");
            }
            return total;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MyException(throwables.getMessage());
        }
    }

    private List<LinkedHashMap<String, Object>> rsToList(ResultSet rs) throws SQLException {
        List<LinkedHashMap<String, Object>> list = Lists.newArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        LinkedHashMap<String, Object> rowData;
        while (rs.next()) {
            rowData = Maps.newLinkedHashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }

    @Transactional
    @Override
    public Long save(String tableName, List<Map<String, Object>> params) {
        TableInfo tableInfo = getTable(tableName);
        List<String> fieldList = getTableFieldList(tableInfo, null, false);
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Long id_ = null;
        try {
            List<String> keyList, commaList;
            List<Object> valueList;
            for (Map<String, Object> param : params) {
                keyList = Lists.newArrayList();
                valueList = Lists.newArrayList();
                commaList = Lists.newArrayList();
                boolean isUpdate = false, isFirst = false;
                Object id = param.get("id");
                if (YHTUtils.isNotEmpty(id)) {
                    isUpdate = true;
                }
                if (!isUpdate) {
                    setDefaultValue(fieldList, param);
                }

                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    if (YHTUtils.isNotEmpty(entry.getKey()) && YHTUtils.isNotEmpty(entry.getValue()) && fieldList.contains(entry.getKey()) && !"id".equalsIgnoreCase(entry.getKey())) {
                        if (isUpdate) {
                            keyList.add(entry.getKey() + "=?");
                            valueList.add(entry.getValue());
                            if (!isFirst && fieldList.contains("update_date") && YHTUtils.isEmpty(param.get("update_date"))) {
                                keyList.add("update_date=?");
                                valueList.add(YHTUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
                                isFirst = true;
                            }
                        } else {
                            keyList.add(entry.getKey());
                            valueList.add(entry.getValue());
                            commaList.add("?");
                        }

                    }
                }


                String sql = "";
                if (isUpdate) {
                    if (keyList.size() == 0) {
                        throw new MyException("空数据无法更新");
                    }
                    sql = "UPDATE " + tableInfo.getTableName() +
                            " SET " + Joiner.on(",").join(keyList) +
                            " where id=?";
                } else {
                    if (keyList.size() == 0) {
                        throw new MyException("空数据无法添加");
                    }
                    sql = "INSERT INTO " +
                            tableInfo.getTableName() + "(" + Joiner.on(",").join(keyList) + ")" +
                            " VALUES(" + Joiner.on(",").join(commaList) + ")";
                }

                c = dataSource.getConnection();
                ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                setPS(ps, valueList);
                if (isUpdate) {
                    ps.setLong(keyList.size() + 1, Long.parseLong(id.toString()));
                    ps.executeUpdate();
                    id_ = Long.parseLong(id.toString());
                } else {
                    ps.executeUpdate();
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        id_ = rs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        } finally {
            YHTUtils.closeDB(c, ps);
        }
        return id_;
    }

    private void setPS(PreparedStatement ps, List<Object> valueList) {
        try {
            Object o;
            for (int i = 1; i <= valueList.size(); i++) {
                o = valueList.get(i-1);
                if (o instanceof String) {
                    ps.setString(i, o.toString());
                }else if (o instanceof Long) {
                    ps.setLong(i, (Long) o);
                } else if (o instanceof Integer) {
                    ps.setInt(i, (Integer) o);
                } else if (o instanceof Double) {
                    ps.setDouble(i, (Double) o);
                } else if (o instanceof Date) {
                    ps.setDate(i, (Date) o);
                } else if (o instanceof Time) {
                    ps.setTime(i, (Time) o);
                } else if (o instanceof Timestamp) {
                    ps.setTimestamp(i, (Timestamp) o);
                } else if (o instanceof Blob) {
                    ps.setBlob(i, (Blob) o);
                } else if(o instanceof Boolean){
                    ps.setBoolean(i, (Boolean) o);
                } else {
                    throw new MyException("无法识别的类型：" + o.getClass().getSimpleName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        }

    }

    private void setDefaultValue(List<String> fieldList, Map<String, Object> params) {
        if (fieldList.contains("uuid"))
            params.put("uuid", YHTUtils.getUUID());
        if (fieldList.contains("create_date"))
            params.put("create_date", YHTUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
        if (fieldList.contains("update_date"))
            params.put("update_date", YHTUtils.getNowDate("yyyy-MM-dd HH:mm:ss"));
        if (fieldList.contains("delflag"))
            params.put("delflag", 0);
    }

    @Override
    public List<TitleInfoVO> getTitle(String tableName) {
        return dao.getTitle(getTable(tableName).getId());
    }

    @Transactional
    @Override
    public boolean delById(String tableName, List<Long> ids) {
        TableInfo tableInfo = getTable(tableName);
        String sql = "UPDATE " + tableInfo.getTableName() + " SET delflag=1 WHERE id IN(" + Joiner.on(",").skipNulls().join(ids) + ")";
        return dao.updateExec(sql) >= 1;
    }

    @Override
    public LinkedHashMap<String, Object> getById(String tableName, String id) {

        if (YHTUtils.isEmpty(id)) {
            throw new MyException("id is not null");
        }
        long id_;
        try {
            id_ = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new MyException("只支持 Long 类型 : " + id);
        }
        TableInfo tableInfo = getTable(tableName);
// 设置忽略的字段

        return dao.getById(getTableFieldList(tableInfo, getIgnoreFieldList(), true), tableInfo.getTableName(), id_);
    }

    @Override
    public LinkedHashMap<String, Object> getByUsername(String username) {
        if (YHTUtils.isEmpty(username)) {
            throw new MyException("username is not null");
        }

        return dao.getByUsername(username);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getByIds(String tableName, String ids) {
        TableInfo tableInfo = getTable(tableName);
        if (null == tableInfo) {
            throw new MyException("没有此表：" + tableName);
        }
        if (YHTUtils.isEmpty(ids)) {
            throw new MyException("ids is not null");
        }
        String[] idsArr = ids.split(",");
        List<Long> idsList = Lists.newArrayList();
        idsList.add(0l);
        for (String s : idsArr) {
            try {
                idsList.add(Long.parseLong(s));
            } catch (NumberFormatException e) {
                throw new MyException("此 id 有错误：" + s);
            }
        }
        List<String> fieldList = getTableFieldList(tableInfo, getIgnoreFieldList(), true);
        String sql = "select " + Joiner.on(",").join(fieldList) + " from " + tableInfo.getTableName() + " where delflag = 0 and id in(" + Joiner.on(",").join(idsList) + ")";
        return dao.getList(sql);
    }

    @Override
    public LinkedHashMap<String, Object> getList(String tableName, Integer pageO, Integer limitO) {

        int page = 0,limit=10;
        if (YHTUtils.isNotEmpty(pageO)) {
            page = pageO;
        }
        if (YHTUtils.isNotEmpty(limitO)) {
            limit = limitO;
        }
        page = Math.max((page-1)*limit,0);
        limit = Math.min(limit, 200);
        TableInfo tableInfo = getTable(tableName);

        // 设置忽略的字段
        List<String> ignoreFieldList = Lists.newArrayList("display", "create_person", "update_person", "delflag");

        String sql = "select " + Joiner.on(",").join(getTableFieldList(tableInfo, ignoreFieldList, true)) +
                " from " + tableInfo.getTableName() +
                " where delflag=0 " +
                " limit " + page + "," + limit;
        LinkedHashMap<String, Object> res = Maps.newLinkedHashMap();
        res.put("data", dao.getList(sql));
        res.put("total", getCount(tableInfo.getTableName()));
        return res;
    }

    private int getCount(String tableName) {
        String sql = "select count(id) from " + tableName + " where delflag=0";
        return dao.exec(sql);
    }

    private TableInfo getTable(String tableName) {
        TableInfo tableInfo = tableInfoService.getOne(new QueryWrapper<TableInfo>().eq("delflag", 0).eq("table_name", tableName));
        if (null == tableInfo) {
            throw new MyException("不存在此表 " + tableName);
        }
        return tableInfo;
    }

    /**
     * 获取表字段
     *
     * @param tableInfo       表信息
     * @param ignoreFieldList 需要忽略的字段
     * @param isOrigin        true-日期需要 format(此字段不用于比较，如果需要比较需要设置 false)
     * @return 结果
     */
    private List<String> getTableFieldList(TableInfo tableInfo, List<String> ignoreFieldList, boolean isOrigin) {

        // 找出此表的字段
        List<String> fieldList = Lists.newArrayList(dao.getTableField(tableInfo.getTableName()));
        if (null == ignoreFieldList) {
            return fieldList;
        }
        // 过滤忽略的字段
        ListIterator<String> iterable = fieldList.listIterator();
        while (iterable.hasNext()) {
            String s2 = iterable.next();
            if ("create_date".equalsIgnoreCase(s2) && isOrigin) {
                iterable.set("DATE_FORMAT(create_date,'%Y-%m-%d %H:%i:%s') create_date");
            } else if ("update_date".equalsIgnoreCase(s2) && isOrigin) {
                iterable.set("DATE_FORMAT(update_date,'%Y-%m-%d %H:%i:%s') update_date");
            }
            for (String s : ignoreFieldList) {
                if (s2.equalsIgnoreCase(s)) {
                    iterable.remove();
                }
            }
        }
        if (null == fieldList || 0 == fieldList.size()) {
            throw new MyException("不存在此表字段 " + tableInfo.getTableName());
        }
        return fieldList;
    }

    private List<String> getIgnoreFieldList() {
        return Lists.newArrayList("display", "create_person", "update_person", "delflag");
    }
}
