package com.htfate.app.config.bean;

import org.springframework.security.core.Authentication;

public interface IAuthenticationInfo {
    Authentication getAuthentication();
}
