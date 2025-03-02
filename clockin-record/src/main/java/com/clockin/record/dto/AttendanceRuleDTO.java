package com.clockin.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 考勤規則數據傳輸對象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "考勤規則數據")
public class AttendanceRuleDTO {

    /**
     * 主鍵ID
     */
    @Schema(description = "規則ID")
    private Long id;

    /**
     * 規則名稱
     */
    @NotBlank(message = "規則名稱不能為空")
    @Schema(description = "規則名稱", example = "標準上班制")
    private String name;

    /**
     * 上班時間
     */
    @NotNull(message = "上班時間不能為空")
    @Schema(description = "上班時間", example = "09:00:00")
    private LocalTime workStartTime;

    /**
     * 下班時間
     */
    @NotNull(message = "下班時間不能為空")
    @Schema(description = "下班時間", example = "18:00:00")
    private LocalTime workEndTime;

    /**
     * 彈性時間（分鐘）
     */
    @Schema(description = "彈性時間（分鐘）", example = "30")
    private Integer flexibleTime;

    /**
     * 是否啟用彈性工作時間
     */
    @Schema(description = "是否啟用彈性工作時間", example = "true")
    private Boolean enableFlexibleTime;

    /**
     * 午休開始時間
     */
    @Schema(description = "午休開始時間", example = "12:00:00")
    private LocalTime lunchStartTime;

    /**
     * 午休結束時間
     */
    @Schema(description = "午休結束時間", example = "13:30:00")
    private LocalTime lunchEndTime;

    /**
     * 是否啟用午休時間
     */
    @Schema(description = "是否啟用午休時間", example = "true")
    private Boolean enableLunchTime;

    /**
     * 遲到閾值（分鐘）
     */
    @NotNull(message = "遲到閾值不能為空")
    @Schema(description = "遲到閾值（分鐘）", example = "15")
    private Integer lateThreshold;

    /**
     * 早退閾值（分鐘）
     */
    @NotNull(message = "早退閾值不能為空")
    @Schema(description = "早退閾值（分鐘）", example = "15")
    private Integer earlyLeaveThreshold;

    /**
     * 加班閾值（分鐘）
     */
    @NotNull(message = "加班閾值不能為空")
    @Schema(description = "加班閾值（分鐘）", example = "60")
    private Integer overtimeThreshold;

    /**
     * 工作時長（分鐘）
     */
    @NotNull(message = "工作時長不能為空")
    @Schema(description = "工作時長（分鐘）", example = "480")
    private Integer workDuration;

    /**
     * 是否考勤特殊節假日
     */
    @Schema(description = "是否考勤特殊節假日", example = "true")
    private Boolean checkHolidays;

    /**
     * 是否啟用外勤打卡
     */
    @Schema(description = "是否啟用外勤打卡", example = "true")
    private Boolean enableOutWorking;

    /**
     * 是否啟用拍照打卡
     */
    @Schema(description = "是否啟用拍照打卡", example = "false")
    private Boolean enablePhotoClockIn;

    /**
     * 是否啟用WiFi打卡驗證
     */
    @Schema(description = "是否啟用WiFi打卡驗證", example = "true")
    private Boolean enableWifiValidation;

    /**
     * 是否啟用位置打卡驗證
     */
    @Schema(description = "是否啟用位置打卡驗證", example = "true")
    private Boolean enableLocationValidation;

    /**
     * 規則描述
     */
    @Schema(description = "規則描述", example = "標準9點至18點工作制")
    private String description;

    /**
     * 部門ID（NULL表示全公司通用規則）
     */
    @Schema(description = "部門ID", example = "1")
    private Long departmentId;

    /**
     * 部門名稱
     */
    @Schema(description = "部門名稱", example = "技術部")
    private String departmentName;

    /**
     * 創建時間
     */
    @Schema(description = "創建時間")
    private LocalDateTime createTime;

    /**
     * 創建者
     */
    @Schema(description = "創建者", example = "admin")
    private String createBy;

    /**
     * 更新時間
     */
    @Schema(description = "更新時間")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @Schema(description = "更新者", example = "admin")
    private String updateBy;

    /**
     * 備註
     */
    @Schema(description = "備註", example = "適用於標準辦公人員")
    private String remark;

    /**
     * 是否啟用
     */
    @Schema(description = "是否啟用", example = "true")
    private Boolean isEnabled = true;

    /**
     * 優先級（數字越小優先級越高）
     */
    @NotNull(message = "優先級不能為空")
    @Schema(description = "優先級（數字越小優先級越高）", example = "1")
    private Integer priority;
}
