package com.hand.sharesinfo;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hand.sharesinfo.mapper")
public class SharesinfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharesinfoApplication.class, args);
    }

}
