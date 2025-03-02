package com.clockin.record.controller.api;

import com.clockin.record.common.R;
import com.clockin.record.dto.AttendanceStatisticsDTO;
import com.clockin.record.service.AttendanceStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 考勤統計 API 控制器
 */
@Tag(name = "考勤統計", description = "考勤統計相關 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {

    private final AttendanceStatisticsService attendanceStatisticsService;

    @Operation(summary = "獲取當前用戶考勤統計", description = "獲取當前登錄用戶在指定日期範圍內的考勤統計數據")
    @GetMapping("/current")
    public R<AttendanceStatisticsDTO> getCurrentUserStatistics(
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        AttendanceStatisticsDTO statistics = attendanceStatisticsService.getCurrentUserStatistics(startDate, endDate);
        return R.ok(statistics);
    }

    @Operation(summary = "獲取指定用戶考勤統計", description = "獲取指定用戶在指定日期範圍內的考勤統計數據")
    @GetMapping("/user/{userId}")
    public R<AttendanceStatisticsDTO> getUserStatistics(
            @Parameter(description = "用戶ID") @PathVariable Long userId,
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        AttendanceStatisticsDTO statistics = attendanceStatisticsService.getUserStatistics(userId, startDate, endDate);
        return R.ok(statistics);
    }

    @Operation(summary = "獲取部門考勤統計", description = "獲取指定部門在指定日期範圍內的考勤統計數據")
    @GetMapping("/department/{departmentId}")
    public R<List<AttendanceStatisticsDTO>> getDepartmentStatistics(
            @Parameter(description = "部門ID") @PathVariable Long departmentId,
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceStatisticsDTO> statistics = attendanceStatisticsService.getDepartmentStatistics(departmentId, startDate, endDate);
        return R.ok(statistics);
    }

    @Operation(summary = "獲取所有用戶考勤統計", description = "獲取所有用戶在指定日期範圍內的考勤統計數據")
    @GetMapping("/all")
    public R<List<AttendanceStatisticsDTO>> getAllUserStatistics(
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceStatisticsDTO> statistics = attendanceStatisticsService.getAllUserStatistics(startDate, endDate);
        return R.ok(statistics);
    }

    @Operation(summary = "生成月度考勤報表", description = "生成指定年月的考勤月報")
    @GetMapping("/report/monthly")
    public R<List<AttendanceStatisticsDTO>> generateMonthlyReport(
            @Parameter(description = "年份") @RequestParam int year,
            @Parameter(description = "月份 (1-12)") @RequestParam int month) {
        
        List<AttendanceStatisticsDTO> report = attendanceStatisticsService.generateMonthlyReport(year, month);
        return R.ok(report);
    }

    @Operation(summary = "獲取考勤異常用戶統計", description = "獲取指定日期範圍內有考勤異常的用戶統計數據")
    @GetMapping("/abnormal")
    public R<List<AttendanceStatisticsDTO>> getAbnormalUserStatistics(
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<AttendanceStatisticsDTO> statistics = attendanceStatisticsService.getAbnormalUserStatistics(startDate, endDate);
        return R.ok(statistics);
    }
}
