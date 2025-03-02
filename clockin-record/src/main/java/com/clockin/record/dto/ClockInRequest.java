package com.clockin.record.dto;

import com.clockin.record.enums.ClockInType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 打卡請求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "打卡請求")
public class ClockInRequest {

    @Schema(description = "打卡類型: 1-上班打卡, 2-下班打卡", example = "1", required = true)
    @NotNull(message = "打卡類型不能為空")
    private ClockInType clockType;

    @Schema(description = "打卡日期，格式：yyyy-MM-dd，默認為當天", example = "2025-03-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate clockDate;

    @Schema(description = "打卡時間，格式：HH:mm:ss，默認為當前時間", example = "09:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime clockTime;

    @Schema(description = "打卡位置", example = "公司辦公室")
    private String location;

    @Schema(description = "打卡設備", example = "Web瀏覽器")
    private String device;

    @Schema(description = "備註", example = "外出客戶拜訪")
    private String remark;

    @Schema(description = "是否補卡", example = "false")
    private Boolean isMakeup = false;
}
