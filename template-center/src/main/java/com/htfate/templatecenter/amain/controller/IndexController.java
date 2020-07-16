package com.htfate.templatecenter.amain.controller;

import com.htfate.templatecenter.amain.service.index.IIndexService;
import com.htfate.utilcenter.util.ReturnMsg;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class IndexController {
    @Autowired
    private IIndexService indexService;

    @GetMapping("test/{name}")
    @ApiOperation("controller 测试请求")
    public Object test(@RequestParam(required = false) Integer age, @PathVariable String name) {
        return UtilsReturnMsg.success("hello : " + name + ", Your years old: " + age);
    }

    @GetMapping("test")
    @ApiOperation("controller 测试请求")
    public Object test2(@RequestParam(required = false) Integer age) {
        System.out.println("method start");
        indexService.test("xxx");
        System.out.println("method end");
        return UtilsReturnMsg.success();
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
