package com.htfate.uploadcenter;

import com.purerland.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class OtherConfig extends WebMvcConfigurationSupport {
    @Value("${custom.upload-path}")
    private String locationPath;// 文件存放路径
    @Value("${custom.resource-path}")
    private String resourcePath;// 资源访问路径

    /**
     * 添加虚拟目录
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        YHTUtils.log(locationPath, resourcePath);
        registry.addResourceHandler(resourcePath).addResourceLocations("file:"+locationPath);
        // 如果是阿里云
        //registry.addResourceHandler("/memory_file/**").addResourceLocations("/memory_file/");
    }

}
