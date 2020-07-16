package com.htfate.oauth2center.entity.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateInfo {
    // 全局
    private String groupId;
    private String artifactId;
    private String author;
    // 数据源
    private String url;
    private String driveName;
    private String username;
    private String password;
    // 包
    private String moduleName;

}
