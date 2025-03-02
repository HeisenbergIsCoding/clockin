package com.clockin.record.service.impl;

import com.clockin.common.exception.BusinessException;
import com.clockin.record.dto.ClockInRequest;
import com.clockin.record.dto.ClockInResponse;
import com.clockin.record.dto.ClockInStatisticsDTO;
import com.clockin.record.entity.ClockInRecord;
import com.clockin.record.entity.ClockInSummary;
import com.clockin.record.enums.AbsenceType;
import com.clockin.record.enums.ClockInStatus;
import com.clockin.record.enums.ClockInType;
import com.clockin.record.repository.ClockInRecordRepository;
import com.clockin.record.repository.ClockInSummaryRepository;
import com.clockin.record.service.ClockInService;
import com.clockin.record.service.WorkConfigClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 打卡服務實現
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClockInServiceImpl implements ClockInService {

    private static final int NOT_DELETED = 0;
    private static final int IS_DELETED = 1;
    
    private final ClockInRecordRepository recordRepository;
    private final ClockInSummaryRepository summaryRepository;
    private final WorkConfigClient workConfigClient;

    @Override
    @Transactional
    public ClockInResponse clockIn(Long userId, ClockInRequest request) {
        // 校驗參數
        if (userId == null || request.getClockType() == null) {
            throw new BusinessException("參數不完整");
        }

        // 設置默認值
        LocalDate clockDate = request.getClockDate() != null ? request.getClockDate() : LocalDate.now();
        LocalTime clockTime = request.getClockTime() != null ? request.getClockTime() : LocalTime.now();

        // 檢查是否是工作日
        boolean isWorkDay = workConfigClient.isWorkDay(clockDate);
        if (!isWorkDay) {
            log.info("用戶 {} 在非工作日 {} 打卡", userId, clockDate);
        }

        // 獲取工作時間配置
        LocalTime standardClockInTime = workConfigClient.getStandardClockInTime(userId);
        LocalTime standardClockOutTime = workConfigClient.getStandardClockOutTime(userId);
        int lateMinutes = workConfigClient.getLateThresholdMinutes();
        int earlyLeaveMinutes = workConfigClient.getEarlyLeaveThresholdMinutes();

        // 檢查是否已經打過卡
        boolean isFirstClockIn = !recordRepository.existsByUserIdAndClockDateAndClockTypeAndIsDeleted(
                userId, clockDate, request.getClockType(), NOT_DELETED);

        // 判斷打卡狀態
        ClockInStatus status;
        if (Boolean.TRUE.equals(request.getIsMakeup())) {
            status = ClockInStatus.MAKEUP;
        } else if (request.getClockType() == ClockInType.CLOCK_IN) {
            // 上班打卡，判斷是否遲到
            long minutesLate = standardClockInTime.until(clockTime, ChronoUnit.MINUTES);
            status = minutesLate > lateMinutes ? ClockInStatus.LATE : ClockInStatus.NORMAL;
        } else {
            // 下班打卡，判斷是否早退
            long minutesEarly = clockTime.until(standardClockOutTime, ChronoUnit.MINUTES);
            status = minutesEarly > earlyLeaveMinutes ? ClockInStatus.EARLY_LEAVE : ClockInStatus.NORMAL;
        }

        // 創建打卡記錄
        ClockInRecord record = ClockInRecord.builder()
                .userId(userId)
                .clockDate(clockDate)
                .clockTime(clockTime)
                .clockType(request.getClockType())
                .status(status)
                .location(request.getLocation())
                .device(request.getDevice())
                .remark(request.getRemark())
                .isDeleted(NOT_DELETED)
                .build();

        // 保存記錄
        record = recordRepository.save(record);
        log.info("用戶 {} 在 {} {} 打卡成功，狀態：{}", userId, clockDate, clockTime, status.getDesc());

        // 更新匯總數據
        calculateAndUpdateSummary(userId, clockDate);

        // 構建響應
        return ClockInResponse.builder()
                .id(record.getId())
                .userId(userId)
                .clockDate(clockDate)
                .clockTime(clockTime)
                .clockType(request.getClockType())
                .status(status)
                .location(request.getLocation())
                .device(request.getDevice())
                .remark(request.getRemark())
                .isFirstClockIn(isFirstClockIn)
                .standardClockInTime(standardClockInTime)
                .standardClockOutTime(standardClockOutTime)
                .message(buildClockInMessage(request.getClockType(), status, clockTime))
                .build();
    }

    @Override
    public List<ClockInRecord> getTodayRecords(Long userId) {
        return getRecordsByDate(userId, LocalDate.now());
    }

    @Override
    public List<ClockInRecord> getRecordsByDate(Long userId, LocalDate date) {
        return recordRepository.findByUserIdAndClockDateAndIsDeletedOrderByClockTimeAsc(userId, date, NOT_DELETED);
    }

    @Override
    public List<ClockInRecord> getRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        // 使用 JPA Specification 實現範圍查詢
        return recordRepository.findAll((root, query, cb) -> {
            return cb.and(
                    cb.equal(root.get("userId"), userId),
                    cb.between(root.get("clockDate"), startDate, endDate),
                    cb.equal(root.get("isDeleted"), NOT_DELETED)
            );
        });
    }

    @Override
    public ClockInSummary getSummaryByDate(Long userId, LocalDate date) {
        return summaryRepository.findByUserIdAndClockDateAndIsDeleted(userId, date, NOT_DELETED)
                .orElse(null);
    }

    @Override
    public List<ClockInSummary> getSummaryByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return summaryRepository.findByUserIdAndClockDateBetweenAndIsDeletedOrderByClockDateAsc(
                userId, startDate, endDate, NOT_DELETED);
    }

    @Override
    public ClockInStatisticsDTO getMonthlyStatistics(Long userId, int year, int month) {
        // 獲取月份起止日期
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 獲取月度匯總數據
        List<ClockInSummary> summaries = getSummaryByDateRange(userId, startDate, endDate);

        // 統計工作日數量
        int workingDays = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (workConfigClient.isWorkDay(date)) {
                workingDays++;
            }
        }

        // 統計各類型出勤情況
        int normalDays = 0;
        int lateDays = 0;
        int earlyLeaveDays = 0;
        int absenceDays = 0;
        int leaveDays = 0;
        int outsideWorkDays = 0;

        // 日統計詳情
        Map<String, ClockInStatisticsDTO.DailyStatistics> dailyDetails = new HashMap<>();

        for (ClockInSummary summary : summaries) {
            // 根據缺勤類型統計
            switch (AbsenceType.fromValue(summary.getAbsenceType())) {
                case NORMAL:
                    normalDays++;
                    break;
                case LATE:
                    lateDays++;
                    break;
                case EARLY_LEAVE:
                    earlyLeaveDays++;
                    break;
                case ABSENCE:
                    absenceDays++;
                    break;
                case LEAVE:
                    leaveDays++;
                    break;
                case OUTSIDE_WORK:
                    outsideWorkDays++;
                    break;
                default:
                    break;
            }

            // 添加日統計詳情
            String dateStr = summary.getClockDate().toString();
            ClockInStatisticsDTO.DailyStatistics dailyStats = ClockInStatisticsDTO.DailyStatistics.builder()
                    .date(dateStr)
                    .isAttendance(summary.getAbsenceType() != AbsenceType.ABSENCE.getValue())
                    .absenceType(summary.getAbsenceType())
                    .clockInTime(summary.getClockInTime() != null ? summary.getClockInTime().toString() : null)
                    .clockInStatus(summary.getClockInStatus())
                    .clockOutTime(summary.getClockOutTime() != null ? summary.getClockOutTime().toString() : null)
                    .clockOutStatus(summary.getClockOutStatus())
                    .workDuration(summary.getWorkDuration())
                    .remark(summary.getRemark())
                    .build();
            dailyDetails.put(dateStr, dailyStats);
        }

        // 計算出勤天數和出勤率
        int attendanceDays = normalDays + lateDays + earlyLeaveDays + outsideWorkDays;
        double attendanceRate = workingDays > 0 ? ((double) attendanceDays / workingDays) * 100 : 0;

        // 構建並返回統計結果
        return ClockInStatisticsDTO.builder()
                .userId(userId)
                .year(year)
                .month(month)
                .attendanceDays(attendanceDays)
                .workingDays(workingDays)
                .attendanceRate(Math.round(attendanceRate * 100) / 100.0) // 保留兩位小數
                .normalDays(normalDays)
                .lateDays(lateDays)
                .earlyLeaveDays(earlyLeaveDays)
                .absenceDays(absenceDays)
                .leaveDays(leaveDays)
                .outsideWorkDays(outsideWorkDays)
                .dailyDetails(dailyDetails)
                .build();
    }

    @Override
    @Transactional
    public ClockInResponse applyForMakeup(Long userId, ClockInRequest request) {
        // 設置為補卡請求
        request.setIsMakeup(true);
        
        // 調用打卡方法
        return clockIn(userId, request);
    }

    @Override
    @Transactional
    public boolean approveMakeup(Long recordId, boolean approved, Long approver, String remark) {
        // 查詢補卡記錄
        ClockInRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException("補卡記錄不存在"));

        // 檢查是否為補卡記錄
        if (record.getStatus() != ClockInStatus.MAKEUP) {
            throw new BusinessException("該記錄不是補卡記錄");
        }

        if (approved) {
            // 批准補卡，更新狀態為正常
            record.setStatus(ClockInStatus.NORMAL);
            record.setRemark(remark + "(補卡已批准)");
        } else {
            // 拒絕補卡，標記為已刪除
            record.setIsDeleted(IS_DELETED);
            record.setRemark(remark + "(補卡已拒絕)");
        }

        // 保存記錄
        recordRepository.save(record);

        // 更新匯總數據
        calculateAndUpdateSummary(record.getUserId(), record.getClockDate());

        log.info("補卡審批: 記錄ID={}, 用戶ID={}, 審批結果={}, 審批人={}", 
                recordId, record.getUserId(), approved ? "批准" : "拒絕", approver);
        
        return true;
    }

    @Override
    @Transactional
    public ClockInSummary calculateAndUpdateSummary(Long userId, LocalDate date) {
        log.debug("計算並更新用戶 {} 在 {} 的打卡匯總", userId, date);

        // 查詢當日打卡記錄
        List<ClockInRecord> records = getRecordsByDate(userId, date);
        
        // 查詢是否已有匯總記錄
        Optional<ClockInSummary> existingSummary = summaryRepository.findByUserIdAndClockDateAndIsDeleted(userId, date, NOT_DELETED);
        
        // 創建或更新匯總記錄
        ClockInSummary summary = existingSummary.orElse(ClockInSummary.builder()
                .userId(userId)
                .clockDate(date)
                .isDeleted(NOT_DELETED)
                .build());

        // 默認缺勤類型為曠工
        AbsenceType absenceType = AbsenceType.ABSENCE;
        
        // 查找上班打卡記錄
        Optional<ClockInRecord> clockInRecord = records.stream()
                .filter(r -> r.getClockType() == ClockInType.CLOCK_IN)
                .findFirst();
        
        // 查找下班打卡記錄
        Optional<ClockInRecord> clockOutRecord = records.stream()
                .filter(r -> r.getClockType() == ClockInType.CLOCK_OUT)
                .findFirst();

        // 更新上班打卡信息
        if (clockInRecord.isPresent()) {
            ClockInRecord inRecord = clockInRecord.get();
            summary.setClockInTime(inRecord.getClockTime());
            summary.setClockInStatus(inRecord.getStatus().getValue());
            
            // 如果有上班打卡，缺勤類型更新為遲到或正常
            absenceType = inRecord.getStatus() == ClockInStatus.LATE ? 
                    AbsenceType.LATE : AbsenceType.NORMAL;
        }

        // 更新下班打卡信息
        if (clockOutRecord.isPresent()) {
            ClockInRecord outRecord = clockOutRecord.get();
            summary.setClockOutTime(outRecord.getClockTime());
            summary.setClockOutStatus(outRecord.getStatus().getValue());
            
            // 如果是早退，缺勤類型更新為早退
            if (outRecord.getStatus() == ClockInStatus.EARLY_LEAVE) {
                absenceType = AbsenceType.EARLY_LEAVE;
            }
        }

        // 計算工作時長（分鐘）
        if (clockInRecord.isPresent() && clockOutRecord.isPresent()) {
            LocalTime inTime = clockInRecord.get().getClockTime();
            LocalTime outTime = clockOutRecord.get().getClockTime();
            int workMinutes = (int) inTime.until(outTime, ChronoUnit.MINUTES);
            
            // 如果工作時間為負數或過大，可能是打卡錯誤
            if (workMinutes < 0 || workMinutes > 24 * 60) {
                log.warn("用戶 {} 在 {} 的工作時長異常: {} 分鐘", userId, date, workMinutes);
                workMinutes = 0;
            }
            
            summary.setWorkDuration(workMinutes);
        }

        // 檢查是否為工作日
        boolean isWorkDay = workConfigClient.isWorkDay(date);
        if (!isWorkDay) {
            // 非工作日，如果有打卡記錄，視為正常
            if (clockInRecord.isPresent() || clockOutRecord.isPresent()) {
                absenceType = AbsenceType.NORMAL;
            } else {
                // 非工作日沒有打卡，不計入缺勤
                absenceType = AbsenceType.NORMAL;
            }
        }

        // 更新缺勤類型
        summary.setAbsenceType(absenceType.getValue());

        // 保存匯總記錄
        return summaryRepository.save(summary);
    }

    @Override
    @Transactional
    public int batchCalculateAndUpdateSummary(LocalDate date) {
        log.info("批量計算並更新 {} 的打卡匯總", date);
        
        // 查詢當日有打卡記錄的用戶
        List<Long> userIds = recordRepository.findAll((root, query, cb) -> {
            query.select(root.get("userId")).distinct(true);
            return cb.and(
                    cb.equal(root.get("clockDate"), date),
                    cb.equal(root.get("isDeleted"), NOT_DELETED)
            );
        }).stream().map(ClockInRecord::getUserId).collect(Collectors.toList());
        
        // 批量更新匯總
        int count = 0;
        for (Long userId : userIds) {
            try {
                calculateAndUpdateSummary(userId, date);
                count++;
            } catch (Exception e) {
                log.error("計算用戶 {} 在 {} 的打卡匯總失敗: {}", userId, date, e.getMessage(), e);
            }
        }
        
        log.info("批量計算完成，共處理 {} 條記錄", count);
        return count;
    }

    /**
     * 構建打卡提示消息
     *
     * @param clockType 打卡類型
     * @param status    打卡狀態
     * @param clockTime 打卡時間
     * @return 提示消息
     */
    private String buildClockInMessage(ClockInType clockType, ClockInStatus status, LocalTime clockTime) {
        StringBuilder message = new StringBuilder();
        
        if (clockType == ClockInType.CLOCK_IN) {
            message.append("上班打卡成功，時間: ").append(clockTime);
        } else {
            message.append("下班打卡成功，時間: ").append(clockTime);
        }
        
        if (status != ClockInStatus.NORMAL) {
            message.append("，狀態: ").append(status.getDesc());
        }
        
        return message.toString();
    }
}
