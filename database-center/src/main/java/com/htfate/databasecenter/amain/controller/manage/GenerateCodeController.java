package com.htfate.databasecenter.amain.controller.manage;

import com.htfate.databasecenter.amain.service.manageCenter.GenerateService;
import com.htfate.utilcenter.util.UtilsReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author 杨海涛
 * @Date 2019-04-09 21:40
 * @Version 1.0.0
 **/
@RestController
@RequestMapping("generateCode")
public class GenerateCodeController {
    @Autowired
    private GenerateService generateService;

    @PostMapping("generate")
    public Object generate(@RequestBody Map<String, Object> params) {
        return UtilsReturnMsg.success(generateService.generate(params));
    }
}
