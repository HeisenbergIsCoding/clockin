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

/**
 * 節假日配置實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "holiday_config")
public class HolidayConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 假日日期
     */
    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate holidayDate;

    /**
     * 假日名稱
     */
    @Column(name = "holiday_name", nullable = false, length = 100)
    private String holidayName;

    /**
     * 假日類型
     */
    @Column(name = "holiday_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private HolidayType holidayType;

    /**
     * 是否工作日
     */
    @Column(name = "is_workday", nullable = false)
    private Boolean isWorkday;

    /**
     * 備註
     */
    @Column(name = "remark", length = 255)
    private String remark;

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
     * 假日類型枚舉
     */
    public enum HolidayType {
        /**
         * 法定節假日
         */
        PUBLIC_HOLIDAY,
        
        /**
         * 公司假日
         */
        COMPANY_HOLIDAY,
        
        /**
         * 週末調整(例如週末需要上班的情況)
         */
        WEEKEND_ADJUSTMENT
    }
}
