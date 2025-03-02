package com.clockin.record.service;

import com.clockin.record.dto.ClockInRequest;
import com.clockin.record.dto.ClockInResponse;
import com.clockin.record.dto.ClockInStatisticsDTO;
import com.clockin.record.entity.ClockInRecord;
import com.clockin.record.entity.ClockInSummary;

import java.time.LocalDate;
import java.util.List;

/**
 * 打卡服務接口
 */
public interface ClockInService {

    /**
     * 用戶打卡
     *
     * @param userId         用戶ID
     * @param clockInRequest 打卡請求
     * @return 打卡響應
     */
    ClockInResponse clockIn(Long userId, ClockInRequest clockInRequest);

    /**
     * 獲取用戶當日打卡記錄
     *
     * @param userId 用戶ID
     * @return 打卡記錄
     */
    List<ClockInRecord> getTodayRecords(Long userId);

    /**
     * 獲取用戶指定日期打卡記錄
     *
     * @param userId 用戶ID
     * @param date   日期
     * @return 打卡記錄
     */
    List<ClockInRecord> getRecordsByDate(Long userId, LocalDate date);

    /**
     * 獲取用戶指定日期範圍的打卡記錄
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 打卡記錄
     */
    List<ClockInRecord> getRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 獲取用戶指定日期的打卡匯總
     *
     * @param userId 用戶ID
     * @param date   日期
     * @return 打卡匯總
     */
    ClockInSummary getSummaryByDate(Long userId, LocalDate date);

    /**
     * 獲取用戶指定日期範圍的打卡匯總
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 打卡匯總列表
     */
    List<ClockInSummary> getSummaryByDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 獲取用戶月度打卡統計
     *
     * @param userId 用戶ID
     * @param year   年份
     * @param month  月份
     * @return 打卡統計
     */
    ClockInStatisticsDTO getMonthlyStatistics(Long userId, int year, int month);

    /**
     * 補卡申請
     *
     * @param userId         用戶ID
     * @param clockInRequest 補卡請求
     * @return 補卡響應
     */
    ClockInResponse applyForMakeup(Long userId, ClockInRequest clockInRequest);

    /**
     * 審批補卡申請
     *
     * @param recordId  打卡記錄ID
     * @param approved  是否批准
     * @param approver  審批人ID
     * @param remark    備註
     * @return 是否成功
     */
    boolean approveMakeup(Long recordId, boolean approved, Long approver, String remark);

    /**
     * 計算並更新日打卡匯總
     *
     * @param userId 用戶ID
     * @param date   日期
     * @return 匯總結果
     */
    ClockInSummary calculateAndUpdateSummary(Long userId, LocalDate date);

    /**
     * 批量計算並更新日打卡匯總
     *
     * @param date 日期
     * @return 成功處理的記錄數
     */
    int batchCalculateAndUpdateSummary(LocalDate date);
}
