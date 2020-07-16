package com.htfate.utilcenter.exception;

import com.htfate.utilcenter.util.UtilsReturnMsg;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object Handle(Exception e) {
        e.printStackTrace();
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            String[] codeMsg = myException.getMessage().split("\\|");
            return UtilsReturnMsg.error(Integer.parseInt(codeMsg[0]),codeMsg[1]);
        } else if (e instanceof MyAuthorizeException) {
            MyAuthorizeException authorizeException = (MyAuthorizeException) e;
            return UtilsReturnMsg.error(401, e.getMessage());
        } else {
            return UtilsReturnMsg.error(101,e.getMessage());
        }
    }
}
