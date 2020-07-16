package com.htfate.databasecenter.amain.entity.vo;

import com.htfate.databasecenter.amain.entity.po.TableInfo;
import com.htfate.databasecenter.amain.entity.po.modelManage.FormInfo;
import com.htfate.databasecenter.amain.entity.po.modelManage.ModelManageField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class VOTableAndField {
    private TableInfo tableInfo;
    private List<ModelManageField> dbField;
    private List<FormInfo> pageField;
}
