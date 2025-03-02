package com.clockin.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 系統用戶實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_user")
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用戶名
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密碼
     */
    @Column(nullable = false)
    private String password;

    /**
     * 姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 電子郵件
     */
    @Column(unique = true, length = 100)
    private String email;

    /**
     * 手機號碼
     */
    @Column(length = 20)
    private String phone;

    /**
     * 頭像
     */
    @Column(length = 200)
    private String avatar;

    /**
     * 性別（0女 1男 2未知）
     */
    @Column
    private Integer gender;

    /**
     * 狀態（0停用 1啟用）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 部門ID
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 部門
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private SysDepartment department;

    /**
     * 崗位ID
     */
    @Column(name = "position_id")
    private Long positionId;

    /**
     * 崗位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private SysPosition position;

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
     * 最後登入時間
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最後登入IP
     */
    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;

    /**
     * 角色列表
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<SysRole> roles = new HashSet<>();
}
