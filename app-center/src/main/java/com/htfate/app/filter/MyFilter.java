package com.htfate.app.filter;

import com.google.common.collect.Lists;
import com.htfate.app.config.bean.AuthenticationInfo;
import com.htfate.app.feign.IFeign;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyFilter implements Filter {
    @Autowired
    private AuthenticationInfo authenticationInfo;
    @Autowired
    private IFeign feign;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将一些信息写入本地线程
        Authentication authentication = authenticationInfo.getAuthentication();
        Object principal = authentication.getPrincipal();
        Map<String, Object> currentUserInfo = new HashMap<>();
        if ("anonymousUser".equals(principal)) {
            currentUserInfo.put("id",null);
            currentUserInfo.put("username","anonymousUser");
        } else {
            Map<String,Object> params = new HashMap<>();
            String sql = "SELECT * FROM tb_user WHERE delflag=0 AND account=?";

            params.put("sql", sql);
            params.put("params", Lists.newArrayList(principal));
            Map res = (Map) feign.executeSqlOne(params);
            YHTUtils.checkRes(res);
            Map user = (Map) res.get("data");

            currentUserInfo.put("id", user.get("id"));
            currentUserInfo.put("account", user.get("account"));
            currentUserInfo.put("username", user.get("username"));
        }

        YHTUtils.setThreadLocal(currentUserInfo);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
