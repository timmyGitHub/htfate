package com.htfate.databasecenter.amain.mapper;


import com.htfate.databasecenter.amain.entity.vo.TitleInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface IBaseDao {
    List<String> getTableField(@Param("tableName") String tableName);


    List<LinkedHashMap<String,Object>> getList(@Param("sql") String sql);

    @Select("${sql}")
    int exec(@Param("sql") String sql);

    @Update("${sql}")
    int updateExec(@Param("sql") String sql);

    LinkedHashMap<String, Object> getById(@Param("list") List<String> fieldList,@Param("tableName") String tableName,@Param("id") Long id);

    LinkedHashMap<String, Object> getByUsername(@Param("username") String username);

    int delByIds(@Param("tableName") String tableName, @Param("ids") String ids);

    List<TitleInfoVO> getTitle(@Param("tableId") Long tableId);
}
