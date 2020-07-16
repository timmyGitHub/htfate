package com.htfate.utilcenter.util;

@SuppressWarnings("ALL")
public class UtilsReturnMsg {
    private static ReturnMsg returnMsg;
    private static ReturnMsgE returnMsgE;
    private static ReturnMsgE2 returnMsgE2;
    private static ReturnMsgE3 returnMsgE3;

    public static Object success() {
        returnMsgE3 = new ReturnMsgE3();
        returnMsgE3.setCode(0);
        return returnMsgE3;
    }
    public static Object success(Object data) {
        returnMsgE2 = new ReturnMsgE2();
        returnMsgE2.setCode(0);
        returnMsgE2.setData(data);
        return returnMsgE2;
    }
    public static Object error(String msg) {
        returnMsgE = new ReturnMsgE();
        returnMsgE.setMsg(msg);
        returnMsgE.setCode(101);
        return returnMsgE;
    }
    public static Object error(Integer code,String msg) {
        returnMsgE = new ReturnMsgE();
        returnMsgE.setMsg(msg);
        returnMsgE.setCode(code);
        return returnMsgE;
    }
}
