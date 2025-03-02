package com.clockin.record.service;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 工作時間配置客戶端
 * 用於調用 admin 服務的接口獲取工作時間配置
 */
@HttpExchange("/admin")
public interface WorkConfigClient {

    /**
     * 獲取標準上班時間
     *
     * @param userId 用戶ID，可選，不傳則獲取默認配置
     * @return 標準上班時間
     */
    @GetExchange("/worktime/clockin")
    LocalTime getStandardClockInTime(@RequestParam(value = "userId", required = false) Long userId);

    /**
     * 獲取標準下班時間
     *
     * @param userId 用戶ID，可選，不傳則獲取默認配置
     * @return 標準下班時間
     */
    @GetExchange("/worktime/clockout")
    LocalTime getStandardClockOutTime(@RequestParam(value = "userId", required = false) Long userId);

    /**
     * 獲取遲到閾值（分鐘）
     *
     * @return 遲到閾值
     */
    @GetExchange("/worktime/late-threshold")
    int getLateThresholdMinutes();

    /**
     * 獲取早退閾值（分鐘）
     *
     * @return 早退閾值
     */
    @GetExchange("/worktime/early-leave-threshold")
    int getEarlyLeaveThresholdMinutes();

    /**
     * 檢查指定日期是否為工作日
     *
     * @param date 日期
     * @return 是否為工作日
     */
    @GetExchange("/holiday/is-workday")
    boolean isWorkDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    /**
     * 獲取指定年月的假期數量
     *
     * @param year  年份
     * @param month 月份
     * @return 假期數量
     */
    @GetExchange("/holiday/count/{year}/{month}")
    int getHolidayCount(@PathVariable("year") int year, @PathVariable("month") int month);
}
