package com.clockin.admin.dto;

import com.clockin.common.validation.ValidationGroups;
import com.clockin.common.validation.annotation.DateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用戶工作時間配置關聯數據傳輸對象
 */
@Data
@Schema(description = "用戶工作時間配置關聯")
@DateRange(startDate = "effectiveStartDate", endDate = "effectiveEndDate", message = "結束日期必須在開始日期之後或相等")
public class UserWorkTimeConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "關聯ID")
    @NotNull(message = "關聯ID不能為空", groups = {ValidationGroups.Update.class, ValidationGroups.Delete.class})
    private Long id;

    @Schema(description = "用戶ID", required = true)
    @NotNull(message = "用戶ID不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Long userId;

    @Schema(description = "配置ID", required = true)
    @NotNull(message = "配置ID不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Long configId;

    @Schema(description = "生效開始日期", required = true, example = "2025-01-01")
    @NotNull(message = "生效開始日期不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveStartDate;

    @Schema(description = "生效結束日期", example = "2025-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveEndDate;

    @Schema(description = "是否為當前生效配置", example = "false")
    private Boolean isCurrent;

    @Schema(description = "狀態(0-禁用 1-啟用)", example = "1")
    @Min(value = 0, message = "狀態值不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 1, message = "狀態值不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer status;

    @Schema(description = "備註")
    private String remark;
}
