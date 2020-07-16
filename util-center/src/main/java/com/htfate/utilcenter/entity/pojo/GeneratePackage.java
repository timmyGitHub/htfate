package com.htfate.utilcenter.entity.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GeneratePackage {
    private String moduleName;
    private String parentName;
}
