package com.clockin.record.controller.api;

import com.clockin.record.dto.ApiResponse;
import com.clockin.record.dto.ClockRecordDTO;
import com.clockin.record.service.ClockRecordDTOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 打卡API控制器
 * <p>
 * 提供給前端使用的打卡相關API
 */
@RestController
@RequestMapping("/api/v1/clock")
@RequiredArgsConstructor
@Tag(name = "打卡管理", description = "打卡相關操作API")
@SecurityRequirement(name = "bearerAuth")
public class ClockApiController {

    private final ClockRecordDTOService clockRecordDTOService;

    @PostMapping("/in")
    @Operation(summary = "上班打卡", description = "當前用戶上班打卡")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClockRecordDTO>> clockIn(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime clockInTime,
            @RequestParam(required = false) String workLocation,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String deviceInfo,
            @RequestParam(required = false) String remark) {

        ClockRecordDTO result = clockRecordDTOService.currentUserClockIn(
                clockInTime, workLocation, ipAddress, deviceInfo, remark);
        return ResponseEntity.ok(ApiResponse.success("上班打卡成功", result));
    }

    @PostMapping("/out")
    @Operation(summary = "下班打卡", description = "當前用戶下班打卡")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClockRecordDTO>> clockOut(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime clockOutTime,
            @RequestParam(required = false) String workLocation,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String deviceInfo,
            @RequestParam(required = false) String remark) {

        ClockRecordDTO result = clockRecordDTOService.currentUserClockOut(
                clockOutTime, workLocation, ipAddress, deviceInfo, remark);
        return ResponseEntity.ok(ApiResponse.success("下班打卡成功", result));
    }

    @GetMapping("/today")
    @Operation(summary = "獲取今日打卡", description = "獲取當前用戶今日打卡記錄")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClockRecordDTO>> getTodayRecord() {
        return clockRecordDTOService.getRecordByUserAndDate(null, LocalDate.now())
                .map(record -> ResponseEntity.ok(ApiResponse.success(record)))
                .orElse(ResponseEntity.ok(ApiResponse.success("今日尚未打卡", null)));
    }

    @GetMapping("/latest")
    @Operation(summary = "獲取最近打卡", description = "獲取當前用戶最近一次打卡記錄")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClockRecordDTO>> getLatestRecord() {
        return clockRecordDTOService.getCurrentUserLatestRecord()
                .map(record -> ResponseEntity.ok(ApiResponse.success(record)))
                .orElse(ResponseEntity.ok(ApiResponse.success("暫無打卡記錄", null)));
    }

    @GetMapping("/history")
    @Operation(summary = "獲取打卡歷史", description = "獲取當前用戶指定日期範圍內的打卡記錄")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ClockRecordDTO>>> getHistoryRecords(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<ClockRecordDTO> records = clockRecordDTOService.getRecordsByUserAndDateRange(null, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @GetMapping("/{id}")
    @Operation(summary = "獲取打卡詳情", description = "根據ID獲取打卡記錄詳情")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ClockRecordDTO>> getRecordById(@PathVariable Long id) {
        return clockRecordDTOService.getRecordById(id)
                .map(record -> ResponseEntity.ok(ApiResponse.success(record)))
                .orElse(ResponseEntity.ok(ApiResponse.error(404, "打卡記錄不存在")));
    }

    @GetMapping("/page")
    @Operation(summary = "分頁查詢打卡記錄", description = "分頁查詢當前用戶的打卡記錄")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<ClockRecordDTO>>> getRecordsPage(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) Boolean isAbnormal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "clockDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ClockRecordDTO> records = clockRecordDTOService.getRecordsPage(null, date, isAbnormal, pageable);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @GetMapping("/admin/all")
    @Operation(summary = "獲取所有用戶今日打卡", description = "獲取所有用戶的今日打卡記錄（管理員使用）")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ClockRecordDTO>>> getAllTodayRecords() {
        List<ClockRecordDTO> records = clockRecordDTOService.getTodayRecords();
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @GetMapping("/admin/abnormal")
    @Operation(summary = "獲取異常打卡記錄", description = "獲取指定日期範圍內所有異常打卡記錄（管理員使用）")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ClockRecordDTO>>> getAbnormalRecords(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<ClockRecordDTO> records = clockRecordDTOService.getAbnormalRecords(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新打卡記錄", description = "更新指定ID的打卡記錄（管理員使用）")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClockRecordDTO>> updateRecord(
            @PathVariable Long id,
            @RequestBody ClockRecordDTO recordDTO) {

        if (!id.equals(recordDTO.getId())) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "ID不匹配"));
        }

        ClockRecordDTO updated = clockRecordDTOService.updateRecord(recordDTO);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
    }
}
