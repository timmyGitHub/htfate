package com.htfate.oauth2center.exception;

import com.alibaba.fastjson.JSONObject;
import com.htfate.utilcenter.exception.MyAuthorizeException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class OverrideAuthorizeFailureException extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
//        super.onAuthenticationFailure(request, response, exception);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,X-Pagination");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("code", 4);
        o.put("success",true);
        if ("Bad credentials".equals(e.getMessage())) {
            o.put("msg","密码错误");
        } else {
            o.put("msg", e.getMessage());
        }
        writer.write(o.toString());
        writer.flush();
    }
}
