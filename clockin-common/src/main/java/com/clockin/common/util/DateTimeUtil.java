package com.clockin.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具類，基於 Apache Commons
 */
public class DateTimeUtil {
    
    /**
     * 日期格式 - yyyy-MM-dd
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    
    /**
     * 時間格式 - HH:mm:ss
     */
    public static final String TIME_PATTERN = "HH:mm:ss";
    
    /**
     * 日期時間格式 - yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 獲取當前日期
     *
     * @return 當前日期
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * 獲取當前時間
     *
     * @return 當前時間
     */
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    /**
     * 獲取當前日期時間
     *
     * @return 當前日期時間
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化後的日期字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化時間
     *
     * @param time    時間
     * @param pattern 格式
     * @return 格式化後的時間字符串
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期時間
     *
     * @param dateTime 日期時間
     * @param pattern  格式
     * @return 格式化後的日期時間字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化 Date 對象
     *
     * @param date    Date 對象
     * @param pattern 格式
     * @return 格式化後的日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 解析日期字符串
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析時間字符串
     *
     * @param timeStr 時間字符串
     * @param pattern 格式
     * @return 時間
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期時間字符串
     *
     * @param dateTimeStr 日期時間字符串
     * @param pattern     格式
     * @return 日期時間
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串為 Date 對象
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date 對象
     * @throws ParseException 解析異常
     */
    public static Date parseDate(String dateStr, String... pattern) throws ParseException {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return DateUtils.parseDate(dateStr, pattern);
    }

    /**
     * Date 轉 LocalDate
     *
     * @param date Date 對象
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date 轉 LocalDateTime
     *
     * @param date Date 對象
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDate 轉 Date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime 轉 Date
     *
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 是否是週末
     *
     * @param date 日期
     * @return 是否是週末
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 獲取當月第一天
     *
     * @return 當月第一天
     */
    public static LocalDate getFirstDayOfMonth() {
        return getCurrentDate().withDayOfMonth(1);
    }

    /**
     * 獲取當月最後一天
     *
     * @return 當月最後一天
     */
    public static LocalDate getLastDayOfMonth() {
        return getCurrentDate().withDayOfMonth(getCurrentDate().lengthOfMonth());
    }

    /**
     * 取得指定日期的當月第一天
     *
     * @param date 指定日期
     * @return 當月第一天
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(1);
    }

    /**
     * 取得指定日期的當月最後一天
     *
     * @param date 指定日期
     * @return 當月最後一天
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    /**
     * 獲取兩個日期之間的天數
     *
     * @param start 開始日期
     * @param end   結束日期
     * @return 天數
     */
    public static long getDaysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 將 Unix 時間戳轉為 LocalDateTime
     *
     * @param timestamp Unix 時間戳（秒）
     * @return LocalDateTime
     */
    public static LocalDateTime fromUnixTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    /**
     * 將 LocalDateTime 轉為 Unix 時間戳
     *
     * @param localDateTime LocalDateTime
     * @return Unix 時間戳（秒）
     */
    public static long toUnixTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
