package com.clockin.record.controller.api;

import com.clockin.record.common.R;
import com.clockin.record.service.ImportExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 數據導入導出 API 控制器
 */
@Slf4j
@Tag(name = "數據導入導出", description = "數據導入導出相關接口")
@RestController
@RequestMapping("/api/import-export")
@RequiredArgsConstructor
public class ImportExportApiController {

    private final ImportExportService importExportService;

    @Operation(summary = "導入打卡記錄", description = "從Excel文件批量導入打卡記錄")
    @PostMapping("/import/clock-records")
    public R<Integer> importClockRecords(
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return R.fail("上傳的文件為空");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
            return R.fail("文件格式不正確，僅支持 .xlsx 或 .xls 格式");
        }
        
        try {
            // 驗證模板格式
            String validationResult = importExportService.validateExcelTemplate(file);
            if (validationResult != null) {
                return R.fail(validationResult);
            }
            
            // 導入數據
            int count = importExportService.importClockRecordsFromExcel(file);
            return R.ok(count, "成功導入 " + count + " 條打卡記錄");
        } catch (Exception e) {
            log.error("導入打卡記錄失敗", e);
            return R.fail("導入失敗：" + e.getMessage());
        }
    }

    @Operation(summary = "導出打卡記錄", description = "將打卡記錄導出為Excel文件")
    @GetMapping("/export/clock-records")
    public ResponseEntity<InputStreamResource> exportClockRecords(
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "用戶ID（為空則導出所有用戶）") @RequestParam(required = false) Long userId,
            @Parameter(description = "部門ID（為空則不按部門篩選）") @RequestParam(required = false) Long departmentId) {
        
        try {
            InputStream excelStream = importExportService.exportClockRecordsToExcel(startDate, endDate, userId, departmentId);
            
            // 生成文件名
            String fileName = "打卡記錄_" + startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + 
                    "_" + endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(excelStream));
        } catch (Exception e) {
            log.error("導出打卡記錄失敗", e);
            throw new RuntimeException("導出失敗：" + e.getMessage());
        }
    }

    @Operation(summary = "下載導入模板", description = "下載打卡記錄導入模板")
    @GetMapping("/template/clock-records")
    public ResponseEntity<InputStreamResource> downloadTemplate() {
        try {
            InputStream templateStream = importExportService.downloadImportTemplate();
            
            String fileName = "打卡記錄導入模板.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(templateStream));
        } catch (Exception e) {
            log.error("下載模板失敗", e);
            throw new RuntimeException("下載模板失敗：" + e.getMessage());
        }
    }

    @Operation(summary = "導出考勤統計報表", description = "將考勤統計導出為Excel文件")
    @GetMapping("/export/attendance-statistics")
    public ResponseEntity<InputStreamResource> exportAttendanceStatistics(
            @Parameter(description = "年份") @RequestParam int year,
            @Parameter(description = "月份 (1-12)") @RequestParam int month,
            @Parameter(description = "部門ID（為空則導出所有部門）") @RequestParam(required = false) Long departmentId) {
        
        try {
            InputStream excelStream = importExportService.exportAttendanceStatistics(year, month, departmentId);
            
            // 生成文件名
            String fileName = "考勤統計_" + year + "年" + month + "月.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(excelStream));
        } catch (Exception e) {
            log.error("導出考勤統計報表失敗", e);
            throw new RuntimeException("導出失敗：" + e.getMessage());
        }
    }

    @Operation(summary = "導出用戶打卡記錄", description = "將特定用戶的打卡記錄導出為Excel文件")
    @GetMapping("/export/user-records/{userId}")
    public ResponseEntity<InputStreamResource> exportUserClockRecords(
            @Parameter(description = "用戶ID") @PathVariable Long userId,
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            InputStream excelStream = importExportService.exportUserClockRecords(userId, startDate, endDate);
            
            // 生成文件名
            String fileName = "用戶打卡記錄_" + userId + "_" + 
                    startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + 
                    "_" + endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(excelStream));
        } catch (Exception e) {
            log.error("導出用戶打卡記錄失敗", e);
            throw new RuntimeException("導出失敗：" + e.getMessage());
        }
    }

    @Operation(summary = "導出部門請假記錄", description = "將部門的請假記錄導出為Excel文件")
    @GetMapping("/export/department-leave/{departmentId}")
    public ResponseEntity<InputStreamResource> exportDepartmentLeaveRecords(
            @Parameter(description = "部門ID") @PathVariable Long departmentId,
            @Parameter(description = "開始日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期 (格式: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        try {
            InputStream excelStream = importExportService.exportDepartmentLeaveRecords(departmentId, startDate, endDate);
            
            // 生成文件名
            String fileName = "部門請假記錄_" + departmentId + "_" + 
                    startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + 
                    "_" + endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(excelStream));
        } catch (Exception e) {
            log.error("導出部門請假記錄失敗", e);
            throw new RuntimeException("導出失敗：" + e.getMessage());
        }
    }
}
