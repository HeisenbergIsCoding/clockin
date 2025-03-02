package com.clockin.admin.dto;

import com.clockin.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 假日配置數據傳輸對象
 */
@Data
@Schema(description = "假日配置")
public class HolidayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "假日ID")
    @NotNull(message = "假日ID不能為空", groups = {ValidationGroups.Update.class, ValidationGroups.Delete.class})
    private Long id;

    @Schema(description = "假日日期", required = true, example = "2025-01-01")
    @NotNull(message = "假日日期不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate holidayDate;

    @Schema(description = "假日名稱", required = true, example = "元旦")
    @NotNull(message = "假日名稱不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String holidayName;

    @Schema(description = "假日類型(1-法定節假日 2-公司假日 3-調休日 4-工作日)", required = true, example = "1")
    @NotNull(message = "假日類型不能為空", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Min(value = 1, message = "假日類型不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 4, message = "假日類型不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer holidayType;

    @Schema(description = "年份", example = "2025")
    private Integer year;

    @Schema(description = "月份", example = "1")
    private Integer month;

    @Schema(description = "日", example = "1")
    private Integer day;

    @Schema(description = "狀態(0-禁用 1-啟用)", example = "1")
    @Min(value = 0, message = "狀態值不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 1, message = "狀態值不正確", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer status;

    @Schema(description = "備註")
    private String remark;
}
