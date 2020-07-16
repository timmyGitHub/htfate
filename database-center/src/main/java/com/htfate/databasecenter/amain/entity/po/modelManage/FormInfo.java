package com.htfate.databasecenter.amain.entity.po.modelManage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormInfo {
    private String pageFormType;
    private String pageFormValue;
    private String defaultValue;
    private String pageFormVerify;
    private String pageFormPlaceholder;
}
