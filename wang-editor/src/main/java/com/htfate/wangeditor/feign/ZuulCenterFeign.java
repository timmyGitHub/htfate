package com.htfate.wangeditor.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zuul-center-1", fallback = ZuulCenterFeignImpl.class)
public interface ZuulCenterFeign {
    @GetMapping("page-center/test/{id}")
    Object test(@PathVariable String id);
}
