package com.htfate.usercenter.controller;

import com.google.common.base.Strings;
import com.htfate.usercenter.entity.vo.TokenInfoVO;
import com.htfate.usercenter.service.IUserService;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.ReturnMsg;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api("用户操作 Controller")
@ApiResponses({
        @ApiResponse(code = 0, message = "请求失败", response = ReturnMsg.class)
        , @ApiResponse(code = 1, message = "请求成功", response = ReturnMsg.class)
        , @ApiResponse(code = 101, message = "系统错误")
})
public class IndexController {
    @Autowired
    private IUserService userService;

    @GetMapping("test")
    @ApiOperation("controller 测试请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String", paramType = "query")
    })
    public Object test(String name) {
        return UtilsReturnMsg.success("hello : " + name);
    }

    @ApiOperation("退出登录")
    @GetMapping("exit")
    public Object logout(){
        return UtilsReturnMsg.success(userService.logout());
    }

    @PostMapping("login")
    @ApiOperation("获取 token")
    public Object login(@RequestBody TokenInfoVO tokenInfoVO) {
        return UtilsReturnMsg.success(userService.getToken(tokenInfoVO));
    }

    @GetMapping("refreshToken")
    @ApiOperation("刷新 token")
    public Object refreshToken(String access_token) {
        if (Strings.isNullOrEmpty(access_token)) {
            throw new MyException("access_token is null");
        }
        return UtilsReturnMsg.success(userService.refreshToken());
    }
}
