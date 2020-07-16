package com.htfate.textcenter.controller;

import com.purerland.utilcenter.exception.MyAuthorizeException;
import com.purerland.utilcenter.exception.MyException;
import com.purerland.utilcenter.util.UtilsReturnMsg;
import com.purerland.utilcenter.util.YHTUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("text")
public class IndexController {
    @GetMapping
    public Object test(String name) {
        return UtilsReturnMsg.success("name: " +YHTUtils.getUUID());
    }

    @PostMapping("test")
    public Object test2(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(params);
    }
}
