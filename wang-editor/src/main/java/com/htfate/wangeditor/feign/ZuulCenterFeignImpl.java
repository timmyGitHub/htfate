package com.htfate.wangeditor.feign;

import com.purerland.utilcenter.util.UtilsReturnMsg;
import org.springframework.stereotype.Component;

@Component
public class ZuulCenterFeignImpl implements ZuulCenterFeign {
    @Override
    public Object test(String id) {
        return UtilsReturnMsg.error("请求失败，这是默认的错误");
    }
}
