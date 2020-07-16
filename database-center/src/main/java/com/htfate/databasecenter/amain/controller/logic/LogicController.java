package com.htfate.databasecenter.amain.controller.logic;

import com.google.common.collect.Lists;
import com.htfate.databasecenter.amain.service.logic.ILogicService;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.ReturnMsg;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("logic")
@Api("操作数据的模块")
@ApiResponses({
        @ApiResponse(code = 0, message = "请求失败", response = ReturnMsg.class)
        , @ApiResponse(code = 1, message = "请求成功", response = ReturnMsg.class)
        , @ApiResponse(code = 101, message = "系统错误")
})
public class LogicController {
    @Autowired
    private ILogicService logicService;

    /**
     * 根据 sql 查询
     * */
    @ApiOperation("sql 查询")
    @PostMapping("executeSql")
    public Object executeSql(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(logicService.executeSql(params));
    }

    /**
     * 根据 sql 查询
     * */
    @ApiOperation("sql 一条查询")
    @PostMapping("executeSqlOne")
    public Object executeSqlOne(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(logicService.executeSqlOne(params));
    }
    /**
     * 根据 sql 查询总数
     * */
    @ApiOperation("sql count")
    @PostMapping("executeCount")
    public Object executeCount(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(logicService.executeCount(params));
    }

    @GetMapping("formValue/{tableName}/{id}/{parentName}")
    public Object formValue(@PathVariable String tableName, @PathVariable String id, @PathVariable String parentName) {
        return UtilsReturnMsg.success(logicService.formValue(tableName, id, parentName));
    }

    /**
     * 根据条件搜索
     * 如果有时间查询之间用英文逗号隔开
     * 例如：2020-05-19,2020-05-20
     *
     * @param tableName 表名
     * @param condition 条件
     * @return 结果
     */
    @GetMapping("search/{tableName}")
    @ApiOperation("根据条件搜索对象")
    public Object search(@PathVariable String tableName, @RequestParam Map<String, Object> condition) {
        return UtilsReturnMsg.success(logicService.search(tableName, condition));
    }
    /**
     * 精确查找
     * */
    @GetMapping("exact/search/{tableName}")
    @ApiOperation("根据条件精确搜索对象")
    public Object exactSearch(@PathVariable String tableName, @RequestParam Map<String, Object> condition) {
        return UtilsReturnMsg.success(logicService.exactSearch(tableName, condition));
    }

    /**
     * 保存对象
     * */
    @PostMapping("save/{tableName}")
    @ApiOperation("保存对象")
    public Object save(@PathVariable String tableName, @RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(logicService.save(tableName, Lists.newArrayList(params)));
    }

    /**
     * 保存多个对象
     * */
    @PostMapping("saves/{tableName}")
    @ApiOperation("保存多个对象")
    public Object saves(@PathVariable String tableName, @RequestBody List<Map<String, Object>> params) {
        return UtilsReturnMsg.success(logicService.save(tableName, params));
    }

    /**
     * 获取数据
     * */
    @GetMapping("list/{tableName}")
    @ApiOperation("根据表名获取数据")
    public Object getList(@PathVariable String tableName, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
        return UtilsReturnMsg.success(logicService.getList(tableName, page, limit));
    }

    @ApiOperation("根据 username 获取信息")
    @GetMapping("getByUsername/{username}")
    public Object getByUsername(@PathVariable String username) {
        return UtilsReturnMsg.success(logicService.getByUsername(username));
    }

    /**
     * id 要查找数据的 id
     */
    @GetMapping("getById/{tableName}/{id}")
    @ApiOperation("根据 id 获取对象")
    public Object getById(@PathVariable String tableName, @PathVariable String id) {
        return UtilsReturnMsg.success(logicService.getById(tableName, id));
    }

    /**
     * id 要查找数据的多个 id, 逗号隔开
     */
    @GetMapping("getByIds/{tableName}/{ids}")
    @ApiOperation("根据 id 获取对象")
    public Object getByIds(@PathVariable String tableName, @PathVariable String ids) {
        return UtilsReturnMsg.success(logicService.getByIds(tableName, ids));
    }

    /**
     * 删除多条条数据
     *
     * @param tableName 表名
     * @param id        数据 id
     * @return 结果
     */
    @GetMapping("del/{tableName}")
    @ApiOperation("根据 id 删除对象")
    public Object delByIds(@PathVariable String tableName, @RequestParam String id) {
        List<String> idStr = Lists.newArrayList(id.split(","));
        List<Long> idList = Lists.newArrayList();
        try {
            for (String s : idStr) {
                idList.add(Long.parseLong(s));
            }
        } catch (NumberFormatException e) {
            throw new MyException("请使用 Long 类型");
        }
        return UtilsReturnMsg.success(logicService.delById(tableName, idList));
    }

    /**
     * 删除一条数据
     *
     * @param tableName 表名
     * @param id        数据 id
     * @return 结果
     */
    @GetMapping("del/{tableName}/{id}")
    @ApiOperation("根据 id 删除对象")
    public Object delById(@PathVariable String tableName, @PathVariable Long id) {
        return UtilsReturnMsg.success(logicService.delById(tableName, Lists.newArrayList(id)));
    }

    /**
     * 获取表头
     */
    @ApiOperation("获取表头")
    @GetMapping("title/{tableName}")
    public Object getTitle(@PathVariable String tableName) {
        return UtilsReturnMsg.success(logicService.getTitle(tableName));
    }

    /**
     * 执行 sql
     * */

}
