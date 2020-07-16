package com.htfate.wangeditor.controller;

import com.purerland.utilcenter.util.UtilsReturnMsg;
import com.purerland.utilcenter.util.YHTUtils;
import com.htfate.wangeditor.service.wang.WangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("wang")
public class WangEditorController {
    @Autowired
    private WangService wangService;

    @RequestMapping("/{test}")
    public String index (@PathVariable String test) {
        if (YHTUtils.isNotEmpty(test)) {
            return test;
        }
        return new Date().toString();
    }

    @PostMapping("save")
    public Object save(@RequestParam Map<String, Object> params) {
        return UtilsReturnMsg.success(wangService.save(params));
    }

    @PostMapping("savePaste")
    public Object savePaste(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(wangService.savePaste(params));
    }

    @GetMapping("listPaste/{userId}")
    public Object list (int pageIndex, int pageSize, @PathVariable String userId) {
        return UtilsReturnMsg.success(wangService.listPaste(userId,pageIndex, pageSize));
    }
    @GetMapping("listPaste")
    public Object list2 (int pageIndex, int pageSize, String userId) {
        return UtilsReturnMsg.success(wangService.listPaste(userId,pageIndex, pageSize));
    }
}
