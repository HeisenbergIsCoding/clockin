package com.clockin.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 用戶工作時間配置響應
 */
@Data
@Schema(description = "用戶工作時間配置響應")
public class UserWorkTimeConfigResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "關聯ID")
    private Long id;

    @Schema(description = "用戶ID")
    private Long userId;

    @Schema(description = "用戶名稱")
    private String username;

    @Schema(description = "用戶姓名")
    private String realName;

    @Schema(description = "配置ID")
    private Long configId;

    @Schema(description = "配置名稱")
    private String configName;

    @Schema(description = "上班時間", example = "09:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime workStartTime;

    @Schema(description = "下班時間", example = "18:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime workEndTime;

    @Schema(description = "午休開始時間", example = "12:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime lunchStartTime;

    @Schema(description = "午休結束時間", example = "13:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime lunchEndTime;

    @Schema(description = "工作日每天工作時長(小時)", example = "8")
    private Double dailyWorkHours;

    @Schema(description = "每週工作天數", example = "5")
    private Integer weeklyWorkDays;

    @Schema(description = "生效開始日期", example = "2025-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveStartDate;

    @Schema(description = "生效結束日期", example = "2025-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveEndDate;

    @Schema(description = "是否為當前生效配置")
    private Boolean isCurrent;

    @Schema(description = "最遲上班時間(遲到判斷時間)", example = "09:30:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime latestClockInTime;

    @Schema(description = "最早下班時間(早退判斷時間)", example = "17:30:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime earliestClockOutTime;

    @Schema(description = "配置狀態(0-禁用 1-啟用)")
    private Integer configStatus;

    @Schema(description = "關聯狀態(0-禁用 1-啟用)")
    private Integer status;

    @Schema(description = "創建時間")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate createTime;

    @Schema(description = "備註")
    private String remark;
}
