package com.clockin.record.service;

import com.clockin.record.dto.ClockRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 打卡記錄DTO服務接口
 * <p>
 * 提供帶有用戶、部門等完整信息的打卡記錄DTO對象
 */
public interface ClockRecordDTOService {

    /**
     * 上班打卡
     *
     * @param userId       用戶ID
     * @param clockInTime  上班打卡時間
     * @param workLocation 工作地點
     * @param ipAddress    IP地址
     * @param deviceInfo   設備信息
     * @param remark       備註
     * @return 打卡記錄DTO
     */
    ClockRecordDTO clockIn(Long userId, LocalDateTime clockInTime, String workLocation, String ipAddress, String deviceInfo, String remark);

    /**
     * 下班打卡
     *
     * @param userId        用戶ID
     * @param clockOutTime  下班打卡時間
     * @param workLocation  工作地點
     * @param ipAddress     IP地址
     * @param deviceInfo    設備信息
     * @param remark        備註
     * @return 打卡記錄DTO
     */
    ClockRecordDTO clockOut(Long userId, LocalDateTime clockOutTime, String workLocation, String ipAddress, String deviceInfo, String remark);

    /**
     * 獲取打卡記錄
     *
     * @param id 記錄ID
     * @return 打卡記錄DTO
     */
    Optional<ClockRecordDTO> getRecordById(Long id);

    /**
     * 獲取用戶某日打卡記錄
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @return 打卡記錄DTO
     */
    Optional<ClockRecordDTO> getRecordByUserAndDate(Long userId, LocalDate clockDate);

    /**
     * 更新打卡記錄
     *
     * @param recordDTO 打卡記錄DTO
     * @return 更新後的打卡記錄DTO
     */
    ClockRecordDTO updateRecord(ClockRecordDTO recordDTO);

    /**
     * 獲取用戶日期範圍內的打卡記錄
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 打卡記錄DTO列表
     */
    List<ClockRecordDTO> getRecordsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 分頁查詢打卡記錄
     *
     * @param userId     用戶ID，可為 null
     * @param date       日期，可為 null
     * @param isAbnormal 是否異常，可為 null
     * @param pageable   分頁參數
     * @return 分頁數據
     */
    Page<ClockRecordDTO> getRecordsPage(Long userId, LocalDate date, Boolean isAbnormal, Pageable pageable);

    /**
     * 獲取用戶最近一次打卡記錄
     *
     * @param userId 用戶ID
     * @return 打卡記錄DTO
     */
    Optional<ClockRecordDTO> getLatestRecord(Long userId);

    /**
     * 獲取當前用戶最近一次打卡記錄
     *
     * @return 打卡記錄DTO
     */
    Optional<ClockRecordDTO> getCurrentUserLatestRecord();

    /**
     * 獲取當日所有用戶的打卡記錄
     *
     * @return 打卡記錄DTO列表
     */
    List<ClockRecordDTO> getTodayRecords();

    /**
     * 獲取異常打卡記錄
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 異常打卡記錄DTO列表
     */
    List<ClockRecordDTO> getAbnormalRecords(LocalDate startDate, LocalDate endDate);

    /**
     * 為當前用戶上班打卡
     *
     * @param clockInTime  上班打卡時間，如果為null則使用當前時間
     * @param workLocation 工作地點
     * @param ipAddress    IP地址
     * @param deviceInfo   設備信息
     * @param remark       備註
     * @return 打卡記錄DTO
     */
    ClockRecordDTO currentUserClockIn(LocalDateTime clockInTime, String workLocation, String ipAddress, String deviceInfo, String remark);

    /**
     * 為當前用戶下班打卡
     *
     * @param clockOutTime 下班打卡時間，如果為null則使用當前時間
     * @param workLocation 工作地點
     * @param ipAddress    IP地址
     * @param deviceInfo   設備信息
     * @param remark       備註
     * @return 打卡記錄DTO
     */
    ClockRecordDTO currentUserClockOut(LocalDateTime clockOutTime, String workLocation, String ipAddress, String deviceInfo, String remark);
}
