package com.htfate.uploadcenter.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IFileService {

    Map<String, Object> upload(MultipartFile file, String pathType);
    String download(String filePath,String fileName, HttpServletResponse response);
}
