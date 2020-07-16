package com.htfate.oauth2center.exception;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.htfate.oauth2center.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
//    @Autowired
//    private TokenStore tokenStore;
    @Autowired
    private RedisService redisService;

//    @Autowired
//    @Qualifier("consumerTokenServices")
//    private ConsumerTokenServices tokenServices;

    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";

    private String prefix = "auth-token:";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JSONObject o = new JSONObject();
        String accessToken = request.getParameter("access_token");
        if(Strings.isNullOrEmpty(accessToken)){
            o.put("code",1);
            o.put("msg","access_token is null");
            o.put("success", false);
        } else{
//            tokenServices.revokeToken(accessToken);
//            redisService.del(prefix + ACCESS + accessToken);
//            redisService.del(prefix + AUTH + accessToken);
//            redisService.del(prefix + ACCESS_TO_REFRESH + accessToken);
//            redisService.del(prefix + REFRESH + refreshToken);
//            redisService.del(prefix + REFRESH_TO_ACCESS + refreshToken);
//            redisService.del(prefix + REFRESH_AUTH + refreshToken);
            o.put("code", 0);
            o.put("success", true);
            o.put("msg", "已退出");
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,X-Pagination");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.write(o.toString());
        writer.flush();
    }
}
