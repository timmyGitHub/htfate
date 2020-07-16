package com.htfate.usercenter.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoVO {
    private String username;
    private String token;
}
