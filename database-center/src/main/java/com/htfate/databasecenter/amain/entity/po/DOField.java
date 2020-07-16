package com.htfate.databasecenter.amain.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * 模板管理中的表信息
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-04-02
 */


@Getter
@Setter
@ToString
@TableName("dofield")
public class DOField {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String uuid;
    private String fieldName;
    private String fieldRemark;
    private String fieldType;
    private String fieldKey;
    private String javaVariable;
    private String javaType;
    private String formType;
    private String formValue;
    private String formDefaultValue;
    private String formPlaceholder;
    private String formVerify;
    private Long tableId;
    private Long dbParentId;
    private String parentId;
    private Integer display;
    private Integer delflag;
    private Timestamp createDate;
    private String createPerson;
    private Timestamp updateDate;
    private String updatePerson;

}