package com.clockin.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 崗位實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_position")
public class SysPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 崗位名稱
     */
    @Column(name = "position_name", nullable = false, length = 50)
    private String positionName;

    /**
     * 崗位編碼
     */
    @Column(name = "position_code", unique = true, length = 30)
    private String positionCode;

    /**
     * 顯示順序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 崗位狀態（0停用 1正常）
     */
    @Column(nullable = false)
    private Integer status;

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
}
