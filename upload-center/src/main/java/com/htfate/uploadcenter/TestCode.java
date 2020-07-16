package com.htfate.uploadcenter;

import com.purerland.utilcenter.util.YHTUtils;

import java.io.File;

public class TestCode {
    public static void main(String[] args) {
        String str = "/Users/timmy/Documents/upload/weixin/37/9/a82917099492e94245e219675cf17.jpg";
        String sql = "/users/timmy/downloads/purerland.sql";
//        YHTUtils.writerFileByLine("timmy", sql);
//        System.out.println(YHTUtils.readFile(sql));

        String f = "/users/timmy/downloads/王诗安 - Home.mp4.crdownload";
        String f3 = "/users/timmy/downloads/王诗安 - Home.mp4";
        String f2 = "/users/timmy/downloads/截屏2020-04-08 上午9.25.14.png";

        File file = new File(f3);
        System.out.println(file.exists());
        System.out.println(file.isFile());
        System.out.println(file.length());
    }
}