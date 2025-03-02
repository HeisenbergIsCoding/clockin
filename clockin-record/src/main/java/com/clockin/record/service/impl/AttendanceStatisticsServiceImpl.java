package com.clockin.record.service.impl;

import com.clockin.record.dto.AttendanceStatisticsDTO;
import com.clockin.record.dto.DepartmentDTO;
import com.clockin.record.dto.SysUserDTO;
import com.clockin.record.entity.ClockRecord;
import com.clockin.record.exception.ApiException;
import com.clockin.record.service.AttendanceStatisticsService;
import com.clockin.record.service.ClockRecordService;
import com.clockin.record.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤統計服務實現類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceStatisticsServiceImpl implements AttendanceStatisticsService {

    private final ClockRecordService clockRecordService;
    private final UserService userService;

    // 標準上班時間（9:00）
    private static final LocalTime STANDARD_WORK_START_TIME = LocalTime.of(9, 0);
    // 標準下班時間（18:00）
    private static final LocalTime STANDARD_WORK_END_TIME = LocalTime.of(18, 0);
    // 標準工作時長（分鐘）
    private static final int STANDARD_WORK_DURATION = 540; // 9小時 = 540分鐘
    // 遲到閾值（分鐘）
    private static final int LATE_THRESHOLD = 15;
    // 早退閾值（分鐘）
    private static final int EARLY_LEAVE_THRESHOLD = 15;
    // 加班閾值（分鐘）
    private static final int OVERTIME_THRESHOLD = 60;

    @Override
    public AttendanceStatisticsDTO getUserStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        // 1. 獲取用戶信息
        SysUserDTO user = userService.getUserById(userId);
        if (user == null) {
            throw new ApiException("用戶不存在");
        }

        // 2. 獲取用戶在日期範圍內的打卡記錄
        List<ClockRecord> records = clockRecordService.getRecordsByUserAndDateRange(userId, startDate, endDate);

        // 3. 計算工作日和休息日
        Map<LocalDate, Boolean> workingDayMap = calculateWorkingDays(startDate, endDate);
        int workingDays = (int) workingDayMap.values().stream().filter(isWorkingDay -> isWorkingDay).count();
        int holidayDays = (int) workingDayMap.values().stream().filter(isWorkingDay -> !isWorkingDay).count();

        // 4. 統計打卡情況
        Map<LocalDate, ClockRecord> recordMap = records.stream()
                .collect(Collectors.toMap(ClockRecord::getClockDate, record -> record));

        int normalDays = 0;
        int abnormalDays = 0;
        int missingDays = 0;
        int onTimeDays = 0;
        int lateDays = 0;
        int earlyLeaveDays = 0;
        int overtimeDays = 0;
        int totalWorkDuration = 0;
        int overtimeDuration = 0;

        for (Map.Entry<LocalDate, Boolean> entry : workingDayMap.entrySet()) {
            LocalDate date = entry.getKey();
            boolean isWorkingDay = entry.getValue();

            if (isWorkingDay) {
                ClockRecord record = recordMap.get(date);
                if (record == null) {
                    // 工作日未打卡
                    missingDays++;
                } else if (Boolean.TRUE.equals(record.getIsAbnormal())) {
                    // 打卡異常
                    abnormalDays++;
                    // 計算工作時長
                    if (record.getWorkDuration() != null) {
                        totalWorkDuration += record.getWorkDuration();
                    }
                    // 檢查是否遲到或早退
                    if (record.getClockInTime() != null && 
                            record.getClockInTime().toLocalTime().isAfter(STANDARD_WORK_START_TIME.plusMinutes(LATE_THRESHOLD))) {
                        lateDays++;
                    }
                    if (record.getClockOutTime() != null && 
                            record.getClockOutTime().toLocalTime().isBefore(STANDARD_WORK_END_TIME.minusMinutes(EARLY_LEAVE_THRESHOLD))) {
                        earlyLeaveDays++;
                    }
                } else {
                    // 正常打卡
                    normalDays++;
                    // 計算工作時長
                    if (record.getWorkDuration() != null) {
                        totalWorkDuration += record.getWorkDuration();
                        if (record.getWorkDuration() > STANDARD_WORK_DURATION + OVERTIME_THRESHOLD) {
                            overtimeDays++;
                            overtimeDuration += (record.getWorkDuration() - STANDARD_WORK_DURATION);
                        }
                    }
                    // 檢查是否準時到達
                    if (record.getClockInTime() != null && 
                            !record.getClockInTime().toLocalTime().isAfter(STANDARD_WORK_START_TIME.plusMinutes(LATE_THRESHOLD))) {
                        onTimeDays++;
                    } else {
                        lateDays++;
                    }
                    // 檢查是否早退
                    if (record.getClockOutTime() != null && 
                            record.getClockOutTime().toLocalTime().isBefore(STANDARD_WORK_END_TIME.minusMinutes(EARLY_LEAVE_THRESHOLD))) {
                        earlyLeaveDays++;
                    }
                }
            } else {
                // 休息日，檢查是否加班
                ClockRecord record = recordMap.get(date);
                if (record != null && record.getWorkDuration() != null && record.getWorkDuration() > 0) {
                    overtimeDays++;
                    overtimeDuration += record.getWorkDuration();
                }
            }
        }

        // 5. 計算平均工作時長
        int averageWorkDuration = normalDays > 0 ? totalWorkDuration / normalDays : 0;

        // 6. 構建統計對象
        AttendanceStatisticsDTO statistics = new AttendanceStatisticsDTO();
        statistics.setUserId(userId);
        statistics.setUserName(user.getUsername());
        statistics.setDepartmentId(user.getDepartmentId());
        statistics.setDepartmentName(user.getDepartmentName());
        statistics.setStartDate(startDate);
        statistics.setEndDate(endDate);
        statistics.setNormalDays(normalDays);
        statistics.setAbnormalDays(abnormalDays);
        statistics.setMissingDays(missingDays);
        statistics.setWorkingDays(workingDays);
        statistics.setHolidayDays(holidayDays);
        statistics.setTotalWorkDuration(totalWorkDuration);
        statistics.setTotalWorkDurationFormatted(formatDuration(totalWorkDuration));
        statistics.setAverageWorkDuration(averageWorkDuration);
        statistics.setAverageWorkDurationFormatted(formatDuration(averageWorkDuration));
        statistics.setOnTimeDays(onTimeDays);
        statistics.setLateDays(lateDays);
        statistics.setEarlyLeaveDays(earlyLeaveDays);
        statistics.setOvertimeDays(overtimeDays);
        statistics.setOvertimeDuration(overtimeDuration);
        statistics.setOvertimeDurationFormatted(formatDuration(overtimeDuration));

        return statistics;
    }

    @Override
    public AttendanceStatisticsDTO getCurrentUserStatistics(LocalDate startDate, LocalDate endDate) {
        SysUserDTO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ApiException("未獲取到當前用戶信息");
        }
        return getUserStatistics(currentUser.getId(), startDate, endDate);
    }

    @Override
    public List<AttendanceStatisticsDTO> getDepartmentStatistics(Long departmentId, LocalDate startDate, LocalDate endDate) {
        // 獲取部門信息
        DepartmentDTO department = userService.getDepartmentById(departmentId);
        if (department == null) {
            throw new ApiException("部門不存在");
        }

        // 獲取部門內所有用戶
        List<SysUserDTO> users = userService.getUsersByDepartment(departmentId);
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        // 獲取每個用戶的統計數據
        List<AttendanceStatisticsDTO> statisticsList = new ArrayList<>();
        for (SysUserDTO user : users) {
            try {
                AttendanceStatisticsDTO statistics = getUserStatistics(user.getId(), startDate, endDate);
                statisticsList.add(statistics);
            } catch (Exception e) {
                log.error("獲取用戶考勤統計異常，用戶ID: {}, 錯誤信息: {}", user.getId(), e.getMessage());
            }
        }

        return statisticsList;
    }

    @Override
    public List<AttendanceStatisticsDTO> getAllUserStatistics(LocalDate startDate, LocalDate endDate) {
        // 獲取所有部門
        List<DepartmentDTO> departments = userService.getAllDepartments();

        // 獲取所有用戶統計數據
        List<AttendanceStatisticsDTO> allStatistics = new ArrayList<>();
        for (DepartmentDTO department : departments) {
            try {
                List<AttendanceStatisticsDTO> departmentStatistics = getDepartmentStatistics(department.getId(), startDate, endDate);
                allStatistics.addAll(departmentStatistics);
            } catch (Exception e) {
                log.error("獲取部門考勤統計異常，部門ID: {}, 錯誤信息: {}", department.getId(), e.getMessage());
            }
        }

        return allStatistics;
    }

    @Override
    public List<AttendanceStatisticsDTO> generateMonthlyReport(int year, int month) {
        // 獲取指定月份的開始和結束日期
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        // 獲取所有用戶的考勤統計
        return getAllUserStatistics(startDate, endDate);
    }

    @Override
    public List<AttendanceStatisticsDTO> getAbnormalUserStatistics(LocalDate startDate, LocalDate endDate) {
        // 獲取所有用戶的考勤統計
        List<AttendanceStatisticsDTO> allStatistics = getAllUserStatistics(startDate, endDate);

        // 篩選出有異常的用戶
        return allStatistics.stream()
                .filter(statistics -> statistics.getAbnormalDays() > 0 || statistics.getMissingDays() > 0)
                .collect(Collectors.toList());
    }

    /**
     * 計算指定日期範圍內的工作日和休息日
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 日期與是否為工作日的映射
     */
    private Map<LocalDate, Boolean> calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Boolean> workingDayMap = new HashMap<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            // 簡單判斷：週一至週五為工作日，週六日為休息日
            boolean isWorkingDay = !(currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || 
                                      currentDate.getDayOfWeek() == DayOfWeek.SUNDAY);
            workingDayMap.put(currentDate, isWorkingDay);
            currentDate = currentDate.plusDays(1);
        }

        return workingDayMap;
    }

    /**
     * 格式化時長（分鐘）為小時和分鐘的字符串
     *
     * @param minutes 時長（分鐘）
     * @return 格式化後的字符串
     */
    private String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%d小時%d分鐘", hours, mins);
    }
}
