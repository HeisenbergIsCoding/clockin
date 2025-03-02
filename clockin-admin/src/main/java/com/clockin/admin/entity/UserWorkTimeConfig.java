package com.clockin.admin.entity;

import com.clockin.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 用戶工作時間配置關聯實體
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "user_work_time_config")
public class UserWorkTimeConfig extends BaseEntity {

    /**
     * 用戶ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 工作時間配置ID
     */
    @Column(name = "config_id", nullable = false)
    private Long configId;

    /**
     * 生效開始日期
     */
    @Column(name = "effective_start_date", nullable = false)
    private LocalDate effectiveStartDate;

    /**
     * 生效結束日期
     */
    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    /**
     * 是否為當前生效配置
     */
    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent;

    /**
     * 狀態：0-停用，1-啟用
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 備註
     */
    @Column(name = "remark", length = 200)
    private String remark;
}
