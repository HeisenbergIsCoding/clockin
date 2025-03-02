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
 * 假日配置實體
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "holiday")
public class Holiday extends BaseEntity {

    /**
     * 假日名稱
     */
    @Column(name = "holiday_name", nullable = false, length = 50)
    private String holidayName;

    /**
     * 假日日期
     */
    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    /**
     * 假日類型：1-法定假日，2-公司假日，3-特殊工作日
     */
    @Column(name = "holiday_type", nullable = false)
    private Integer holidayType;

    /**
     * 假日年份
     */
    @Column(name = "year", nullable = false)
    private Integer year;

    /**
     * 假日月份
     */
    @Column(name = "month", nullable = false)
    private Integer month;

    /**
     * 假日日期（日）
     */
    @Column(name = "day", nullable = false)
    private Integer day;

    /**
     * 假日描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 狀態：0-停用，1-啟用
     */
    @Column(name = "status", nullable = false)
    private Integer status;
}
