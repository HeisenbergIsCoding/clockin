package com.clockin.record.controller;

import com.clockin.record.dto.ApiResponse;
import com.clockin.record.dto.HolidayConfigDTO;
import com.clockin.record.entity.HolidayConfig;
import com.clockin.record.exception.BusinessException;
import com.clockin.record.service.HolidayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 節假日配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/holidays")
@Tag(name = "節假日配置", description = "節假日配置相關API")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class HolidayConfigController {

    private final HolidayConfigService holidayConfigService;

    @GetMapping
    @Operation(summary = "查詢節假日列表", description = "根據條件查詢節假日列表")
    public ApiResponse<List<HolidayConfig>> listHolidays(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year,
            @Parameter(description = "月份") @RequestParam(required = false) Integer month,
            @Parameter(description = "開始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<HolidayConfig> results;

        if (year != null && month != null) {
            results = holidayConfigService.findHolidaysByYearAndMonth(year, month);
        } else if (year != null) {
            results = holidayConfigService.findHolidaysByYear(year);
        } else if (startDate != null && endDate != null) {
            results = holidayConfigService.findHolidaysBetween(startDate, endDate);
        } else {
            throw new BusinessException("必須指定查詢條件：年份、年月或日期範圍");
        }

        return ApiResponse.success(results);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查詢節假日詳情", description = "根據ID查詢節假日詳情")
    public ApiResponse<HolidayConfig> getHoliday(@PathVariable Long id) {
        return holidayConfigService.findById(id)
                .map(ApiResponse::success)
                .orElseThrow(() -> new BusinessException("未找到ID為 " + id + " 的節假日配置"));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "查詢指定日期的節假日", description = "根據日期查詢節假日詳情")
    public ApiResponse<HolidayConfig> getHolidayByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return holidayConfigService.findByDate(date)
                .map(ApiResponse::success)
                .orElseThrow(() -> new BusinessException("未找到日期為 " + date + " 的節假日配置"));
    }

    @GetMapping("/isWorkday/{date}")
    @Operation(summary = "判斷指定日期是否為工作日", description = "根據日期判斷是否為工作日")
    public ApiResponse<Boolean> isWorkday(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isWorkday = holidayConfigService.isWorkday(date);
        return ApiResponse.success(isWorkday);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "創建節假日配置", description = "創建新的節假日配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HolidayConfig> createHoliday(@Valid @RequestBody HolidayConfigDTO holidayDTO) {
        HolidayConfig createdHoliday = holidayConfigService.createHolidayConfig(holidayDTO);
        return ApiResponse.success("創建節假日配置成功", createdHoliday);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新節假日配置", description = "根據ID更新節假日配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<HolidayConfig> updateHoliday(
            @PathVariable Long id, @Valid @RequestBody HolidayConfigDTO holidayDTO) {
        HolidayConfig updatedHoliday = holidayConfigService.updateHolidayConfig(id, holidayDTO);
        return ApiResponse.success("更新節假日配置成功", updatedHoliday);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除節假日配置", description = "根據ID刪除節假日配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteHoliday(@PathVariable Long id) {
        holidayConfigService.deleteById(id);
        return ApiResponse.success("刪除節假日配置成功");
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量導入節假日", description = "批量導入節假日配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<HolidayConfig>> batchImportHolidays(
            @Valid @RequestBody List<HolidayConfigDTO> holidays) {
        List<HolidayConfig> importedHolidays = holidayConfigService.batchImportHolidays(holidays);
        return ApiResponse.success(String.format("成功導入 %d 個節假日配置", importedHolidays.size()), importedHolidays);
    }
}
