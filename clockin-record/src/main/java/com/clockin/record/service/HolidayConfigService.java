package com.clockin.record.service;

import com.clockin.record.dto.HolidayConfigDTO;
import com.clockin.record.entity.HolidayConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 節假日配置服務接口
 */
public interface HolidayConfigService {

    /**
     * 判斷指定日期是否為工作日
     *
     * @param date 指定日期
     * @return 是否為工作日
     */
    boolean isWorkday(LocalDate date);

    /**
     * 保存節假日配置
     *
     * @param config 節假日配置
     * @return 保存後的節假日配置
     */
    HolidayConfig saveHolidayConfig(HolidayConfig config);

    /**
     * 根據ID查詢節假日配置
     *
     * @param id 配置ID
     * @return 節假日配置
     */
    Optional<HolidayConfig> findById(Long id);

    /**
     * 根據日期查詢節假日配置
     *
     * @param date 日期
     * @return 節假日配置
     */
    Optional<HolidayConfig> findByDate(LocalDate date);

    /**
     * 查詢指定年份的所有節假日
     *
     * @param year 年份
     * @return 節假日列表
     */
    List<HolidayConfig> findHolidaysByYear(int year);

    /**
     * 查詢指定年份和月份的所有節假日
     *
     * @param year 年份
     * @param month 月份
     * @return 節假日列表
     */
    List<HolidayConfig> findHolidaysByYearAndMonth(int year, int month);

    /**
     * 查詢日期範圍內的所有節假日
     *
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 節假日列表
     */
    List<HolidayConfig> findHolidaysBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 刪除節假日配置
     *
     * @param id 配置ID
     */
    void deleteById(Long id);

    /**
     * 批量導入節假日配置
     *
     * @param holidays 節假日配置列表
     * @return 導入成功的節假日配置列表
     */
    List<HolidayConfig> batchImportHolidays(List<HolidayConfigDTO> holidays);

    /**
     * 創建節假日配置
     *
     * @param holidayDTO 節假日配置DTO
     * @return 創建後的節假日配置
     */
    HolidayConfig createHolidayConfig(HolidayConfigDTO holidayDTO);

    /**
     * 更新節假日配置
     *
     * @param id 配置ID
     * @param holidayDTO 節假日配置DTO
     * @return 更新後的節假日配置
     */
    HolidayConfig updateHolidayConfig(Long id, HolidayConfigDTO holidayDTO);
}
