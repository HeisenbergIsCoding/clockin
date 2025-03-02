package com.clockin.record.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 工作時間配置 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeConfigDTO {

    private Long id;

    private Long userId;

    private Long departmentId;

    @NotNull(message = "上午上班時間不能為空")
    private LocalTime morningStartTime;

    @NotNull(message = "上午下班時間不能為空")
    private LocalTime morningEndTime;

    @NotNull(message = "下午上班時間不能為空")
    private LocalTime afternoonStartTime;

    @NotNull(message = "下午下班時間不能為空")
    private LocalTime afternoonEndTime;

    @NotNull(message = "彈性時間不能為空")
    @Positive(message = "彈性時間必須大於0")
    private Integer flexibleMinutes;

    @NotNull(message = "生效日期不能為空")
    private LocalDate effectiveDate;

    private LocalDate expiredDate;

    @NotNull(message = "是否啟用不能為空")
    private Boolean isActive;

    @NotNull(message = "優先級不能為空")
    private Integer priority;
}
