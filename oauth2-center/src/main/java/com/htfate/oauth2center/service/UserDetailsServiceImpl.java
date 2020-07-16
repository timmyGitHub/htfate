package com.htfate.oauth2center.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.htfate.oauth2center.feign.IFeign;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IFeign feign;

    private Map res;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (Strings.isNullOrEmpty(username)) {
            throw new MyException("username is null");
        }
        // 查找用户
        String sql = "SELECT * FROM tb_user WHERE delflag=0 AND account=?";
        Map<String, Object> reqParams;
        reqParams = new HashMap<>();

        reqParams.put("sql", sql);
        reqParams.put("params", Lists.newArrayList(username));

        res = (Map) feign.executeSqlOne(reqParams);
        YHTUtils.checkRes(res);
        // 该获取用户进行判断了
        Map user = (Map) res.get("data");

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (user.size() > 0) {
            sql = "SELECT*FROM tb_auth WHERE delflag=0 AND id IN (\n" +
                    "SELECT auth_id FROM tb_role_auth WHERE delflag=0 AND role_id IN (\n" +
                    "SELECT role_id FROM tb_user_role WHERE delflag=0 AND user_id=(\n" +
                    "SELECT id FROM tb_user WHERE delflag=0 AND id=?)))";

            reqParams = new HashMap<>();
            reqParams.put("sql", sql);
            reqParams.put("params", Lists.newArrayList(user.get("id")));

            res = (Map) feign.executeSql(reqParams);
            YHTUtils.checkRes(res);

            List<Map<String, Object>> tbPermissions = (List<Map<String, Object>>) res.get("data");

            tbPermissions.forEach(tbPermission -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(tbPermission.get("en_name").toString());
                grantedAuthorityList.add(grantedAuthority);
            });
        } else {
            throw new MyException("用户不存在");
        }

        return new User(user.get("account").toString(), user.get("password").toString(), grantedAuthorityList);
        /*List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("system");
        grantedAuthorityList.add(grantedAuthority);
        return new User("timmy", "$2a$10$0vYFMpv3zb2R6PNbxonOL.j2TKsmHDVvGgIQZIqaEbHlIfDUYzrum", grantedAuthorityList);*/
    }
}
