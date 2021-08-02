package com.mall.authcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AuthcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthcenterApplication.class, args);
    }

}
