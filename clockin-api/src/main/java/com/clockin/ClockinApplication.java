package com.clockin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 打卡系統啟動類
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableFeignClients
@EnableJpaAuditing
public class ClockinApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClockinApplication.class, args);
    }
}
