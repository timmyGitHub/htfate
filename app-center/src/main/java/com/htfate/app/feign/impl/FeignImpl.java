package com.htfate.app.feign.impl;

import com.htfate.app.feign.IFeign;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class FeignImpl implements IFeign {
    private static LinkedHashMap<String, Object> res = new LinkedHashMap<>() {{
        put("code", 6);
        put("msg", "feign 请求错误");
    }};

    @Override
    public Object executeSqlOne(Map<String, Object> params) {
        return res;
    }

    @Override
    public Object executeCount(Map<String, Object> params) {
        return res;
    }

    @Override
    public Object executeSql(Map<String, Object> params) {
        return res;
    }

    @Override
    public Object exactSearch(String tableName, Map<String, Object> condition) {
        return res;
    }

    @Override
    public Object getTitle(String tableName) {
        return res;
    }

    @Override
    public Object getDataList(String name, Integer page, Integer limit) {
        return res;
    }

    @Override
    public Object getDataById(String tableName, Long id) {
        return res;
    }

    @Override
    public Object searchData(String tableName, Map<String, Object> condition) {
        return res;
    }

    @Override
    public Object delById(String tableName, Long id) {
        return res;
    }

    @Override
    public Object save(String tableName, Map<String, Object> params) {
        return res;
    }
}
