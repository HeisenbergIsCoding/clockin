package com.clockin.record.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 考勤規則實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_attendance_rule")
@EntityListeners(AuditingEntityListener.class)
public class AttendanceRule {

    /**
     * 主鍵ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 規則名稱
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 上班時間
     */
    @Column(name = "work_start_time", nullable = false)
    private LocalTime workStartTime;

    /**
     * 下班時間
     */
    @Column(name = "work_end_time", nullable = false)
    private LocalTime workEndTime;

    /**
     * 彈性時間（分鐘）
     */
    @Column(name = "flexible_time", nullable = false)
    private Integer flexibleTime;

    /**
     * 是否啟用彈性工作時間
     */
    @Column(name = "enable_flexible_time", nullable = false)
    private Boolean enableFlexibleTime;

    /**
     * 午休開始時間
     */
    @Column(name = "lunch_start_time")
    private LocalTime lunchStartTime;

    /**
     * 午休結束時間
     */
    @Column(name = "lunch_end_time")
    private LocalTime lunchEndTime;

    /**
     * 是否啟用午休時間
     */
    @Column(name = "enable_lunch_time", nullable = false)
    private Boolean enableLunchTime;

    /**
     * 遲到閾值（分鐘）
     */
    @Column(name = "late_threshold", nullable = false)
    private Integer lateThreshold;

    /**
     * 早退閾值（分鐘）
     */
    @Column(name = "early_leave_threshold", nullable = false)
    private Integer earlyLeaveThreshold;

    /**
     * 加班閾值（分鐘）
     */
    @Column(name = "overtime_threshold", nullable = false)
    private Integer overtimeThreshold;

    /**
     * 工作時長（分鐘）
     */
    @Column(name = "work_duration", nullable = false)
    private Integer workDuration;

    /**
     * 是否考勤特殊節假日
     */
    @Column(name = "check_holidays", nullable = false)
    private Boolean checkHolidays;

    /**
     * 是否啟用外勤打卡
     */
    @Column(name = "enable_out_working", nullable = false)
    private Boolean enableOutWorking;

    /**
     * 是否啟用拍照打卡
     */
    @Column(name = "enable_photo_clock", nullable = false)
    private Boolean enablePhotoClockIn;

    /**
     * 是否啟用WiFi打卡驗證
     */
    @Column(name = "enable_wifi_validation", nullable = false)
    private Boolean enableWifiValidation;

    /**
     * 是否啟用位置打卡驗證
     */
    @Column(name = "enable_location_validation", nullable = false)
    private Boolean enableLocationValidation;

    /**
     * 規則描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 部門ID（NULL表示全公司通用規則）
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 創建時間
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 創建者
     */
    @CreatedBy
    @Column(name = "create_by", updatable = false)
    private String createBy;

    /**
     * 更新時間
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 備註
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 是否啟用
     */
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    /**
     * 刪除標記（0代表存在 1代表刪除）
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * 優先級（數字越小優先級越高）
     */
    @Column(name = "priority", nullable = false)
    private Integer priority;
}
