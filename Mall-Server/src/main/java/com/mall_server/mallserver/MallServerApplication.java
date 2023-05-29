package com.mall_server.mallserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mall_server.mallserver.mapper")
public class MallServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallServerApplication.class, args);
    }

}
