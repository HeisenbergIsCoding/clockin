package com.clockin.admin.task;

import com.clockin.admin.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 假日同步定時任務
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HolidaySyncTask {

    private final HolidayService holidayService;

    /**
     * 每月第一天凌晨2點檢查未來三個月假日數據
     * cron: 秒 分 時 日 月 星期
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void checkAndSyncHolidays() {
        log.info("開始執行假日數據檢查任務");
        
        try {
            LocalDate now = LocalDate.now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();
            
            // 檢查當前月及未來兩個月的假日數據
            for (int i = 0; i < 3; i++) {
                int targetMonth = currentMonth + i;
                int targetYear = currentYear;
                
                // 處理跨年情況
                if (targetMonth > 12) {
                    targetMonth = targetMonth - 12;
                    targetYear++;
                }
                
                log.info("檢查 {}-{} 月份假日數據", targetYear, targetMonth);
                
                // 這裡可以調用服務方法來檢查和同步特定月份的假日數據
                // 例如：holidayService.checkAndSyncHolidaysByYearAndMonth(targetYear, targetMonth);
                
                // 示例假日數據處理邏輯
                int holidayCount = holidayService.countHolidaysByYearAndMonth(targetYear, targetMonth);
                log.info("{}-{} 月份共有 {} 個假日", targetYear, targetMonth, holidayCount);
            }
            
            log.info("假日數據檢查任務執行完成");
        } catch (Exception e) {
            log.error("假日數據檢查任務執行失敗", e);
        }
    }
    
    /**
     * 每天凌晨1點更新假日緩存
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateHolidayCache() {
        log.info("開始更新假日緩存數據");
        
        try {
            // 更新當年假日緩存
            int currentYear = LocalDate.now().getYear();
            holidayService.refreshHolidayCache(currentYear);
            
            log.info("假日緩存數據更新完成");
        } catch (Exception e) {
            log.error("假日緩存數據更新失敗", e);
        }
    }
}
