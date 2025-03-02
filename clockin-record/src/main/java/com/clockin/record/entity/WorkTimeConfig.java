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
 * 工作時間配置實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_time_config")
public class WorkTimeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用戶ID，為NULL表示全局配置
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 部門ID，為NULL表示全局配置
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 上午上班時間
     */
    @Column(name = "morning_start_time", nullable = false)
    private LocalTime morningStartTime;

    /**
     * 上午下班時間
     */
    @Column(name = "morning_end_time", nullable = false)
    private LocalTime morningEndTime;

    /**
     * 下午上班時間
     */
    @Column(name = "afternoon_start_time", nullable = false)
    private LocalTime afternoonStartTime;

    /**
     * 下午下班時間
     */
    @Column(name = "afternoon_end_time", nullable = false)
    private LocalTime afternoonEndTime;

    /**
     * 彈性時間(分鐘)
     */
    @Column(name = "flexible_minutes", nullable = false)
    private Integer flexibleMinutes;

    /**
     * 生效日期
     */
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    /**
     * 過期日期，為NULL表示永久有效
     */
    @Column(name = "expired_date")
    private LocalDate expiredDate;

    /**
     * 是否啟用
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * 優先級，數字越大優先級越高
     */
    @Column(name = "priority", nullable = false)
    private Integer priority;

    /**
     * 創建時間
     */
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新時間
     */
    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    /**
     * 檢查配置在指定日期是否有效
     *
     * @param date 指定日期
     * @return 是否有效
     */
    public boolean isEffectiveAt(LocalDate date) {
        return isActive && 
               !date.isBefore(effectiveDate) && 
               (expiredDate == null || !date.isAfter(expiredDate));
    }
}
