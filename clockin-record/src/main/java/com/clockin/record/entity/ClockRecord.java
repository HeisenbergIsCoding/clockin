package com.clockin.record.entity;

import com.clockin.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 打卡記錄實體
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "clock_record")
public class ClockRecord extends BaseEntity {

    /**
     * 用戶ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 打卡日期
     */
    @Column(name = "clock_date", nullable = false)
    private LocalDate clockDate;

    /**
     * 上班打卡時間
     */
    @Column(name = "clock_in_time")
    private LocalDateTime clockInTime;

    /**
     * 下班打卡時間
     */
    @Column(name = "clock_out_time")
    private LocalDateTime clockOutTime;

    /**
     * 打卡類型：1-手動打卡，2-自動打卡，3-系統修正
     */
    @Column(name = "clock_type")
    private Integer clockType;

    /**
     * 工作地點
     */
    @Column(name = "work_location", length = 100)
    private String workLocation;

    /**
     * IP 地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * 設備信息
     */
    @Column(name = "device_info", length = 200)
    private String deviceInfo;

    /**
     * 備註
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 是否異常：0-正常，1-異常
     */
    @Column(name = "is_abnormal")
    private Boolean isAbnormal;

    /**
     * 異常原因
     */
    @Column(name = "abnormal_reason", length = 500)
    private String abnormalReason;

    /**
     * 工作時長（分鐘）
     */
    @Column(name = "work_duration")
    private Integer workDuration;

    /**
     * 是否休假日：0-否，1-是
     */
    @Column(name = "is_holiday")
    private Boolean isHoliday;

    /**
     * 休假類型：1-法定假日，2-公司假日，3-個人假日
     */
    @Column(name = "holiday_type")
    private Integer holidayType;
}
