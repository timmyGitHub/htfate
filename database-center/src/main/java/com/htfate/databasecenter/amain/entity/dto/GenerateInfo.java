package com.htfate.databasecenter.amain.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author 杨海涛
 * @Date 2019-04-09 21:55
 * @Version 1.0.0
 **/
@Getter
@Setter
@ToString
public class GenerateInfo {
    private String tableId;
    private String tableAuthor;
    private String tableName;
    private String tableRemark;
    private Boolean controller;
    private Boolean entity;
    private Boolean service;
    private Boolean dao;
}
