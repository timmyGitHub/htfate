package com.htfate.app.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Describption 自定义返回信息
 * @Author 杨海涛
 * @Date 2020/3/25
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        String uri = httpServletRequest.getRequestURI();
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
//        resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,X-Pagination");
//        resp.addHeader("Access-Control-Allow-Headers", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "*");
//        resp.setHeader("Access-Control-Allow-Credentials", "true");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setContentType("application/json; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        o.put("success",true);
        o.put("uri", uri);
        if ("Full authentication is required to access this resource".equals(e.getMessage())) {
            o.put("code", 9);
            o.put("msg","access_token is null");
        } else if(e.getMessage().contains("Invalid access token")){
            o.put("code", 5);
            o.put("msg","token 无效");
        } else {
            o.put("code", 4);
            o.put("msg", e.getMessage());
        }
        writer.write(o.toString());
        writer.flush();
    }
}
