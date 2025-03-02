package com.clockin.record.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 打卡匯總實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clock_in_summary")
public class ClockInSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private LocalTime clockInTime;

    /**
     * 上班打卡狀態：0-正常，1-遲到，2-早退，3-補卡，4-異常
     */
    @Column(name = "clock_in_status")
    private Integer clockInStatus;

    /**
     * 下班打卡時間
     */
    @Column(name = "clock_out_time")
    private LocalTime clockOutTime;

    /**
     * 下班打卡狀態：0-正常，1-遲到，2-早退，3-補卡，4-異常
     */
    @Column(name = "clock_out_status")
    private Integer clockOutStatus;

    /**
     * 工作時長（分鐘）
     */
    @Column(name = "work_duration")
    private Integer workDuration;

    /**
     * 缺勤類型：0-正常，1-遲到，2-早退，3-曠工，4-請假，5-外勤
     */
    @Column(name = "absence_type", nullable = false)
    private Integer absenceType;

    /**
     * 備註
     */
    @Column(length = 255)
    private String remark;

    /**
     * 創建時間
     */
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否刪除：0-未刪除，1-已刪除
     */
    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;
}
