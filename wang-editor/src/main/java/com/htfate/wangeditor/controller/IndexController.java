package com.htfate.wangeditor.controller;

import com.htfate.wangeditor.feign.ZuulCenterFeign;
import com.purerland.utilcenter.util.UtilsReturnMsg;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private ZuulCenterFeign zuulCenterFeign;

    @PostMapping("emoji")
    public Object index(@RequestBody Map<String,Object> params) {
        params.put("parse", EmojiParser.parseToAliases(params.get("msg").toString()));
        return UtilsReturnMsg.success(params);
    }

    @GetMapping("test/{id}")
    public Object test(@PathVariable String id) {
        return UtilsReturnMsg.success(zuulCenterFeign.test(id));
    }
}
