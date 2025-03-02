package com.clockin.admin.repository;

import com.clockin.admin.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 假日配置數據庫操作
 */
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long>, JpaSpecificationExecutor<Holiday> {
    
    /**
     * 根據假日日期查詢假日配置
     *
     * @param holidayDate 假日日期
     * @return 假日配置
     */
    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);
    
    /**
     * 根據年份查詢假日列表
     *
     * @param year 年份
     * @return 假日列表
     */
    List<Holiday> findByYearOrderByHolidayDateAsc(Integer year);
    
    /**
     * 根據年份和月份查詢假日列表
     *
     * @param year  年份
     * @param month 月份
     * @return 假日列表
     */
    List<Holiday> findByYearAndMonthOrderByDayAsc(Integer year, Integer month);
    
    /**
     * 查詢日期範圍內的假日列表
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 假日列表
     */
    List<Holiday> findByHolidayDateBetweenOrderByHolidayDateAsc(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根據假日類型和年份查詢假日列表
     *
     * @param holidayType 假日類型
     * @param year        年份
     * @return 假日列表
     */
    List<Holiday> findByHolidayTypeAndYearOrderByHolidayDateAsc(Integer holidayType, Integer year);
    
    /**
     * 判斷日期是否為假日
     *
     * @param date 日期
     * @return 假日數量，大於0表示為假日
     */
    @Query("SELECT COUNT(h) FROM Holiday h WHERE h.holidayDate = :date AND h.status = 1")
    Integer countByHolidayDateAndStatusActive(@Param("date") LocalDate date);
    
    /**
     * 統計某年某月的假日數量
     *
     * @param year  年份
     * @param month 月份
     * @return 假日數量
     */
    @Query("SELECT COUNT(h) FROM Holiday h WHERE h.year = :year AND h.month = :month AND h.status = 1")
    Long countByYearAndMonthAndStatusActive(@Param("year") Integer year, @Param("month") Integer month);
}
