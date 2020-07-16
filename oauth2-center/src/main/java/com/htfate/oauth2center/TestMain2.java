package com.htfate.oauth2center;

import com.google.common.collect.Lists;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;

public class TestMain2 {
    public static void main(String[] args) {
        System.out.println(Lists.newArrayList("1-".split("-")).toString());
        System.out.println(YHTUtils.getBase64Encoder("htfate:htfate"));
    }
}
