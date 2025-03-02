package com.clockin.record.entity;

import com.clockin.record.enums.ClockInStatus;
import com.clockin.record.enums.ClockInType;
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
 * 打卡記錄實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clock_in_record")
public class ClockInRecord {

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
     * 打卡時間
     */
    @Column(name = "clock_time", nullable = false)
    private LocalTime clockTime;

    /**
     * 打卡類型：1-上班打卡，2-下班打卡
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "clock_type", nullable = false)
    private ClockInType clockType;

    /**
     * 打卡狀態：0-正常，1-遲到，2-早退，3-補卡，4-異常
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private ClockInStatus status;

    /**
     * 打卡位置
     */
    @Column(name = "location", length = 255)
    private String location;

    /**
     * 打卡設備
     */
    @Column(name = "device", length = 100)
    private String device;

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
