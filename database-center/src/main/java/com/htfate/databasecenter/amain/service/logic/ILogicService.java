package com.htfate.databasecenter.amain.service.logic;

import com.htfate.databasecenter.amain.entity.vo.TitleInfoVO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ILogicService {
    Long save(String tableName, List<Map<String, Object>> params);

    boolean delById(String tableName, List<Long> ids);

    LinkedHashMap<String, Object> getById(String tableName, String id);

    LinkedHashMap<String, Object> getByUsername(String username);

    List<LinkedHashMap<String, Object>> getByIds(String tableName, String ids);

    LinkedHashMap<String, Object> getList(String tableName, Integer page, Integer limit);

    LinkedHashMap<String, Object> search(String tableName, Map<String, Object> condition);

    LinkedHashMap<String, Object> exactSearch(String tableName, Map<String, Object> condition);

    List<LinkedHashMap<String, Object>> executeSql(Map<String, Object> params);

    LinkedHashMap<String, Object> executeSqlOne(Map<String, Object> params);

    Integer executeCount(Map<String,Object> params);

    List<TitleInfoVO> getTitle(String tableName);

    List<LinkedHashMap<String, Object>> formValue(String tableName, String id, String parentName);
}
