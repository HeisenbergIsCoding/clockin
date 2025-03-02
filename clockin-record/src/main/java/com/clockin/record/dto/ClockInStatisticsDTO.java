package com.clockin.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 打卡統計DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "打卡統計")
public class ClockInStatisticsDTO {

    @Schema(description = "用戶ID")
    private Long userId;

    @Schema(description = "年份", example = "2025")
    private Integer year;

    @Schema(description = "月份", example = "3")
    private Integer month;

    @Schema(description = "出勤天數", example = "22")
    private Integer attendanceDays;

    @Schema(description = "應出勤天數", example = "23")
    private Integer workingDays;

    @Schema(description = "出勤率", example = "95.65")
    private Double attendanceRate;

    @Schema(description = "正常打卡天數", example = "20")
    private Integer normalDays;

    @Schema(description = "遲到次數", example = "1")
    private Integer lateDays;

    @Schema(description = "早退次數", example = "0")
    private Integer earlyLeaveDays;

    @Schema(description = "曠工天數", example = "0")
    private Integer absenceDays;

    @Schema(description = "請假天數", example = "1")
    private Integer leaveDays;

    @Schema(description = "外勤天數", example = "1")
    private Integer outsideWorkDays;

    @Schema(description = "日統計詳情，鍵為日期 (yyyy-MM-dd)，值為當日出勤情況")
    private Map<String, DailyStatistics> dailyDetails;

    /**
     * 日統計詳情
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "日統計詳情")
    public static class DailyStatistics {

        @Schema(description = "日期", example = "2025-03-01")
        private String date;

        @Schema(description = "是否出勤", example = "true")
        private Boolean isAttendance;

        @Schema(description = "缺勤類型: 0-正常, 1-遲到, 2-早退, 3-曠工, 4-請假, 5-外勤", example = "0")
        private Integer absenceType;

        @Schema(description = "上班打卡時間", example = "09:00:00")
        private String clockInTime;

        @Schema(description = "上班打卡狀態: 0-正常, 1-遲到, 2-早退, 3-補卡, 4-異常", example = "0")
        private Integer clockInStatus;

        @Schema(description = "下班打卡時間", example = "18:00:00")
        private String clockOutTime;

        @Schema(description = "下班打卡狀態: 0-正常, 1-遲到, 2-早退, 3-補卡, 4-異常", example = "0")
        private Integer clockOutStatus;

        @Schema(description = "工作時長（分鐘）", example = "540")
        private Integer workDuration;

        @Schema(description = "備註", example = "外出客戶拜訪")
        private String remark;
    }
}
