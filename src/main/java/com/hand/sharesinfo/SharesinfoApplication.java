package com.hand.sharesinfo;

import com.hand.sharesinfo.datadownload.DiaoYongApp;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
@MapperScan("com.hand.sharesinfo.mapper")
public class SharesinfoApplication {

    public static void main(String[] args) {
        try {
            new DiaoYongApp().downLoad();
        } catch (Exception e){
            System.out.println("数据已存在或更新失败");
        }
        SpringApplication.run(SharesinfoApplication.class, args);
    }

}
