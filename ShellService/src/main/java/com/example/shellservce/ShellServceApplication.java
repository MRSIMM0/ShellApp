package com.example.shellservce;

import com.example.shellservce.SSHUtils.SSHUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ShellServceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShellServceApplication.class, args);
    }


}
