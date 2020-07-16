package com.htfate.databasecenter.amain.controller;

import com.htfate.databasecenter.config.bean.AuthenticationInfo;
import com.htfate.utilcenter.util.ReturnMsg;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api("测试的 controller")
@RestController
@ApiResponses({
        @ApiResponse(code = 0, message = "请求失败", response = ReturnMsg.class)
        , @ApiResponse(code = 1, message = "请求成功", response = ReturnMsg.class)
        , @ApiResponse(code = 101, message = "系统错误")
})
@Log4j2
public class IndexController {
    @Autowired
    private AuthenticationInfo authenticationInfo;

    @GetMapping("test/{name}")
    @ApiOperation("controller 测试请求")
    public Object test(@RequestParam(required = false) Integer age, @PathVariable String name) {
        log.debug("这是一个 debug 测试");
        log.info("这是个一个 info 测试");
        return UtilsReturnMsg.success("hello : " + name + ", Your years old: " + age);
    }

    @GetMapping("test")
    @ApiOperation("controller 测试请求")
    public Object test2(@RequestParam(required = false) Integer age) {
        return UtilsReturnMsg.success(authenticationInfo.getAuthentication().getName());
    }

    @GetMapping("error/{code}")
    public Object error404(HttpServletResponse response, @PathVariable String code) {
        int i = 0;
        String errorMsg = "未知错误";
        switch (response.getStatus()) {
            case 404:
                i = 404;
                errorMsg = "请检查路径是否正确";
                break;
            case 500:
                i = 500;
                errorMsg = "真不好意思，内部错误";
                break;
        }
        return UtilsReturnMsg.error(i, errorMsg);
    }
}
