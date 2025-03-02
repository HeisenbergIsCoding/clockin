package com.clockin.record.dto;

import com.clockin.record.entity.MakeupClockInRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 補卡申請DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MakeupClockInRequestDTO {

    private Long id;

    private Long userId;
    
    private String userName;
    
    @NotNull(message = "申請日期不能為空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate;
    
    @NotNull(message = "補卡時間不能為空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;
    
    @NotBlank(message = "打卡類型不能為空")
    @Size(max = 20, message = "打卡類型長度不能超過20")
    private String clockInType;
    
    @NotBlank(message = "補卡原因不能為空")
    @Size(max = 512, message = "補卡原因長度不能超過512")
    private String reason;
    
    private MakeupClockInRequest.RequestStatus status;
    
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
