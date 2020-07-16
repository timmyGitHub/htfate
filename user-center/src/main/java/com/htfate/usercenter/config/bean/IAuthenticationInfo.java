package com.htfate.usercenter.config.bean;

import org.springframework.security.core.Authentication;

public interface IAuthenticationInfo {
    Authentication getAuthentication();
}
