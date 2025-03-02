package com.clockin.record.task;

import com.clockin.record.service.ClockInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 打卡排程任務
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClockInTask {

    private final ClockInService clockInService;

    /**
     * 每天凌晨1點執行，計算前一天的打卡匯總
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void calculateDailyClockInSummary() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("開始計算 {} 的打卡匯總數據", yesterday);
        
        try {
            int count = clockInService.batchCalculateAndUpdateSummary(yesterday);
            log.info("完成 {} 的打卡匯總數據計算，共處理 {} 條記錄", yesterday, count);
        } catch (Exception e) {
            log.error("計算 {} 的打卡匯總數據失敗: {}", yesterday, e.getMessage(), e);
        }
    }

    /**
     * 每月1號凌晨2點執行，重新計算上個月的所有打卡匯總
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void recalculateMonthlyClockInSummary() {
        LocalDate firstDayOfLastMonth = LocalDate.now().withDayOfMonth(1).minusMonths(1);
        LocalDate lastDayOfLastMonth = LocalDate.now().withDayOfMonth(1).minusDays(1);
        
        log.info("開始重新計算 {} 至 {} 的打卡匯總數據", firstDayOfLastMonth, lastDayOfLastMonth);
        
        try {
            int totalCount = 0;
            LocalDate currentDate = firstDayOfLastMonth;
            
            while (!currentDate.isAfter(lastDayOfLastMonth)) {
                int count = clockInService.batchCalculateAndUpdateSummary(currentDate);
                totalCount += count;
                currentDate = currentDate.plusDays(1);
            }
            
            log.info("完成 {} 至 {} 的打卡匯總數據重新計算，共處理 {} 條記錄", 
                    firstDayOfLastMonth, lastDayOfLastMonth, totalCount);
        } catch (Exception e) {
            log.error("重新計算 {} 至 {} 的打卡匯總數據失敗: {}", 
                    firstDayOfLastMonth, lastDayOfLastMonth, e.getMessage(), e);
        }
    }

    /**
     * 每天晚上23:55執行，檢查並處理未打卡記錄
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void handleMissingClockInRecords() {
        LocalDate today = LocalDate.now();
        log.info("開始處理 {} 的未打卡記錄", today);
        
        try {
            // 此處可以實現自動檢查當天未打卡的用戶，並進行相應處理
            // 例如發送郵件提醒或自動標記為缺勤
            log.info("完成 {} 的未打卡記錄處理", today);
        } catch (Exception e) {
            log.error("處理 {} 的未打卡記錄失敗: {}", today, e.getMessage(), e);
        }
    }
}
