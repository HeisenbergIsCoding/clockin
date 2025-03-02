package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 打卡記錄數據傳輸對象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClockRecordDTO {

    /**
     * 記錄ID
     */
    private Long id;

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
     * 打卡日期
     */
    private LocalDate clockDate;

    /**
     * 上班打卡時間
     */
    private LocalDateTime clockInTime;

    /**
     * 下班打卡時間
     */
    private LocalDateTime clockOutTime;

    /**
     * 打卡類型：1-手動打卡，2-自動打卡，3-系統修正
     */
    private Integer clockType;

    /**
     * 打卡類型描述
     */
    private String clockTypeDesc;

    /**
     * 工作地點
     */
    private String workLocation;

    /**
     * IP 地址
     */
    private String ipAddress;

    /**
     * 設備信息
     */
    private String deviceInfo;

    /**
     * 備註
     */
    private String remark;

    /**
     * 是否異常
     */
    private Boolean isAbnormal;

    /**
     * 異常原因
     */
    private String abnormalReason;

    /**
     * 工作時長（分鐘）
     */
    private Integer workDuration;

    /**
     * 工作時長（格式化）
     */
    private String workDurationFormatted;

    /**
     * 是否休假日
     */
    private Boolean isHoliday;

    /**
     * 休假類型：1-法定假日，2-公司假日，3-個人假日
     */
    private Integer holidayType;

    /**
     * 休假類型描述
     */
    private String holidayTypeDesc;

    /**
     * 創建時間
     */
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;
}
