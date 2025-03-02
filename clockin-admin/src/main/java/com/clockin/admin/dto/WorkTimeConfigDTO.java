package com.clockin.admin.dto;

import com.clockin.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * 工作時間配置數據傳輸對象
 */
@Data
@Schema(description = "工作時間配置")
public class WorkTimeConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置ID")
    @NotNull(message = "配置ID不能為空", groups = {ValidationGroups.Update.class, ValidationGroups.Delete.class})
    private Long id;

    @Schema(description = "配置名稱", required = true)
    @NotBlank(message = "配置名稱不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String configName;

    @Schema(description = "上班時間", required = true, example = "09:00:00")
    @NotNull(message = "上班時間不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime workStartTime;

    @Schema(description = "下班時間", required = true, example = "18:00:00")
    @NotNull(message = "下班時間不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime workEndTime;

    @Schema(description = "午休開始時間", example = "12:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime lunchStartTime;

    @Schema(description = "午休結束時間", example = "13:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime lunchEndTime;

    @Schema(description = "工作日每天工作時長(小時)", required = true, example = "8")
    @NotNull(message = "每天工作時長不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Min(value = 1, message = "每天工作時長不能小於1小時", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 24, message = "每天工作時長不能大於24小時", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Double dailyWorkHours;

    @Schema(description = "每週工作天數", required = true, example = "5")
    @NotNull(message = "每週工作天數不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Min(value = 1, message = "每週工作天數不能小於1天", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 7, message = "每週工作天數不能大於7天", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer weeklyWorkDays;

    @Schema(description = "是否設置為預設配置", example = "false")
    private Boolean isDefault;

    @Schema(description = "狀態(0-禁用 1-啟用)", example = "1")
    @Min(value = 0, message = "狀態值不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 1, message = "狀態值不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer status;

    @Schema(description = "最遲上班時間(遲到判斷時間)", example = "09:30:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime latestClockInTime;

    @Schema(description = "最早下班時間(早退判斷時間)", example = "17:30:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime earliestClockOutTime;

    @Schema(description = "備註")
    private String remark;
}
