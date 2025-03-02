package com.clockin.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 移動端打卡請求數據傳輸對象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "移動端打卡請求數據")
public class MobileClockInRequestDTO {

    /**
     * 經度
     */
    @NotNull(message = "經度不能為空")
    @Schema(description = "經度", example = "116.403963")
    private Double longitude;

    /**
     * 緯度
     */
    @NotNull(message = "緯度不能為空")
    @Schema(description = "緯度", example = "39.915119")
    private Double latitude;

    /**
     * 位置描述
     */
    @Schema(description = "位置描述", example = "北京市東城區長安街1號")
    private String locationDescription;

    /**
     * 打卡類型：1-上班打卡，2-下班打卡
     */
    @NotNull(message = "打卡類型不能為空")
    @Schema(description = "打卡類型：1-上班打卡，2-下班打卡", example = "1")
    private Integer clockType;

    /**
     * 設備類型：1-iOS, 2-Android, 3-其他
     */
    @Schema(description = "設備類型：1-iOS, 2-Android, 3-其他", example = "2")
    private Integer deviceType;

    /**
     * 設備識別碼
     */
    @Schema(description = "設備識別碼", example = "AABBCC112233")
    private String deviceId;

    /**
     * 照片URL（可選，拍照打卡時使用）
     */
    @Schema(description = "照片URL", example = "http://example.com/photos/123.jpg")
    private String photoUrl;

    /**
     * 備註信息
     */
    @Schema(description = "備註信息", example = "外勤打卡")
    private String remark;

    /**
     * 網絡類型：1-WiFi, 2-移動數據, 3-其他
     */
    @Schema(description = "網絡類型：1-WiFi, 2-移動數據, 3-其他", example = "1")
    private Integer networkType;

    /**
     * WiFi SSID（WiFi打卡時使用）
     */
    @Schema(description = "WiFi SSID", example = "Company_WiFi")
    private String wifiSsid;

    /**
     * WiFi MAC地址（WiFi打卡時使用）
     */
    @Schema(description = "WiFi MAC地址", example = "00:11:22:33:44:55")
    private String wifiMac;

    /**
     * 是否為外勤打卡
     */
    @Schema(description = "是否為外勤打卡", example = "false")
    private Boolean isOutWorking = false;

    /**
     * 外勤原因（外勤打卡時使用）
     */
    @Schema(description = "外勤原因", example = "客戶拜訪")
    private String outWorkingReason;

    /**
     * APP版本號
     */
    @Schema(description = "APP版本號", example = "1.0.0")
    private String appVersion;
}
