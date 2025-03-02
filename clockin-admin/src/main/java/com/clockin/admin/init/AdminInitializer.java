package com.clockin.admin.init;

import com.clockin.admin.service.WorkTimeConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 管理模組初始化類
 * 用於系統啟動時執行必要的初始化操作
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final WorkTimeConfigService workTimeConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("管理模組初始化開始...");
        
        try {
            // 檢查是否有預設工作時間配置，如果沒有則創建
            checkDefaultWorkTimeConfig();
            
            // 其他初始化操作...
            
            log.info("管理模組初始化完成");
        } catch (Exception e) {
            log.error("管理模組初始化失敗", e);
            throw e;
        }
    }
    
    /**
     * 檢查預設工作時間配置
     */
    private void checkDefaultWorkTimeConfig() {
        log.info("檢查預設工作時間配置");
        
        try {
            boolean hasDefaultConfig = workTimeConfigService.hasDefaultConfig();
            
            if (!hasDefaultConfig) {
                log.info("未找到預設工作時間配置，將創建預設配置");
                // 這裡可以呼叫服務創建預設配置
                // workTimeConfigService.createDefaultConfig();
            } else {
                log.info("已存在預設工作時間配置");
            }
        } catch (Exception e) {
            log.error("檢查預設工作時間配置失敗", e);
        }
    }
}
