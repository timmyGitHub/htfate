package com.htfate.utilcenter.entity.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenerateDataSource {
    private String url;
    private String driverName;
    private String username;
    private String password;

    private String tableName;// 若生成多个，用 ',' 隔开
}
