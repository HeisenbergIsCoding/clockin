package com.clockin.record.dto;

import com.clockin.record.entity.HolidayConfig;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 節假日配置 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayConfigDTO {

    private Long id;

    @NotNull(message = "假日日期不能為空")
    private LocalDate holidayDate;

    @NotEmpty(message = "假日名稱不能為空")
    private String holidayName;

    @NotNull(message = "假日類型不能為空")
    private HolidayConfig.HolidayType holidayType;

    @NotNull(message = "是否工作日不能為空")
    private Boolean isWorkday;

    private String remark;
}
