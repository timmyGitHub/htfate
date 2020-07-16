package com.htfate.oauth2center.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface IBaseDao {

    @Select("${sql}")
    List<LinkedHashMap<String, Object>> exec(@Param("sql") String sql);

    @Update("${sql}")
    int updateExec(@Param("sql") String sql);

}
