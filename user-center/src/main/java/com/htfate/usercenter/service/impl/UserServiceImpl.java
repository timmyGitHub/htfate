package com.htfate.usercenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.htfate.usercenter.config.bean.AuthenticationInfo;
import com.htfate.usercenter.entity.po.TokenPO;
import com.htfate.usercenter.entity.vo.TokenInfoVO;
import com.htfate.usercenter.entity.vo.UserInfoVO;
import com.htfate.usercenter.feign.IFeign;
import com.htfate.usercenter.service.IUserService;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AuthenticationInfo authenticationInfo;
    @Autowired
    private IFeign feign;

    private Map res;

    @Override
    public String logout() {
        Map threadLocal = (Map) YHTUtils.getThreadLocal();
        String account = (String) threadLocal.get("account");
        if (Strings.isNullOrEmpty(account)) {
            return "已退出";
        }
        String access_token = redisService.getValue(account);

        if (Strings.isNullOrEmpty(access_token)) {
            return "已退出";
        }
        res = (Map) feign.logout(access_token);
        YHTUtils.checkRes(res);
        res = (Map) feign.removeToken(access_token);
        YHTUtils.checkRes(res);
        redisService.del(account);
        redisService.del(threadLocal.get("access_token").toString());
        return "已退出";
    }

    @Override
    public TokenPO refreshToken() {
        Map threadLocal = (Map) YHTUtils.getThreadLocal();
        String access_token = (String) res.get("access_token");
        String refreshToken;
        if (Strings.isNullOrEmpty(access_token)) {
            refreshToken = redisService.getValue(threadLocal.get("account").toString());
        } else {
            refreshToken = redisService.getValue(access_token);
        }
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setUsername(threadLocal.get("account").toString());
        tokenInfoVO.setRefreshToken(refreshToken);
        tokenInfoVO.setAccessToken(access_token);
        return token(tokenInfoVO,"refresh_token");
    }

    @Override
    public UserInfoVO getToken(TokenInfoVO tokenInfoVO) {
        TokenPO tokenPO = token(tokenInfoVO, "password");
        return UserInfoVO.builder().token(tokenPO.getAccess_token()).username(tokenInfoVO.getUsername()).build();
    }

    public TokenPO token(TokenInfoVO tokenInfoVO, String grantType) {
        String url = "http://localhost:52001/oauth2/oauth/token";

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("grant_type", grantType);
        if ("password".equals(grantType)) {
            postParameters.add("username", tokenInfoVO.getUsername());
            postParameters.add("password", tokenInfoVO.getPassword());
        } else if ("refresh_token".equals(grantType)) {
            tokenInfoVO.setRefreshToken(getRefreshToken(tokenInfoVO.getUsername()));
            postParameters.add("refresh_token", tokenInfoVO.getRefreshToken());
        }
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/x-www-form-urlencoded");
        headers.setContentType(mediaType);
//        headers.add("content-type", "application/x-www-form-urlencoded");
        headers.add("authorization", "Basic " + YHTUtils.getBase64Encoder("htfate:htfate"));
//        headers.add("cache-control", "no-cache");

        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);

        String result;
        TokenPO tokenPO;

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, r, String.class);
        result = responseEntity.getBody();

        // 判断是否返回正确的信息
        Map codeMap = JSON.parseObject(result, Map.class);
        if (null != codeMap.get("code")) {
            throw new MyException(codeMap.get("msg").toString());
        }
        try {
            tokenPO = JSON.parseObject(result, TokenPO.class);
        } catch (Exception e) {
            throw new MyException("解析出错: " + result);
        }
        redisService.setValue(tokenInfoVO.getUsername(), tokenPO.getRefresh_token());

        if ("refresh_token".equals(grantType)) {
            redisService.del(tokenInfoVO.getAccessToken());
            redisService.setSetValue(tokenPO.getAccess_token(), tokenPO.getRefresh_token());
        }
        return tokenPO;
    }

    /**
     * 获取 refresh_token
     */
    private String getRefreshToken(String username) {
        return redisService.getValue(username);
    }
}
