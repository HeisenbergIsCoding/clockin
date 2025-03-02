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
 * 部門實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_department")
public class SysDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 部門名稱
     */
    @Column(name = "dept_name", nullable = false, length = 50)
    private String deptName;

    /**
     * 部門編碼
     */
    @Column(name = "dept_code", unique = true, length = 30)
    private String deptCode;

    /**
     * 父部門ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 顯示順序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 負責人ID
     */
    @Column(name = "leader_id")
    private Long leaderId;

    /**
     * 負責人姓名
     */
    @Column(name = "leader_name", length = 50)
    private String leaderName;

    /**
     * 部門狀態（0停用 1正常）
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
