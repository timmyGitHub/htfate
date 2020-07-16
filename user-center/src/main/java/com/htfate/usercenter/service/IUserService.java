package com.htfate.usercenter.service;

import com.htfate.usercenter.entity.po.TokenPO;
import com.htfate.usercenter.entity.vo.TokenInfoVO;
import com.htfate.usercenter.entity.vo.UserInfoVO;

public interface IUserService {
    UserInfoVO getToken(TokenInfoVO tokenInfoVO);
    TokenPO refreshToken();
    String logout();
}
