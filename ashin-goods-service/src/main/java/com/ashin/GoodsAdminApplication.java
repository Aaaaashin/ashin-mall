package com.ashin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.ashin.mapper")
//@EnableDiscoveryClient
public class GoodsAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsAdminApplication.class,args);
    }
}