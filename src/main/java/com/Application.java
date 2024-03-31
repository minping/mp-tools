package com;

import fileKit.FileKit;
import fileKit.FileSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        System.out.println("启动成功(●'◡'●)");

        // 导出逻辑
        //String filePath = FileSupport.createFile(bytes, fileName, uploadProp.getVirtualPath(), ".doc");
        //FileKit.output(response, request, new File(filePath));
    }
}