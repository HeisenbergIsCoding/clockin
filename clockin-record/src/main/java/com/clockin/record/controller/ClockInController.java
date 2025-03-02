package com.clockin.record.controller;

import com.clockin.common.util.ResponseResult;
import com.clockin.record.dto.ClockInRequest;
import com.clockin.record.dto.ClockInResponse;
import com.clockin.record.dto.ClockInStatisticsDTO;
import com.clockin.record.entity.ClockInRecord;
import com.clockin.record.entity.ClockInSummary;
import com.clockin.record.service.ClockInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 打卡控制器
 */
@Slf4j
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
@Tag(name = "打卡記錄", description = "打卡相關接口")
public class ClockInController {

    private final ClockInService clockInService;

    /**
     * 用戶打卡
     */
    @PostMapping("/clock")
    @Operation(summary = "用戶打卡", description = "用戶上下班打卡")
    public ResponseResult<ClockInResponse> clockIn(@Valid @RequestBody ClockInRequest request) {
        Long userId = getCurrentUserId();
        ClockInResponse response = clockInService.clockIn(userId, request);
        return ResponseResult.success(response);
    }

    /**
     * 補卡申請
     */
    @PostMapping("/makeup")
    @Operation(summary = "補卡申請", description = "用戶申請補卡")
    public ResponseResult<ClockInResponse> applyForMakeup(@Valid @RequestBody ClockInRequest request) {
        Long userId = getCurrentUserId();
        ClockInResponse response = clockInService.applyForMakeup(userId, request);
        return ResponseResult.success(response);
    }

    /**
     * 獲取當日打卡記錄
     */
    @GetMapping("/today")
    @Operation(summary = "獲取當日打卡記錄", description = "獲取當前用戶的當日打卡記錄")
    public ResponseResult<List<ClockInRecord>> getTodayRecords() {
        Long userId = getCurrentUserId();
        List<ClockInRecord> records = clockInService.getTodayRecords(userId);
        return ResponseResult.success(records);
    }

    /**
     * 獲取指定日期打卡記錄
     */
    @GetMapping("/date")
    @Operation(summary = "獲取指定日期打卡記錄", description = "獲取當前用戶指定日期的打卡記錄")
    public ResponseResult<List<ClockInRecord>> getRecordsByDate(
            @Parameter(description = "日期，格式：yyyy-MM-dd", example = "2025-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = getCurrentUserId();
        List<ClockInRecord> records = clockInService.getRecordsByDate(userId, date);
        return ResponseResult.success(records);
    }

    /**
     * 獲取日期範圍打卡記錄
     */
    @GetMapping("/range")
    @Operation(summary = "獲取日期範圍打卡記錄", description = "獲取當前用戶指定日期範圍的打卡記錄")
    public ResponseResult<List<ClockInRecord>> getRecordsByDateRange(
            @Parameter(description = "開始日期，格式：yyyy-MM-dd", example = "2025-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期，格式：yyyy-MM-dd", example = "2025-03-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();
        List<ClockInRecord> records = clockInService.getRecordsByDateRange(userId, startDate, endDate);
        return ResponseResult.success(records);
    }

    /**
     * 獲取指定日期打卡匯總
     */
    @GetMapping("/summary/date")
    @Operation(summary = "獲取指定日期打卡匯總", description = "獲取當前用戶指定日期的打卡匯總")
    public ResponseResult<ClockInSummary> getSummaryByDate(
            @Parameter(description = "日期，格式：yyyy-MM-dd", example = "2025-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = getCurrentUserId();
        ClockInSummary summary = clockInService.getSummaryByDate(userId, date);
        return ResponseResult.success(summary);
    }

    /**
     * 獲取日期範圍打卡匯總
     */
    @GetMapping("/summary/range")
    @Operation(summary = "獲取日期範圍打卡匯總", description = "獲取當前用戶指定日期範圍的打卡匯總")
    public ResponseResult<List<ClockInSummary>> getSummaryByDateRange(
            @Parameter(description = "開始日期，格式：yyyy-MM-dd", example = "2025-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期，格式：yyyy-MM-dd", example = "2025-03-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();
        List<ClockInSummary> summaries = clockInService.getSummaryByDateRange(userId, startDate, endDate);
        return ResponseResult.success(summaries);
    }

    /**
     * 獲取月度打卡統計
     */
    @GetMapping("/statistics/month")
    @Operation(summary = "獲取月度打卡統計", description = "獲取當前用戶指定年月的打卡統計")
    public ResponseResult<ClockInStatisticsDTO> getMonthlyStatistics(
            @Parameter(description = "年份", example = "2025")
            @RequestParam int year,
            @Parameter(description = "月份", example = "3")
            @RequestParam int month) {
        Long userId = getCurrentUserId();
        ClockInStatisticsDTO statistics = clockInService.getMonthlyStatistics(userId, year, month);
        return ResponseResult.success(statistics);
    }

    /**
     * 審批補卡申請（管理員）
     */
    @PostMapping("/admin/approve-makeup")
    @Operation(summary = "審批補卡申請", description = "管理員審批補卡申請")
    public ResponseResult<Boolean> approveMakeup(
            @Parameter(description = "打卡記錄ID", example = "1")
            @RequestParam Long recordId,
            @Parameter(description = "是否批准", example = "true")
            @RequestParam boolean approved,
            @Parameter(description = "備註", example = "已確認")
            @RequestParam(required = false) String remark) {
        Long approverId = getCurrentUserId();
        boolean result = clockInService.approveMakeup(recordId, approved, approverId, remark);
        return ResponseResult.success(result);
    }

    /**
     * 獲取當前用戶ID
     *
     * @return 用戶ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("未認證的用戶嘗試訪問打卡接口");
            throw new SecurityException("用戶未登錄");
        }
        
        // 在實際應用中，應該從認證信息中獲取用戶ID
        // 這裡簡化處理，從Principal中獲取用戶名，實際可能需要查詢數據庫或從JWT中解析
        String username = authentication.getName();
        
        // 這裡應該有一個用戶服務來根據用戶名獲取用戶ID
        // 現在簡單模擬，實際實現需要替換
        return 1L; // 模擬用戶ID，實際應通過用戶服務獲取
    }
}
