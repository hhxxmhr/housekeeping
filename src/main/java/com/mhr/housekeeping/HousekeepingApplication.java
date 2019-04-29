package com.mhr.housekeeping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mhr.housekeeping.dao")
public class HousekeepingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HousekeepingApplication.class, args);
    }
}
