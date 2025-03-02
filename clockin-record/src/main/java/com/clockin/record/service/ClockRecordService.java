package com.clockin.record.service;

import com.clockin.record.entity.ClockRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 打卡記錄服務接口
 */
public interface ClockRecordService {
    
    /**
     * 創建打卡記錄
     *
     * @param record 打卡記錄
     * @return 創建後的打卡記錄
     */
    ClockRecord createRecord(ClockRecord record);
    
    /**
     * 上班打卡
     *
     * @param userId       用戶ID
     * @param clockInTime  上班打卡時間
     * @param workLocation 工作地點
     * @param ipAddress    IP地址
     * @param deviceInfo   設備信息
     * @param remark       備註
     * @return 打卡記錄
     */
    ClockRecord clockIn(Long userId, LocalDateTime clockInTime, String workLocation, String ipAddress, String deviceInfo, String remark);
    
    /**
     * 下班打卡
     *
     * @param userId        用戶ID
     * @param clockOutTime  下班打卡時間
     * @param workLocation  工作地點
     * @param ipAddress     IP地址
     * @param deviceInfo    設備信息
     * @param remark        備註
     * @return 打卡記錄
     */
    ClockRecord clockOut(Long userId, LocalDateTime clockOutTime, String workLocation, String ipAddress, String deviceInfo, String remark);
    
    /**
     * 獲取打卡記錄
     *
     * @param id 記錄ID
     * @return 打卡記錄
     */
    Optional<ClockRecord> getRecordById(Long id);
    
    /**
     * 獲取用戶某日打卡記錄
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @return 打卡記錄
     */
    Optional<ClockRecord> getRecordByUserAndDate(Long userId, LocalDate clockDate);
    
    /**
     * 更新打卡記錄
     *
     * @param record 打卡記錄
     * @return 更新後的打卡記錄
     */
    ClockRecord updateRecord(ClockRecord record);
    
    /**
     * 刪除打卡記錄
     *
     * @param id 記錄ID
     */
    void deleteRecord(Long id);
    
    /**
     * 獲取用戶日期範圍內的打卡記錄
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 打卡記錄列表
     */
    List<ClockRecord> getRecordsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 分頁查詢打卡記錄
     *
     * @param userId   用戶ID，可為 null
     * @param date     日期，可為 null
     * @param isAbnormal 是否異常，可為 null
     * @param pageable 分頁參數
     * @return 分頁數據
     */
    Page<ClockRecord> getRecordsPage(Long userId, LocalDate date, Boolean isAbnormal, Pageable pageable);
    
    /**
     * 批量導入打卡記錄
     *
     * @param records 打卡記錄列表
     * @return 導入後的打卡記錄列表
     */
    List<ClockRecord> batchImportRecords(List<ClockRecord> records);
    
    /**
     * 統計用戶指定日期範圍內的工作總時長
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 工作總時長（分鐘）
     */
    Integer getTotalWorkDuration(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 獲取用戶最近一次打卡記錄
     *
     * @param userId 用戶ID
     * @return 打卡記錄
     */
    Optional<ClockRecord> getLatestRecord(Long userId);
    
    /**
     * 獲取當日所有用戶的打卡記錄
     *
     * @return 打卡記錄列表
     */
    List<ClockRecord> getTodayRecords();
    
    /**
     * 獲取異常打卡記錄
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 異常打卡記錄列表
     */
    List<ClockRecord> getAbnormalRecords(LocalDate startDate, LocalDate endDate);
}
