package com.htfate.pagecenter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {
    @Value("${custom.use-zuul:true}")
    private boolean useZuul;
    @Value("${spring.application.name:''}")
    private String contextPath;

    /**
     * 默认的页面
     */
    @RequestMapping()
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

    /**
     * 有权限的各个页面
     */
    @RequestMapping("page/{page}")
    public ModelAndView modelAndView(@PathVariable String page, String uid, String uname) {
        ModelAndView mv = new ModelAndView();
        if (useZuul) {
            mv.addObject("contextPath", "/" + contextPath);
        } else {
            mv.addObject("contextPath", "");
        }
        switch (page) {
            case "xxx":
                mv.setViewName("xxx");  // index
                break;
            default:
                mv.setViewName("index");  // 默认首页
        }
        return mv;
    }

    /**
     * 无权限的页面
     */
    @RequestMapping("pub/{page}")
    public ModelAndView modelAndView2(@PathVariable String page) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("userId", "61381c2a80d2496094d369b9a94b8b98");
        if (useZuul) {
            mv.addObject("contextPath", "/" + contextPath);
        } else {
            mv.addObject("contextPath", "");
        }

        switch (page) {
            // 首页
            case "model":
                mv.setViewName("database/model");
                break;
            default:
                mv.setViewName("index");
        }
        return mv;
    }
}
