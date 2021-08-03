package com.mall.ordercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@MapperScan({"com.mall.ordercenter.dao"})
public class OrdercenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdercenterApplication.class, args);
    }

}
