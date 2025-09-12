package com.aoyu.bitsetup.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName：BitSetupServerClientApplication
 * @Author: aoyu
 * @Date: 2025-07-16 16:47
 * @Description: 客户端服务启动类
 */
@SpringBootApplication
@MapperScan("com.aoyu.bitsetup.client")
public class BitSetupServerClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(BitSetupServerClientApplication.class,args);
    }
}
