package com.clockin.admin.controller;

import com.clockin.admin.entity.Holiday;
import com.clockin.admin.service.HolidayService;
import com.clockin.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * 假日配置控制器
 */
@RestController
@RequestMapping("/api/admin/holiday")
@RequiredArgsConstructor
@Tag(name = "假日配置管理", description = "假日配置相關的API")
public class HolidayController {
    
    private final HolidayService holidayService;
    
    @PostMapping
    @Operation(summary = "創建假日配置", description = "創建新的假日配置")
    public Result<Holiday> create(@Valid @RequestBody Holiday holiday) {
        return Result.success(holidayService.createHoliday(holiday));
    }
    
    @PostMapping("/batch")
    @Operation(summary = "批量創建假日配置", description = "批量創建新的假日配置")
    public Result<List<Holiday>> batchCreate(@Valid @RequestBody List<Holiday> holidays) {
        return Result.success(holidayService.batchCreateHolidays(holidays));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新假日配置", description = "根據ID更新假日配置")
    public Result<Holiday> update(
            @Parameter(description = "假日配置ID") @PathVariable Long id,
            @Valid @RequestBody Holiday holiday) {
        
        holiday.setId(id);
        return Result.success(holidayService.updateHoliday(holiday));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除假日配置", description = "根據ID刪除假日配置")
    public Result<Void> delete(@Parameter(description = "假日配置ID") @PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return Result.success();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "獲取假日配置", description = "根據ID獲取假日配置")
    public Result<Holiday> getById(@Parameter(description = "假日配置ID") @PathVariable Long id) {
        return holidayService.getHolidayById(id)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "假日配置不存在"));
    }
    
    @GetMapping("/date/{date}")
    @Operation(summary = "根據日期獲取假日配置", description = "根據日期獲取假日配置")
    public Result<Holiday> getByDate(
            @Parameter(description = "日期，格式：yyyy-MM-dd") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return holidayService.getHolidayByDate(date)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "該日期沒有假日配置"));
    }
    
    @GetMapping("/check/{date}")
    @Operation(summary = "檢查日期是否為假日", description = "檢查指定日期是否為假日")
    public Result<Boolean> isHoliday(
            @Parameter(description = "日期，格式：yyyy-MM-dd") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return Result.success(holidayService.isHoliday(date));
    }
    
    @GetMapping("/year/{year}")
    @Operation(summary = "獲取指定年份的假日配置", description = "獲取指定年份的所有假日配置")
    public Result<List<Holiday>> getByYear(@Parameter(description = "年份") @PathVariable Integer year) {
        return Result.success(holidayService.getHolidaysByYear(year));
    }
    
    @GetMapping("/year/{year}/month/{month}")
    @Operation(summary = "獲取指定年月的假日配置", description = "獲取指定年月的所有假日配置")
    public Result<List<Holiday>> getByYearAndMonth(
            @Parameter(description = "年份") @PathVariable Integer year,
            @Parameter(description = "月份") @PathVariable Integer month) {
        
        return Result.success(holidayService.getHolidaysByYearAndMonth(year, month));
    }
    
    @GetMapping("/range")
    @Operation(summary = "獲取日期範圍內的假日配置", description = "獲取指定日期範圍內的所有假日配置")
    public Result<List<Holiday>> getByDateRange(
            @Parameter(description = "開始日期，格式：yyyy-MM-dd") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期，格式：yyyy-MM-dd") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return Result.success(holidayService.getHolidaysByDateRange(startDate, endDate));
    }
    
    @GetMapping("/type/{holidayType}/year/{year}")
    @Operation(summary = "獲取指定類型和年份的假日配置", description = "獲取指定類型和年份的所有假日配置")
    public Result<List<Holiday>> getByTypeAndYear(
            @Parameter(description = "假日類型") @PathVariable Integer holidayType,
            @Parameter(description = "年份") @PathVariable Integer year) {
        
        return Result.success(holidayService.getHolidaysByTypeAndYear(holidayType, year));
    }
    
    @PostMapping("/import")
    @Operation(summary = "批量導入假日配置", description = "批量導入假日配置數據")
    public Result<List<Holiday>> batchImport(@Valid @RequestBody List<Holiday> holidays) {
        return Result.success(holidayService.batchImportHolidays(holidays));
    }
    
    @GetMapping("/page")
    @Operation(summary = "分頁獲取假日配置", description = "根據條件分頁獲取假日配置列表")
    public Result<Page<Holiday>> getPage(
            @Parameter(description = "年份") @RequestParam(required = false) Integer year,
            @Parameter(description = "月份") @RequestParam(required = false) Integer month,
            @Parameter(description = "假日類型") @RequestParam(required = false) Integer holidayType,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "holidayDate") String sort,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        return Result.success(holidayService.getHolidaysPage(year, month, holidayType, pageable));
    }
}
