package com.htfate.wangeditor.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RichText {
    private String id;
    private String title;
    private String context;
    private Date createDate;
    private String createPerson;
    private Date updateDate;
    private String updatePerson;
    private Integer delflag;
    private Integer type;
}
