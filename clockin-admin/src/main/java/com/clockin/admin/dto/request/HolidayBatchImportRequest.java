package com.clockin.admin.dto.request;

import com.clockin.admin.dto.HolidayDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 假日批量導入請求
 */
@Data
@Schema(description = "假日批量導入請求")
public class HolidayBatchImportRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "假日列表", required = true)
    @NotEmpty(message = "假日列表不能為空")
    @Valid
    private List<HolidayDTO> holidays;
}
