package com.clockin.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 系統用戶DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDTO {

    private Long id;

    @NotBlank(message = "用戶名不能為空")
    @Size(min = 3, max = 50, message = "用戶名長度必須在3-50之間")
    private String username;

    @Size(min = 6, max = 100, message = "密碼長度必須在6-100之間")
    private String password;

    @NotBlank(message = "姓名不能為空")
    @Size(max = 50, message = "姓名長度不能超過50")
    private String name;

    @Email(message = "電子郵件格式不正確")
    @Size(max = 100, message = "電子郵件長度不能超過100")
    private String email;

    @Pattern(regexp = "^$|^[0-9]{10,20}$", message = "手機號碼格式不正確")
    private String phone;

    private String avatar;

    private Integer gender;

    private Integer status;

    // 部門相關
    private Long departmentId;
    private String departmentName;

    // 崗位相關
    private Long positionId;
    private String positionName;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;

    // 角色相關
    private Set<Long> roleIds;
    private Set<String> roleNames;
    private Set<String> permissions;
}
