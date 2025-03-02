package com.clockin.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定時任務配置類
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    /**
     * 配置定時任務執行的執行緒池
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // 設置執行緒池大小
        scheduler.setThreadNamePrefix("clockin-admin-scheduled-task-"); // 指定執行緒名稱字首
        scheduler.setAwaitTerminationSeconds(60); // 等待終止的秒數
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // 等待任務完成後再關閉
        scheduler.setRemoveOnCancelPolicy(true); // 移除已取消的任務
        scheduler.setErrorHandler(throwable -> {
            // 錯誤處理邏輯，例如記錄日誌或通知管理員
            System.err.println("Scheduled task error: " + throwable.getMessage());
        });
        return scheduler;
    }
}
