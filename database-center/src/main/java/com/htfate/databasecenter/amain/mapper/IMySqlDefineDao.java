package com.htfate.databasecenter.amain.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 杨海涛
 * @version 1.0
 * @date 2019/2/1915:27
 */
@Repository
public interface IMySqlDefineDao {
    @Select("DROP TABLE IF EXISTS ${tableName}")
    void dropTable(@Param("tableName") String tableName);

    @Select("SHOW TABLES")
    String[] dbTableArr();

    @Select("${sql}")
    void execute(@Param("sql") String sql);
}
