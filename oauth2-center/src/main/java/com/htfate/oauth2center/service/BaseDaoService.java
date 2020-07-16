package com.htfate.oauth2center.service;

import com.htfate.oauth2center.mapper.IBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseDaoService {
    @Autowired
    private IBaseDao dao;

}
