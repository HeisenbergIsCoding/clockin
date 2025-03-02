package com.clockin.record.controller;

import com.clockin.common.entity.Result;
import com.clockin.record.entity.ClockRecord;
import com.clockin.record.service.ClockRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 打卡記錄控制器
 */
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
@Tag(name = "打卡記錄管理", description = "打卡記錄相關API")
public class ClockRecordController {
    
    private final ClockRecordService clockRecordService;
    
    @PostMapping("/clock-in")
    @Operation(summary = "上班打卡", description = "用戶上班打卡接口")
    public Result<ClockRecord> clockIn(@RequestParam Long userId,
                                      @RequestParam(required = false) 
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime clockInTime,
                                      @RequestParam(required = false) String workLocation,
                                      @RequestParam(required = false) String ipAddress,
                                      @RequestParam(required = false) String deviceInfo,
                                      @RequestParam(required = false) String remark) {
        
        // 如果未提供打卡時間，則使用當前時間
        if (clockInTime == null) {
            clockInTime = LocalDateTime.now();
        }
        
        ClockRecord record = clockRecordService.clockIn(userId, clockInTime, workLocation, ipAddress, deviceInfo, remark);
        return Result.success(record);
    }
    
    @PostMapping("/clock-out")
    @Operation(summary = "下班打卡", description = "用戶下班打卡接口")
    public Result<ClockRecord> clockOut(@RequestParam Long userId,
                                       @RequestParam(required = false) 
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime clockOutTime,
                                       @RequestParam(required = false) String workLocation,
                                       @RequestParam(required = false) String ipAddress,
                                       @RequestParam(required = false) String deviceInfo,
                                       @RequestParam(required = false) String remark) {
        
        // 如果未提供打卡時間，則使用當前時間
        if (clockOutTime == null) {
            clockOutTime = LocalDateTime.now();
        }
        
        ClockRecord record = clockRecordService.clockOut(userId, clockOutTime, workLocation, ipAddress, deviceInfo, remark);
        return Result.success(record);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "獲取打卡記錄", description = "根據ID獲取打卡記錄")
    public Result<ClockRecord> getRecordById(@PathVariable Long id) {
        return clockRecordService.getRecordById(id)
                .map(Result::success)
                .orElse(Result.fail("打卡記錄不存在"));
    }
    
    @GetMapping("/user/{userId}/date/{date}")
    @Operation(summary = "獲取用戶指定日期打卡記錄", description = "根據用戶ID和日期獲取打卡記錄")
    public Result<ClockRecord> getRecordByUserAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        
        return clockRecordService.getRecordByUserAndDate(userId, date)
                .map(Result::success)
                .orElse(Result.fail("打卡記錄不存在"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新打卡記錄", description = "更新指定ID的打卡記錄")
    public Result<ClockRecord> updateRecord(@PathVariable Long id, @RequestBody ClockRecord record) {
        if (!id.equals(record.getId())) {
            return Result.fail("ID不匹配");
        }
        
        return Result.success(clockRecordService.updateRecord(record));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除打卡記錄", description = "刪除指定ID的打卡記錄")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        clockRecordService.deleteRecord(id);
        return Result.success();
    }
    
    @GetMapping("/user/{userId}/range")
    @Operation(summary = "獲取用戶日期範圍內打卡記錄", description = "根據用戶ID和日期範圍獲取打卡記錄")
    public Result<List<ClockRecord>> getRecordsByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        return Result.success(clockRecordService.getRecordsByUserAndDateRange(userId, startDate, endDate));
    }
    
    @GetMapping("/page")
    @Operation(summary = "分頁查詢打卡記錄", description = "分頁查詢打卡記錄，支持多條件查詢")
    public Result<Page<ClockRecord>> getRecordsPage(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) Boolean isAbnormal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "clockDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return Result.success(clockRecordService.getRecordsPage(userId, date, isAbnormal, pageable));
    }
    
    @PostMapping("/batch-import")
    @Operation(summary = "批量導入打卡記錄", description = "批量導入打卡記錄")
    public Result<List<ClockRecord>> batchImportRecords(@RequestBody List<ClockRecord> records) {
        return Result.success(clockRecordService.batchImportRecords(records));
    }
    
    @GetMapping("/user/{userId}/work-duration")
    @Operation(summary = "統計用戶工作時長", description = "統計用戶指定日期範圍內的工作總時長")
    public Result<Integer> getTotalWorkDuration(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        return Result.success(clockRecordService.getTotalWorkDuration(userId, startDate, endDate));
    }
    
    @GetMapping("/user/{userId}/latest")
    @Operation(summary = "獲取用戶最近打卡記錄", description = "獲取用戶最近一次打卡記錄")
    public Result<ClockRecord> getLatestRecord(@PathVariable Long userId) {
        return clockRecordService.getLatestRecord(userId)
                .map(Result::success)
                .orElse(Result.fail("無打卡記錄"));
    }
    
    @GetMapping("/today")
    @Operation(summary = "獲取今日所有打卡記錄", description = "獲取當日所有用戶的打卡記錄")
    public Result<List<ClockRecord>> getTodayRecords() {
        return Result.success(clockRecordService.getTodayRecords());
    }
    
    @GetMapping("/abnormal")
    @Operation(summary = "獲取異常打卡記錄", description = "獲取指定日期範圍內所有異常打卡記錄")
    public Result<List<ClockRecord>> getAbnormalRecords(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        return Result.success(clockRecordService.getAbnormalRecords(startDate, endDate));
    }
}
