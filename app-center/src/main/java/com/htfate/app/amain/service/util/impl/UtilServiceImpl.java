package com.htfate.app.amain.service.util.impl;

import com.alibaba.fastjson.JSONObject;
import com.htfate.app.amain.service.util.IUtilService;
import com.htfate.utilcenter.exception.MyException;
import com.htfate.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UtilServiceImpl implements IUtilService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JSONObject reverseGeoCoding(String coords) {
        if (YHTUtils.isEmpty(coords)) {
            throw new MyException("coords is null");
        }
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?" +
                "ak=FGTjVgfOWTEV4neGkM4BjjaP2YjYuH95" +
                "&output=json" +
                "&coordtype=wgs84ll" +
                "&location="+coords;
        String res = restTemplate.getForObject(url,String.class);
        return JSONObject.parseObject(res);
    }
}
