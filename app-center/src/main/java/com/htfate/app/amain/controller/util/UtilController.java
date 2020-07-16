package com.htfate.app.amain.controller.util;

import com.alibaba.fastjson.JSONObject;
import com.htfate.app.amain.service.util.IUtilService;
import com.htfate.utilcenter.util.ReturnMsg;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api("测试的 controller")
@RestController
@RequestMapping("util")
@ApiResponses({
        @ApiResponse(code = 0, message = "请求失败", response = ReturnMsg.class)
        , @ApiResponse(code = 1, message = "请求成功", response = ReturnMsg.class)
        , @ApiResponse(code = 101, message = "系统错误")
})
public class UtilController {
    @Autowired
    private IUtilService utilService;

    @GetMapping("test/{name}")
    @ApiOperation("controller 测试请求")
    public Object test(@RequestParam(required = false) Integer age, @PathVariable String name) {
        return UtilsReturnMsg.success("hello : " + name + ", Your years old: " + age);
    }

    @GetMapping("test")
    @ApiOperation("controller 测试请求")
    public Object test2(@RequestParam(required = false) Integer age, @RequestParam String name) {
        return UtilsReturnMsg.success("hello : " + name + ", Your years old: " + age);
    }

    @ApiOperation("获取地理位置")
    @GetMapping("geoCoding")
    public Object reverseGeoCoding(@RequestParam String coords) {
        return UtilsReturnMsg.success(utilService.reverseGeoCoding(coords));
    }
}
