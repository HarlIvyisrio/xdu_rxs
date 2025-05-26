package com.xdurxs.rxs.user.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.xdurxs.rxs.user.biz.domain.mapper")
@EnableFeignClients(basePackages = "com.xdurxs.rxs")
public class RxsUserBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(RxsUserBizApplication.class, args);
    }
}
