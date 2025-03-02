package com.clockin.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部門DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysDepartmentDTO {

    private Long id;

    @NotBlank(message = "部門名稱不能為空")
    @Size(max = 50, message = "部門名稱長度不能超過50")
    private String deptName;

    @Size(max = 30, message = "部門編碼長度不能超過30")
    private String deptCode;

    private Long parentId;

    private String parentName;

    private Integer orderNum;

    private Long leaderId;

    private String leaderName;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 子部門
     */
    private List<SysDepartmentDTO> children;
}
