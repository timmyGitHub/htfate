package com.htfate.databasecenter.amain.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * 模版管理
 *
 * @author 杨海涛
 * @version 1.0.0
 * @date 2019-04-03
 */


@Getter
@Setter
@ToString
public class TableInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String uuid;
    private String tableName;
    private String tableRemark;
    private String tableType;
    private String clazzName;
    private Long dbParentId;
    private Long parentId;
    private long display;
    private int dbFlag;
    private int codeFlag;
    private int delflag;
    private Timestamp createDate;
    private String createPerson;
    private Timestamp updateDate;
    private String updatePerson;

}