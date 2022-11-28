package com.java.ymjr.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.java.ymjr.base","com.java.ymjr.core","com.java.ymjr.common.pojo"})
@MapperScan(basePackages = {"com.java.ymjr.core.mapper"})
public class CoreApp {
    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class,args);
    }
}
