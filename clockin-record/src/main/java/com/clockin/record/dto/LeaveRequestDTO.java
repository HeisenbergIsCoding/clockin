package com.clockin.record.dto;

import com.clockin.record.entity.LeaveRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 請假申請DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {

    private Long id;

    private Long userId;
    
    private String userName;
    
    @NotNull(message = "請假類型不能為空")
    private LeaveRequest.LeaveType leaveType;
    
    @NotNull(message = "開始時間不能為空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @NotNull(message = "結束時間不能為空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    @NotNull(message = "總天數不能為空")
    @Positive(message = "總天數必須為正數")
    private BigDecimal totalDays;
    
    @NotBlank(message = "請假原因不能為空")
    @Size(max = 512, message = "請假原因長度不能超過512")
    private String reason;
    
    private LeaveRequest.RequestStatus status;
    
    private Long approverId;
    
    private String approverName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTime;
    
    @Size(max = 512, message = "審批意見長度不能超過512")
    private String approvalComment;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
