package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 考勤統計數據傳輸對象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatisticsDTO {

    /**
     * 用戶ID
     */
    private Long userId;

    /**
     * 用戶名稱
     */
    private String userName;

    /**
     * 部門ID
     */
    private Long departmentId;

    /**
     * 部門名稱
     */
    private String departmentName;

    /**
     * 統計開始日期
     */
    private LocalDate startDate;

    /**
     * 統計結束日期
     */
    private LocalDate endDate;

    /**
     * 正常打卡天數
     */
    private Integer normalDays;

    /**
     * 異常打卡天數
     */
    private Integer abnormalDays;

    /**
     * 缺卡天數
     */
    private Integer missingDays;

    /**
     * 工作日天數
     */
    private Integer workingDays;

    /**
     * 休息日天數
     */
    private Integer holidayDays;

    /**
     * 總工作時長（分鐘）
     */
    private Integer totalWorkDuration;

    /**
     * 格式化的總工作時長
     */
    private String totalWorkDurationFormatted;

    /**
     * 日均工作時長（分鐘）
     */
    private Integer averageWorkDuration;

    /**
     * 格式化的日均工作時長
     */
    private String averageWorkDurationFormatted;

    /**
     * 準時到達天數
     */
    private Integer onTimeDays;

    /**
     * 遲到天數
     */
    private Integer lateDays;

    /**
     * 早退天數
     */
    private Integer earlyLeaveDays;

    /**
     * 加班天數
     */
    private Integer overtimeDays;

    /**
     * 加班總時長（分鐘）
     */
    private Integer overtimeDuration;

    /**
     * 格式化的加班總時長
     */
    private String overtimeDurationFormatted;
}
