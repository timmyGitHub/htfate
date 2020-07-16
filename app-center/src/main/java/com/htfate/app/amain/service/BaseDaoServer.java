package com.htfate.app.amain.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.htfate.utilcenter.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

@Service
public class BaseDaoServer {
    @Autowired
    private DataSource dataSource;

    public void executeSql(String sql, List<Object> params) {


    }

    private List<Map<String, Object>> rsToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = Lists.newArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        Map<String, Object> rowData;
        while (rs.next()) {
            rowData = Maps.newHashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }
}
