package com.clockin.record.service;

import com.clockin.record.dto.ClockRecordDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 數據導入導出服務接口
 */
public interface ImportExportService {

    /**
     * 從Excel文件批量導入打卡記錄
     *
     * @param file Excel文件
     * @return 成功導入的記錄數
     */
    int importClockRecordsFromExcel(MultipartFile file);

    /**
     * 導出打卡記錄到Excel
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param userId    用戶ID（為null則導出所有用戶）
     * @param departmentId 部門ID（為null則不按部門篩選）
     * @return Excel文件的輸入流
     */
    InputStream exportClockRecordsToExcel(LocalDate startDate, LocalDate endDate, Long userId, Long departmentId);

    /**
     * 驗證Excel模板格式是否正確
     *
     * @param file Excel文件
     * @return 驗證結果信息
     */
    String validateExcelTemplate(MultipartFile file);

    /**
     * 下載打卡記錄導入模板
     *
     * @return 模板文件的輸入流
     */
    InputStream downloadImportTemplate();

    /**
     * 導出考勤統計報表
     *
     * @param year  年份
     * @param month 月份
     * @param departmentId 部門ID（為null則導出所有部門）
     * @return Excel文件的輸入流
     */
    InputStream exportAttendanceStatistics(int year, int month, Long departmentId);

    /**
     * 批量處理導入的打卡數據
     *
     * @param records 打卡記錄列表
     * @return 處理結果，包含成功和失敗的記錄
     */
    Map<String, Object> processBatchImport(List<ClockRecordDTO> records);

    /**
     * 導出特定用戶的打卡記錄
     *
     * @param userId 用戶ID
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return Excel文件的輸入流
     */
    InputStream exportUserClockRecords(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 導出部門的請假記錄
     *
     * @param departmentId 部門ID
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return Excel文件的輸入流
     */
    InputStream exportDepartmentLeaveRecords(Long departmentId, LocalDate startDate, LocalDate endDate);
}
