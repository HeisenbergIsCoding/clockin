package com.clockin.record.dto;

import com.clockin.record.enums.ClockInStatus;
import com.clockin.record.enums.ClockInType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 打卡響應DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "打卡響應")
public class ClockInResponse {

    @Schema(description = "打卡記錄ID")
    private Long id;

    @Schema(description = "用戶ID")
    private Long userId;

    @Schema(description = "打卡日期", example = "2025-03-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate clockDate;

    @Schema(description = "打卡時間", example = "09:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime clockTime;

    @Schema(description = "打卡類型: 1-上班打卡, 2-下班打卡", example = "1")
    private ClockInType clockType;

    @Schema(description = "打卡狀態: 0-正常, 1-遲到, 2-早退, 3-補卡, 4-異常", example = "0")
    private ClockInStatus status;

    @Schema(description = "打卡位置", example = "公司辦公室")
    private String location;

    @Schema(description = "打卡設備", example = "Web瀏覽器")
    private String device;

    @Schema(description = "備註", example = "外出客戶拜訪")
    private String remark;

    @Schema(description = "是否是第一次打卡", example = "true")
    private Boolean isFirstClockIn;

    @Schema(description = "標準上班時間", example = "09:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime standardClockInTime;

    @Schema(description = "標準下班時間", example = "18:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime standardClockOutTime;

    @Schema(description = "提示消息", example = "打卡成功")
    private String message;
}
