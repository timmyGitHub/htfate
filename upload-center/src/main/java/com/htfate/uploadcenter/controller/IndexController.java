package com.htfate.uploadcenter.controller;

import com.htfate.uploadcenter.service.IFileService;
import com.purerland.utilcenter.util.UtilsReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("upload")
public class IndexController {
    @Autowired
    private IFileService fileService;

    @GetMapping("test")
    public Object test(String name,String age) {
        return UtilsReturnMsg.success(name+age);
    }

    /**
     * 文件下载
     * */
    @GetMapping("download")
    public Object download(String filePath,String fileName, HttpServletResponse response) {
        return UtilsReturnMsg.success(fileService.download(filePath,fileName, response));
    }

    /**
     * 文件上传
     * */
    @PostMapping()
    public Object upload(MultipartFile file) {

        return UtilsReturnMsg.success(fileService.upload(file, null));

    }
    /**
     * 粘贴板 文件上传
     * */
    @PostMapping("paste")
    public Object uploadPaste(MultipartFile file) {

        return UtilsReturnMsg.success(fileService.upload(file,"paste"));

    }


}
