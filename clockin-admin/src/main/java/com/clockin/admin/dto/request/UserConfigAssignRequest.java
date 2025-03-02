package com.clockin.admin.dto.request;

import com.clockin.common.validation.ValidationGroups;
import com.clockin.common.validation.annotation.DateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用戶配置分配請求
 */
@Data
@Schema(description = "用戶配置分配請求")
@DateRange(startDate = "startDate", endDate = "endDate", message = "結束日期必須在開始日期之後或相等")
public class UserConfigAssignRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用戶ID", required = true)
    @NotNull(message = "用戶ID不能為空", groups = {ValidationGroups.Create.class})
    private Long userId;

    @Schema(description = "配置ID", required = true)
    @NotNull(message = "配置ID不能為空", groups = {ValidationGroups.Create.class})
    private Long configId;

    @Schema(description = "生效開始日期", required = true, example = "2025-01-01")
    @NotNull(message = "生效開始日期不能為空", groups = {ValidationGroups.Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "生效結束日期", example = "2025-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "是否為當前生效配置", example = "false")
    private Boolean isCurrent;

    @Schema(description = "狀態(0-禁用 1-啟用)", example = "1")
    private Integer status;

    @Schema(description = "備註")
    private String remark;
}
