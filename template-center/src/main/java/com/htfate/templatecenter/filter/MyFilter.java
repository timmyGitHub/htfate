package com.htfate.templatecenter.filter;

import com.google.common.collect.Lists;
import com.htfate.templatecenter.config.bean.AuthenticationInfo;
import com.htfate.templatecenter.feign.IFeign;
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

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
