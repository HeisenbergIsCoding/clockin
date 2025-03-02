package com.clockin.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ClockIn 管理員應用程序啟動類
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.clockin"})
@EnableFeignClients
@EnableJpaAuditing
public class ClockInAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClockInAdminApplication.class, args);
    }
}
