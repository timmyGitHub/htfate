package com.htfate.oauth2center.exception;

import com.google.common.collect.Maps;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * @Describption 自定义返回信息
 * @Author 杨海涛
 * @Date 2020/3/25
 */
public class MyAuthenticationEntryPoint implements WebResponseExceptionTranslator {
    private static Map<String, Object> res = Maps.newLinkedHashMap();

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        res.put("msg", e.getMessage());
        res.put("code", YHTUtils.CODE_OAUTH_ERROR);
        if (e instanceof InternalAuthenticationServiceException) {
            InternalAuthenticationServiceException serviceException = (InternalAuthenticationServiceException) e;
            res.put("msg", serviceException.getMessage());
            String caseStr = serviceException.getCause().toString();
            String exceptionName = caseStr.substring(caseStr.lastIndexOf(".") + 1, caseStr.indexOf(":"));
            // 请求超时
            if (exceptionName.equals("RetryableException")) {
                res.put("code", YHTUtils.CODE_TIMEOUT);
            } else if (exceptionName.equals("MyException")) {
                String[] codeMsg = serviceException.getMessage().split("\\|");
                res.put("code", codeMsg[0]);
                res.put("msg", codeMsg[1]);
            }
        } else {
            if ("Bad credentials".equals(e.getMessage())) {
                res.put("code", YHTUtils.CODE_ERROR);
                res.put("msg", "密码错误");
            }
        }

        return ResponseEntity.ok(res);
    }
}
