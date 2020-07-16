package com.htfate.databasecenter.filter;

import com.htfate.utilcenter.util.YHTUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.sql.*;

@Log4j2
@Component
public class MyFilter implements Filter {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;


    @Override
    public void init(FilterConfig filterConfig) {
        createDB();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    /**
     * 项目第一次运行时创建数据库
     */
    private void createDB() {
        boolean bRight = true, bExistDofield = true, bExistTableinfo = true;
        int first = dbUrl.lastIndexOf("/");
        int second = dbUrl.indexOf("?");
        String dbName = dbUrl.substring(first + 1, second);     // 创建数据库的名字
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsm;

        // 创建数据库，若不存在
        try {
            log.info("\r\n---------------------------------------------------------------------------------------------------");
            log.info("\r\n正在校验数据库中......");
            con = YHTUtils.openDB(dbUrl.substring(0,first+1)+dbUrl.substring(second),"", username, password);
            // 获取数据库名，存入 list 中
            if (con == null) {
                log.error("\r连接 MySQL 失败，检查用户名和密码是否正确！");
                return;
            }
            ps = con.prepareStatement("SHOW DATABASES");
            rs = ps.executeQuery();
            rsm = rs.getMetaData();
            int row = rsm.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= row; i++) {
                    // 查找是否存在要创建的数据库
                    if (dbName.equalsIgnoreCase((String) rs.getObject(i))) {
                        bRight = false;
                        log.info("\r\n数据库已存在！");
                    }
                }
            }
            if (bRight) {
                log.info("\r\n数据库不存在，正在创建...");
                ps = con.prepareStatement("CREATE DATABASE IF NOT EXISTS "+ dbName +" DEFAULT CHARACTER " +
                        "SET utf8 DEFAULT COLLATE utf8_general_ci");
                ps.execute();
                log.info("\r\n数据库创建完成！");
            }
            YHTUtils.closeDB(con, ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            log.info("\r\n校验表中......");
            con = YHTUtils.openDB(dbUrl,dbName, username, password);
            if (con == null) {
                log.error("\r连接 MySQL 失败，检查用户名和密码是否正确！");
                return;
            }
            ps = con.prepareStatement("SHOW TABLES");
            rs = ps.executeQuery();
            rsm = rs.getMetaData();
            int row = rsm.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= row; i++) {
                    if ("dofield".equalsIgnoreCase((String) rs.getObject(i))) {
                        bExistDofield = false;
                        log.info("\r\ndofile 表已存在！");
                    }
                    if ("table_info".equalsIgnoreCase((String) rs.getObject(i))) {
                        bExistTableinfo = false;
                        log.info("\r\ntable_info 表已存在！");
                    }
                }
            }
            // 创建 dofield 表
            if (bExistDofield) {
                log.info("\r\ndofield 表不存在，正在创建。。。");
                ps = con.prepareStatement("DROP TABLE IF EXISTS `dofield`");
                ps.execute();
                ps = con.prepareStatement("CREATE TABLE `dofield`  (" +
                        "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id'," +
                        "  `uuid` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '唯一键'," +
                        "  `field_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '字段名'," +
                        "  `field_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '字段说明'," +
                        "  `field_type` varchar(12) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '字段类型'," +
                        "  `field_key` varchar(6) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '是否主键'," +
                        "  `java_variable` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'java 变量'," +
                        "  `java_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'java 变量类型'," +
                        "  `form_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'form 类型（默认值）'," +
                        "  `form_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'form 类型'," +
                        "  `form_verify` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'form 校验'," +
                        "  `form_default_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'form 默认值'," +
                        "  `form_placeholder` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'form'," +
                        "  `table_id` bigint NULL DEFAULT NULL COMMENT 'table_info 中的id'," +
                        "  `db_parent_id` bigint NULL DEFAULT NULL COMMENT 'db 上一个tableId'," +
                        "  `parent_id` bigint NULL DEFAULT NULL COMMENT '上一个tableId'," +
                        "  `display` int NULL DEFAULT NULL COMMENT '排序'," +
                        "  `delflag` int(3) NULL DEFAULT 0 COMMENT '0-未删除 1-删除'," +
                        "  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间'," +
                        "  `create_person` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '创建人'," +
                        "  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间'," +
                        "  `update_person` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '更新人'," +
                        "  PRIMARY KEY (`id`) USING BTREE" +
                        ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '这是一个保存模板管理中表单的信息' ROW_FORMAT = Dynamic");
                ps.execute();
                log.info("\r\ndofield 表创建完成！");
            }
            // 创建 table_info 表
            if (bExistTableinfo) {
                log.info("\r\ntable_info 表不存在，正在创建。。。");
                ps = con.prepareStatement("DROP TABLE IF EXISTS `table_info`");
                ps.execute();
                ps = con.prepareStatement("CREATE TABLE `table_info`  (" +
                        "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id'," +
                        "  `uuid` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '唯一键'," +
                        "  `table_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '表名'," +
                        "  `table_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '表说明'," +
                        "  `table_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '表类型'," +
                        "  `clazz_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'java 类名'," +
                        "  `db_parent_id` bigint NULL DEFAULT NULL COMMENT 'db 上一个tableId'," +
                        "  `parent_id` bigint NULL DEFAULT NULL COMMENT '上一个tableId'," +
                        "  `display` int(255) NULL DEFAULT NULL COMMENT '排序'," +
                        "  `db_flag` int(3) NULL DEFAULT 0 COMMENT '0-未创建1-未同步2-已同步'," +
                        "  `code_flag` int(3) NULL DEFAULT 0 COMMENT '0-未生成1-已生成'," +
                        "  `delflag` int(3) NULL DEFAULT 0 COMMENT '0-未删除 1-删除'," +
                        "  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间'," +
                        "  `create_person` varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '创建人'," +
                        "  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间'," +
                        "  `update_person` varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '更新人'," +
                        "  PRIMARY KEY (`id`) USING BTREE" +
                        ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '这是模板管理中的表单信息的表信息' ROW_FORMAT = Dynamic");
                ps.execute();
                log.info("\r\ntableinfo 表创建完成！");
            }

            log.info("\r\n表校验完成!");
            YHTUtils.closeDB(con, ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("\r\n数据库校验完毕！");
        log.info("\r\n---------------------------------------------------------------------------------------------YHT---");
    }
}
