package com.htfate.app.amain.service.util;

import com.alibaba.fastjson.JSONObject;

public interface IUtilService {
    JSONObject reverseGeoCoding(String coords);
}
