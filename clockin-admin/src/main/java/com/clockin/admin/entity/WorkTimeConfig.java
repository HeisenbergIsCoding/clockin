package com.clockin.admin.entity;

import com.clockin.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalTime;

/**
 * 工作時間配置實體
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "work_time_config")
public class WorkTimeConfig extends BaseEntity {

    /**
     * 配置名稱
     */
    @Column(name = "config_name", nullable = false, length = 50)
    private String configName;

    /**
     * 配置描述
     */
    @Column(name = "config_desc", length = 200)
    private String configDesc;

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
     * 標準工作時長（分鐘）
     */
    @Column(name = "standard_work_duration", nullable = false)
    private Integer standardWorkDuration;

    /**
     * 最小工作時長（分鐘）
     */
    @Column(name = "min_work_duration")
    private Integer minWorkDuration;

    /**
     * 最大工作時長（分鐘）
     */
    @Column(name = "max_work_duration")
    private Integer maxWorkDuration;

    /**
     * 彈性工作時間（分鐘）
     */
    @Column(name = "flexible_duration")
    private Integer flexibleDuration;

    /**
     * 是否啟用午休時間
     */
    @Column(name = "enable_lunch_time", nullable = false)
    private Boolean enableLunchTime;

    /**
     * 是否啟用彈性工作時間
     */
    @Column(name = "enable_flexible_time", nullable = false)
    private Boolean enableFlexibleTime;

    /**
     * 是否為預設配置
     */
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    /**
     * 狀態：0-停用，1-啟用
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 備註說明
     */
    @Column(name = "remark", length = 200)
    private String remark;
}
