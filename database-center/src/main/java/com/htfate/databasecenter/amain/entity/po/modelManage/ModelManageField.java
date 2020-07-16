package com.htfate.databasecenter.amain.entity.po.modelManage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModelManageField {
    private String fieldName;
    private String fieldRemark;
    private String fieldType;
    private String fieldKey;
    private long fieldDisplay;
}
