package com.clockin.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 崗位DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPositionDTO {

    private Long id;

    @NotBlank(message = "崗位名稱不能為空")
    @Size(max = 50, message = "崗位名稱長度不能超過50")
    private String positionName;

    @Size(max = 30, message = "崗位編碼長度不能超過30")
    private String positionCode;

    private Integer orderNum;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
