package com.clockin.record.service;

import com.clockin.record.dto.WorkTimeConfigDTO;
import com.clockin.record.entity.WorkTimeConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作時間配置服務接口
 */
public interface WorkTimeConfigService {

    /**
     * 根據用戶ID和日期獲取工作時間配置
     *
     * @param userId 用戶ID
     * @param date 日期
     * @return 工作時間配置
     */
    WorkTimeConfig getWorkTimeConfig(Long userId, LocalDate date);

    /**
     * 獲取用戶在特定日期的上班時間
     *
     * @param userId 用戶ID
     * @param date 日期
     * @return 上班時間
     */
    LocalTime getMorningStartTime(Long userId, LocalDate date);

    /**
     * 獲取用戶在特定日期的上午下班時間
     *
     * @param userId 用戶ID
     * @param date 日期
     * @return 上午下班時間
     */
    LocalTime getMorningEndTime(Long userId, LocalDate date);

    /**
     * 獲取用戶在特定日期的下午上班時間
     *
     * @param userId 用戶ID
     * @param date 日期
     * @return 下午上班時間
     */
    LocalTime getAfternoonStartTime(Long userId, LocalDate date);

    /**
     * 獲取用戶在特定日期的下班時間
     *
     * @param userId 用戶ID
     * @param date 日期
     * @return 下班時間
     */
    LocalTime getAfternoonEndTime(Long userId, LocalDate date);

    /**
     * 獲取用戶在特定日期的彈性時間(分鐘)
     *
     * @param userId 用戶ID
     * @param date 日期
     * @return 彈性時間(分鐘)
     */
    int getFlexibleMinutes(Long userId, LocalDate date);

    /**
     * 判斷是否為工作日
     *
     * @param date 日期
     * @return 是否為工作日
     */
    boolean isWorkday(LocalDate date);

    /**
     * 保存工作時間配置
     *
     * @param config 工作時間配置
     * @return 保存後的工作時間配置
     */
    WorkTimeConfig saveWorkTimeConfig(WorkTimeConfig config);

    /**
     * 根據條件查詢工作時間配置列表
     *
     * @param userId 用戶ID
     * @param departmentId 部門ID
     * @param isActive 是否啟用
     * @param effectiveDate 生效日期
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> findWorkTimeConfigs(Long userId, Long departmentId, Boolean isActive, LocalDate effectiveDate);

    /**
     * 根據ID查詢工作時間配置
     *
     * @param id 配置ID
     * @return 工作時間配置
     */
    Optional<WorkTimeConfig> findById(Long id);

    /**
     * 根據ID刪除工作時間配置
     *
     * @param id 配置ID
     */
    void deleteById(Long id);

    /**
     * 檢查時間是否在允許的範圍內（考慮彈性時間）
     *
     * @param userId 用戶ID
     * @param date 日期
     * @param time 時間
     * @param allowedLateness 是否允許遲到（使用彈性時間）
     * @return 是否在允許的範圍內
     */
    boolean isTimeInAllowedRange(Long userId, LocalDate date, LocalTime time, boolean allowedLateness);

    /**
     * 創建工作時間配置
     *
     * @param configDTO 工作時間配置DTO
     * @return 創建後的工作時間配置
     */
    WorkTimeConfig createWorkTimeConfig(WorkTimeConfigDTO configDTO);

    /**
     * 更新工作時間配置
     *
     * @param id 配置ID
     * @param configDTO 工作時間配置DTO
     * @return 更新後的工作時間配置
     */
    WorkTimeConfig updateWorkTimeConfig(Long id, WorkTimeConfigDTO configDTO);
}
