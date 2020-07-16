package com.htfate.usercenter.feign;

import com.htfate.usercenter.feign.impl.FeignImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "gateway", fallback = FeignImpl.class)
public interface IFeign {
    @PostMapping("database/logic/executeCount")
    Object executeCount(@RequestBody Map<String, Object> params);

    @PostMapping("database/logic/executeSql")
    Object executeSql(@RequestBody Map<String, Object> params);

    @PostMapping("database/logic/executeSqlOne")
    Object executeSqlOne(@RequestBody Map<String, Object> params);

    @GetMapping("database/logic/title/{tableName}")
    Object getTitle(@PathVariable("tableName") String tableName);

    @GetMapping("database/logic/list/{tableName}")
    Object getDataList(@PathVariable("tableName") String name,@RequestParam(required = false,value = "page") Integer page, @RequestParam(required = false,value = "limit") Integer limit);

    @GetMapping("database/logic/getById/{tableName}/{id}")
    Object getDataById(@PathVariable("tableName") String tableName, @PathVariable("id") Long id);

    @GetMapping("database/logic/search/{tableName}")
    Object searchData(@PathVariable("tableName") String tableName, @RequestParam(required = false) Map<String, Object> condition);

    @PostMapping("database/logic/save/{tableName}")
    Object save(@PathVariable String tableName, @RequestBody Map<String, Object> params);

    /**
     * 删除一条数据
     *
     * @param tableName 表名
     * @param id        数据 id
     * @return 结果
     */
    @GetMapping("database/logic/del/{tableName}/{id}")
    Object delById(@PathVariable String tableName, @PathVariable Long id);

    /**
     * 精确查找
     * */
    @GetMapping("database/logic/exact/search/{tableName}")
    Object exactSearch(@PathVariable String tableName, @RequestParam Map<String, Object> condition);

    @GetMapping("oauth2/sys/logout")
    Object logout(@RequestParam("access_token") String access_token);
    @GetMapping("oauth2/exit/{access_token}")
    Object removeToken(@PathVariable String access_token);
}
