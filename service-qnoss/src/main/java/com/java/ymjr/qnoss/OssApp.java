package com.java.ymjr.qnoss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.java.ymjr.base","com.java.ymjr.qnoss"})
public class OssApp {
    public static void main(String[] args) {
        SpringApplication.run(OssApp.class,args);
    }
}
