package com.htfate.uploadcenter.service.impl;

import com.htfate.uploadcenter.service.IFileService;
import com.purerland.utilcenter.exception.MyException;
import com.purerland.utilcenter.util.YHTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements IFileService {
    @Value("${custom.upload-path}")
    private String filePath;
    @Value("${custom.download-path}")
    private String downloadPath;
    @Value("${custom.isZuul}")
    private boolean isZuul;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public String download(String filePath, String fileName, HttpServletResponse response) {
        if (YHTUtils.isEmpty(fileName) || YHTUtils.isEmpty(fileName)) {
            throw new MyException("文件名为空");
        }

        File file = new File(downloadPath + filePath);
        if (file.exists()) { //判断文件父目录是否存在

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            try {
                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @Override
    public Map<String, Object> upload(MultipartFile file, String pathType) {
        if (YHTUtils.isEmpty(file)) {
            throw new MyException("上传文件为空");
        }

        String originalFileName, suffix, oldFileName, fileName, fileNamePre, accessUrl;
        long fileSize = file.getSize();
        originalFileName = file.getOriginalFilename();
        suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        oldFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        fileName = YHTUtils.getUUID() + "." + suffix;

        if (YHTUtils.isEmpty(pathType)) {
            fileNamePre = "/default";
        } else {
            fileNamePre = "/" + pathType;
        }

        accessUrl = "/upload";
        if (isZuul) {
            accessUrl = "/"+appName + accessUrl;
        }
        File dest;

        switch (suffix) {
            case "jpeg":
            case "jpg":
            case "png":
                fileNamePre += "/images/" + fileName;
                dest = new File(filePath + fileNamePre);
                break;
            case "mp4":
            case "avi":
            case "rm":
                fileNamePre += "/mv/" + fileName;
                dest = new File(filePath + fileNamePre);
                break;
            case "txt":
            case "xls":
            case "doc":
                fileNamePre += "/document/" + fileName;
                dest = new File(filePath + fileNamePre);
                break;
            case "rar":
            case "zip":
            case "7z":
                fileNamePre += "/zip/" + fileName;
                dest = new File(filePath + fileNamePre);
                break;
            default:
                fileNamePre += "/common/" + fileName;
                dest = new File(filePath + fileNamePre);
        }
        writeFile(file, dest);

        Map<String, Object> result = new HashMap<>();
        result.put("localPath",filePath+fileNamePre);
        result.put("url", accessUrl + fileNamePre);
        result.put("downloadUrl", "/upload" + fileNamePre);
        result.put("originalFileName", originalFileName);
        result.put("oldFileName", oldFileName);
        result.put("newFileName", fileName);
        result.put("suffix", suffix);
        result.put("fileSize", fileSize);
        return result;
    }

    /**
     * 写文件
     */
    private void writeFile(MultipartFile origin, File dest) {
        if (!dest.getParentFile().exists()) {
            if (dest.getParentFile().mkdirs()) {
                try {
                    origin.transferTo(dest);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new MyException("文件上传异常\r\n" + e.toString());
                }
            }
        } else {
            try {
                origin.transferTo(dest);
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException("文件上传异常\r\n" + e.toString());
            }
        }
    }
}
