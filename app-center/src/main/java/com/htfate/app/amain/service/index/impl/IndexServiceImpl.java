package com.htfate.app.amain.service.index.impl;

import com.google.common.collect.Lists;
import com.htfate.app.amain.service.BaseDaoServer;
import com.htfate.app.amain.service.index.IIndexService;
import com.htfate.app.server.WebsocketServer;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IndexServiceImpl implements IIndexService {
    @Autowired
    private WebsocketServer websocketServer;
    @Autowired
    private BaseDaoServer baseDaoServer;

    @Override
    public void sendMsg(String msg) {
        Map threadLocal = (Map) YHTUtils.getThreadLocal();
//        websocketServer.sendInfo("10", msg);
        String sql = "SELECT * FROM table_info WHERE id = ?";
        baseDaoServer.executeSql(sql, Lists.newArrayList(5L));
    }
}
