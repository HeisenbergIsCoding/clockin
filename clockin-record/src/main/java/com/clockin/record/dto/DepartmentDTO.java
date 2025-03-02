package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部門DTO（供其他模組使用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private Long id;
    
    private String deptName;
    
    private String deptCode;
    
    private Long parentId;
    
    private String parentName;
    
    private Integer orderNum;
    
    private Long leaderId;
    
    private String leaderName;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private List<DepartmentDTO> children;
}
