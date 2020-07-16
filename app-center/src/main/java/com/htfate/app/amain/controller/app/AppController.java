package com.htfate.app.amain.controller.app;

import com.htfate.utilcenter.util.UtilsReturnMsg;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("app")
public class AppController {

    @PostMapping("login")
    public Object login(@RequestBody Map params) {
        System.out.println(params.toString());
        params.put("access_token", "xxxx-yyyy");
        return UtilsReturnMsg.success(params);
    }
}
