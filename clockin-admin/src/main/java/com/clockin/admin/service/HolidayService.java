package com.clockin.admin.service;

import com.clockin.admin.entity.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 假日配置服務接口
 */
public interface HolidayService {
    
    /**
     * 創建假日配置
     *
     * @param holiday 假日配置
     * @return 創建後的假日配置
     */
    Holiday createHoliday(Holiday holiday);
    
    /**
     * 批量創建假日配置
     *
     * @param holidays 假日配置列表
     * @return 創建後的假日配置列表
     */
    List<Holiday> batchCreateHolidays(List<Holiday> holidays);
    
    /**
     * 更新假日配置
     *
     * @param holiday 假日配置
     * @return 更新後的假日配置
     */
    Holiday updateHoliday(Holiday holiday);
    
    /**
     * 刪除假日配置
     *
     * @param id 假日ID
     */
    void deleteHoliday(Long id);
    
    /**
     * 根據ID查詢假日配置
     *
     * @param id 假日ID
     * @return 假日配置
     */
    Optional<Holiday> getHolidayById(Long id);
    
    /**
     * 根據日期查詢假日配置
     *
     * @param date 日期
     * @return 假日配置
     */
    Optional<Holiday> getHolidayByDate(LocalDate date);
    
    /**
     * 查詢指定年份的所有假日
     *
     * @param year 年份
     * @return 假日配置列表
     */
    List<Holiday> getHolidaysByYear(Integer year);
    
    /**
     * 查詢指定年份和月份的所有假日
     *
     * @param year  年份
     * @param month 月份
     * @return 假日配置列表
     */
    List<Holiday> getHolidaysByYearAndMonth(Integer year, Integer month);
    
    /**
     * 查詢日期範圍內的所有假日
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 假日配置列表
     */
    List<Holiday> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 查詢指定類型和年份的所有假日
     *
     * @param holidayType 假日類型
     * @param year        年份
     * @return 假日配置列表
     */
    List<Holiday> getHolidaysByTypeAndYear(Integer holidayType, Integer year);
    
    /**
     * 判斷指定日期是否為假日
     *
     * @param date 日期
     * @return 是否為假日
     */
    boolean isHoliday(LocalDate date);
    
    /**
     * 批量導入假日配置
     *
     * @param holidays 假日配置列表
     * @return 導入後的假日配置列表
     */
    List<Holiday> batchImportHolidays(List<Holiday> holidays);
    
    /**
     * 分頁查詢假日配置
     *
     * @param year        年份，可為 null
     * @param month       月份，可為 null
     * @param holidayType 假日類型，可為 null
     * @param pageable    分頁參數
     * @return 分頁數據
     */
    Page<Holiday> getHolidaysPage(Integer year, Integer month, Integer holidayType, Pageable pageable);
    
    /**
     * 統計某年某月的假日數量
     *
     * @param year  年份
     * @param month 月份
     * @return 假日數量
     */
    int countHolidaysByYearAndMonth(Integer year, Integer month);
    
    /**
     * 刷新假日緩存
     *
     * @param year 年份
     */
    void refreshHolidayCache(Integer year);
}
