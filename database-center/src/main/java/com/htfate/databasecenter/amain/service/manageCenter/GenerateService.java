package com.htfate.databasecenter.amain.service.manageCenter;

import java.util.Map;

/**
 * @Author 杨海涛
 * @Date 2019-04-09 21:46
 * @Version 1.0.0
 **/
public interface GenerateService {
    /**
     * 生成代码
     *
     * @param params 参数
     * @return 结果
     */
    String generate(Map<String, Object> params);
}
