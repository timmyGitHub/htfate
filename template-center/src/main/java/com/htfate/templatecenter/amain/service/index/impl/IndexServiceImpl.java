package com.htfate.templatecenter.amain.service.index.impl;

import com.htfate.templatecenter.amain.service.index.IIndexService;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IndexServiceImpl implements IIndexService {

    @Override
    public void test(String msg) {
        Map threadLocal = (Map) YHTUtils.getThreadLocal();
//        websocketServer.sendInfo("10", msg);
        String sql = "SELECT * FROM table_info WHERE id = ?";
        System.out.println(threadLocal.get("username"));
    }
}
