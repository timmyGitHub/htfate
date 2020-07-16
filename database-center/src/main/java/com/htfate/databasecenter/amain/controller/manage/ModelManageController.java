package com.htfate.databasecenter.amain.controller.manage;

import com.htfate.databasecenter.amain.service.manageCenter.ModelManageService;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("modelManage")
public class ModelManageController {
    @Autowired
    private ModelManageService modelManage;

    /**
     * 创建数据库表
     *
     * @param sign    0-创建1-同步
     * @param tableId 参数
     * @return 结果
     */
    @RequestMapping("create/{sign}/{tableId}")
    public Object create(@PathVariable int sign, @PathVariable Long tableId) {
        return UtilsReturnMsg.success(modelManage.create(sign, tableId));
    }

    /**
     * 保存表单信息
     *
     * @param flag   0-保存1-保存修改(1 是 tableId)
     * @param params 参数
     * @return 结果
     */
    @PostMapping("save/{flag}")
    public Object save(@PathVariable Long flag, @RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(modelManage.save(flag, params));
    }

    /**
     * 删除表单，可以删除多个，逗号隔开
     *
     * @param tableIds tableIds
     * @return 结果
     */
    @DeleteMapping("delete/{tableIds}")
    public Object deleteTable(@PathVariable String tableIds) {
        System.out.println(tableIds);
        return UtilsReturnMsg.success(modelManage.deleteTable(tableIds));
    }

    /**
     * 查找表单
     *
     * @param params 条件查询
     * @return 结果
     */
    @PostMapping("search")
    public Object search(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(modelManage.search(params));
    }

    /**
     * 根据 tableId 查找字段信息
     *
     * @param id tableId
     * @return 字段数据
     */
    @GetMapping("form/{id}")
    public Object form(@PathVariable Long id) {
        return UtilsReturnMsg.success(modelManage.formInfo(id));
    }

    /**
     * 测试
     * */
    @GetMapping("test")
    public Object test() {
        return UtilsReturnMsg.success(modelManage.test());
    }
}
