package com.fys.wx.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class WxProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxProjectApplication.class, args);
    }

}
