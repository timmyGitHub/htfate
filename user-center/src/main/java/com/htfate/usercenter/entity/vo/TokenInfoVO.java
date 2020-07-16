package com.htfate.usercenter.entity.vo;

import lombok.Data;

@Data
public class TokenInfoVO {
    private String username;
    private String password;
    private String refreshToken;
    private String accessToken;
}
