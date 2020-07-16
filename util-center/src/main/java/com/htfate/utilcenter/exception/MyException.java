package com.htfate.utilcenter.exception;

public class MyException extends RuntimeException {

    /**
     * 自定义异常
     * @param msg 异常信息
     */
    public MyException(String msg) {
        super(1 + "|" + msg);
    }

    /**
     * 自定义异常
     * @param code 错误代码
     * @param msg 错误信息
     */
    public MyException(int code, String msg) {
        super(code + "|" + msg);
    }
}
