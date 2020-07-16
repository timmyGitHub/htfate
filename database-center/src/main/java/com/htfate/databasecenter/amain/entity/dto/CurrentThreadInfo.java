package com.htfate.databasecenter.amain.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 本地线程信息
 *
 * @Author 杨海涛
 * @Date 2019/10/15 4:52 下午
 **/
@Getter
@Setter
@ToString
public class CurrentThreadInfo {
    private String id;
    private String name;
    private String operation;
    private Long startTime;
    private Long endTime;
}
