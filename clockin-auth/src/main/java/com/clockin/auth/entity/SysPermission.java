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
 * 系統權限實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_permission")
public class SysPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 權限名稱
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 權限編碼
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 權限描述
     */
    @Column(length = 255)
    private String description;

    /**
     * 權限類型：1-菜單，2-按鈕，3-API
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 父權限ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 權限路徑
     */
    @Column(length = 255)
    private String path;

    /**
     * 排序號
     */
    @Column
    private Integer sort;

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
