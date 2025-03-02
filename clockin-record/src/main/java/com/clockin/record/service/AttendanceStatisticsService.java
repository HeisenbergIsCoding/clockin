package com.clockin.record.service;

import com.clockin.record.dto.AttendanceStatisticsDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * 考勤統計服務接口
 */
public interface AttendanceStatisticsService {

    /**
     * 獲取用戶在指定日期範圍內的考勤統計
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 考勤統計數據
     */
    AttendanceStatisticsDTO getUserStatistics(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 獲取當前用戶在指定日期範圍內的考勤統計
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 考勤統計數據
     */
    AttendanceStatisticsDTO getCurrentUserStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 獲取部門在指定日期範圍內的考勤統計
     *
     * @param departmentId 部門ID
     * @param startDate    開始日期
     * @param endDate      結束日期
     * @return 考勤統計數據列表（部門內每個用戶的統計）
     */
    List<AttendanceStatisticsDTO> getDepartmentStatistics(Long departmentId, LocalDate startDate, LocalDate endDate);

    /**
     * 獲取所有用戶在指定日期範圍內的考勤統計
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 考勤統計數據列表（所有用戶的統計）
     */
    List<AttendanceStatisticsDTO> getAllUserStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 生成月度考勤報表
     *
     * @param year  年份
     * @param month 月份（1-12）
     * @return 考勤統計數據列表
     */
    List<AttendanceStatisticsDTO> generateMonthlyReport(int year, int month);

    /**
     * 生成考勤異常用戶統計
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 考勤異常用戶統計
     */
    List<AttendanceStatisticsDTO> getAbnormalUserStatistics(LocalDate startDate, LocalDate endDate);
}
