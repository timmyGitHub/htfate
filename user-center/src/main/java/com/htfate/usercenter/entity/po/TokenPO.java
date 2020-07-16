package com.htfate.usercenter.entity.po;

import lombok.Data;

@Data
public class TokenPO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
}
