package com.lntravel.scenic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 景区服务启动类
 */
@SpringBootApplication
@MapperScan("com.lntravel.scenic.mapper")
@EnableFeignClients
public class ScenicServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScenicServiceApplication.class, args);
    }
}

