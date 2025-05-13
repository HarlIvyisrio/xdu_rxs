package com.xdurxs.rxs.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.xdurxs.rxs.auth.domain.mapper")
@SpringBootApplication
public class RxsAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(RxsAuthApplication.class, args);
    }
}
