package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 系統用戶DTO（供record模組使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDTO {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer status;
    
    // 部門相關
    private Long departmentId;
    private String departmentName;
    private String departmentCode;
    
    // 崗位相關
    private Long positionId;
    private String positionName;
    private String positionCode;
    
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
    
    // 角色和權限相關
    private Set<String> roles;
    private Set<String> permissions;
}
