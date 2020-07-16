package com.htfate.utilcenter.util;

import lombok.Data;

import java.util.Map;

@Data
public class ReturnMsg {
    private Integer code;
    private String msg;
    private Object data;
}
