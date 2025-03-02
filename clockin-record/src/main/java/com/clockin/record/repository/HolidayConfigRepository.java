package com.clockin.record.repository;

import com.clockin.record.entity.HolidayConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 節假日配置數據訪問層
 */
@Repository
public interface HolidayConfigRepository extends JpaRepository<HolidayConfig, Long> {

    /**
     * 根據日期查詢假日配置
     *
     * @param holidayDate 假日日期
     * @return 假日配置
     */
    Optional<HolidayConfig> findByHolidayDate(LocalDate holidayDate);

    /**
     * 根據日期範圍查詢假日配置列表
     *
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 假日配置列表
     */
    List<HolidayConfig> findByHolidayDateBetweenOrderByHolidayDate(LocalDate startDate, LocalDate endDate);

    /**
     * 根據假日類型查詢假日配置列表
     *
     * @param holidayType 假日類型
     * @return 假日配置列表
     */
    List<HolidayConfig> findByHolidayTypeOrderByHolidayDate(HolidayConfig.HolidayType holidayType);

    /**
     * 根據日期範圍和假日類型查詢假日配置列表
     *
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @param holidayType 假日類型
     * @return 假日配置列表
     */
    List<HolidayConfig> findByHolidayDateBetweenAndHolidayTypeOrderByHolidayDate(
            LocalDate startDate, LocalDate endDate, HolidayConfig.HolidayType holidayType);

    /**
     * 根據年份查詢假日配置列表
     *
     * @param year 年份
     * @return 假日配置列表
     */
    @Query("SELECT h FROM HolidayConfig h WHERE YEAR(h.holidayDate) = :year ORDER BY h.holidayDate")
    List<HolidayConfig> findByYear(@Param("year") int year);

    /**
     * 根據年份和月份查詢假日配置列表
     *
     * @param year 年份
     * @param month 月份
     * @return 假日配置列表
     */
    @Query("SELECT h FROM HolidayConfig h WHERE YEAR(h.holidayDate) = :year AND MONTH(h.holidayDate) = :month ORDER BY h.holidayDate")
    List<HolidayConfig> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    /**
     * 查詢日期範圍內的工作日配置
     *
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 工作日配置列表
     */
    List<HolidayConfig> findByHolidayDateBetweenAndIsWorkdayTrueOrderByHolidayDate(LocalDate startDate, LocalDate endDate);

    /**
     * 查詢日期範圍內的非工作日配置
     *
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 非工作日配置列表
     */
    List<HolidayConfig> findByHolidayDateBetweenAndIsWorkdayFalseOrderByHolidayDate(LocalDate startDate, LocalDate endDate);
}
