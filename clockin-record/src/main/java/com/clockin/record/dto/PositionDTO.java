package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 崗位DTO（供其他模組使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDTO {

    private Long id;
    
    private String positionName;
    
    private String positionCode;
    
    private Integer orderNum;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
