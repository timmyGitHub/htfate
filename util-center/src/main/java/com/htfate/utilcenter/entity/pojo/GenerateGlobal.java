package com.htfate.utilcenter.entity.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GenerateGlobal implements Serializable {
    private String parentModuleName;
    private String author;
    private String path;
    private boolean fileOverride;
    private boolean swagger2;

    public GenerateGlobal() {
        this.path = System.getProperty("user.dir") + "/generate-code";
        this.fileOverride = false;
        this.swagger2 = false;
    }
}
