package com.htfate.app.aop;

import com.google.common.collect.Lists;
import com.htfate.app.config.bean.IAuthenticationInfo;
import com.htfate.app.feign.IFeign;
import com.htfate.utilcenter.util.YHTUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Log4j2
public class AopAspect {
    @Autowired
    private IAuthenticationInfo authenticationInfo;
    @Autowired
    private IFeign feign;

    @Pointcut("execution(public * com.htfate.app.*.controller.*.*(..))")
    public void LogAspect() {
    }

    @Before("LogAspect()")
    public void doBefore(JoinPoint joinPoint) {
        // 将一些信息写入本地线程
        writeThreadLocal();
        // 相关信息
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------(");
        sb.append(joinPoint.getSignature().getDeclaringTypeName());
        sb.append(")----------------------------\r\n");
        // 用户
        Map threadLocal = (Map) YHTUtils.getThreadLocal();
        sb.append("\tAccountId: ").append(threadLocal.get("id")).append("\r\n");
        sb.append("\tAccount: ").append(threadLocal.get("account")).append("\r\n");
        sb.append("\tUsername: ").append(threadLocal.get("username")).append("\r\n");
        // 请求方法
        sb.append("\tMethod: ")
                .append(joinPoint.getSignature().getName());
        // 请求参数
        Object[] paramsO = joinPoint.getArgs();
        Object o;
        sb.append(" (");
        for (int i = 0, len = paramsO.length; i < len; i++) {
            o = paramsO[i];
            sb.append(o.getClass().getSimpleName()).append(" ");
            sb.append(o.toString());
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(")\r\n");
        threadLocal.put("log", sb.toString());
    }

    @After("LogAspect()")
    public void doAfter() {

    }

    @AfterReturning(returning = "o", pointcut = "LogAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object o) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Map threadLocal = (Map) YHTUtils.getThreadLocal();
        Long time = (Long) threadLocal.get("time");
        String logStr = threadLocal.get("log").toString() +
                "\treturn: " + o.toString() + "\r\n" +
                "\ttime: " + (System.currentTimeMillis() - time) + "ms\r\n" +
                "\torigin: " + request.getHeader(HttpHeaders.USER_AGENT) + "\r\n" +
                "----------------------------(" +
                joinPoint.getSignature().getDeclaringTypeName() +
                ")----------------------------\r\n";

        log.info(logStr);
        // 移除本地线程
        YHTUtils.removeThreadLocal();
    }

    @AfterThrowing("LogAspect()")
    public void deAfterThrowing() {
    }

    @Around("LogAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    private void writeThreadLocal() {
        Authentication authentication = authenticationInfo.getAuthentication();
        OAuth2AuthenticationDetails details= (OAuth2AuthenticationDetails) authentication.getDetails();
        Object principal = authentication.getPrincipal();
        Map<String, Object> currentUserInfo = new HashMap<>();
        currentUserInfo.put("access_token", details.getTokenValue());
        if ("anonymousUser".equals(principal)) {
            currentUserInfo.put("id", null);
            currentUserInfo.put("username", "anonymousUser");
        } else {
            Map<String, Object> params = new HashMap<>();
            String sql = "SELECT * FROM tb_user WHERE delflag=0 AND account=?";

            params.put("sql", sql);
            params.put("params", Lists.newArrayList(principal.toString()));
            Map res = (Map) feign.executeSqlOne(params);
            YHTUtils.checkRes(res);
            Map user = (Map) res.get("data");

            currentUserInfo.put("id", user.get("id"));
            currentUserInfo.put("account", user.get("account"));
            currentUserInfo.put("username", user.get("username"));
        }

        currentUserInfo.put("time", System.currentTimeMillis());

        YHTUtils.setThreadLocal(currentUserInfo);
    }
}
