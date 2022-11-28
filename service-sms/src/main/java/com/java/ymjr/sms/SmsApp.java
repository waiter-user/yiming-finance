package com.java.ymjr.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.java.ymjr.base","com.java.ymjr.sms"})
public class SmsApp {
    public static void main(String[] args) {
        SpringApplication.run(SmsApp.class,args);
    }
}
