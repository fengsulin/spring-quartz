package com.lin.quartz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lin.quartz.mapper")
public class SpringQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringQuartzApplication.class, args);
    }

}
