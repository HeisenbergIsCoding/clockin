package com.clockin.record.service.impl;

import com.clockin.common.util.DateTimeUtil;
import com.clockin.record.entity.ClockRecord;
import com.clockin.record.repository.ClockRecordRepository;
import com.clockin.record.service.ClockRecordService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 打卡記錄服務實現
 */
@Service
@RequiredArgsConstructor
public class ClockRecordServiceImpl implements ClockRecordService {
    
    private final ClockRecordRepository clockRecordRepository;
    
    @Override
    @Transactional
    public ClockRecord createRecord(ClockRecord record) {
        if (record.getClockDate() == null) {
            record.setClockDate(LocalDate.now());
        }
        return clockRecordRepository.save(record);
    }
    
    @Override
    @Transactional
    public ClockRecord clockIn(Long userId, LocalDateTime clockInTime, String workLocation, String ipAddress, String deviceInfo, String remark) {
        LocalDate today = clockInTime.toLocalDate();
        Optional<ClockRecord> existingRecord = clockRecordRepository.findByUserIdAndClockDate(userId, today);
        
        ClockRecord record;
        if (existingRecord.isPresent()) {
            record = existingRecord.get();
            record.setClockInTime(clockInTime);
        } else {
            record = new ClockRecord();
            record.setUserId(userId);
            record.setClockDate(today);
            record.setClockInTime(clockInTime);
            record.setClockType(1); // 手動打卡
        }
        
        record.setWorkLocation(workLocation);
        record.setIpAddress(ipAddress);
        record.setDeviceInfo(deviceInfo);
        record.setRemark(remark);
        
        // 檢查是否異常
        checkAbnormal(record);
        
        return clockRecordRepository.save(record);
    }
    
    @Override
    @Transactional
    public ClockRecord clockOut(Long userId, LocalDateTime clockOutTime, String workLocation, String ipAddress, String deviceInfo, String remark) {
        LocalDate today = clockOutTime.toLocalDate();
        Optional<ClockRecord> existingRecord = clockRecordRepository.findByUserIdAndClockDate(userId, today);
        
        if (existingRecord.isEmpty()) {
            // 如果今天沒有打卡記錄，先創建一個
            ClockRecord record = new ClockRecord();
            record.setUserId(userId);
            record.setClockDate(today);
            record.setClockOutTime(clockOutTime);
            record.setClockType(1); // 手動打卡
            record.setWorkLocation(workLocation);
            record.setIpAddress(ipAddress);
            record.setDeviceInfo(deviceInfo);
            record.setRemark(remark);
            record.setIsAbnormal(true);
            record.setAbnormalReason("缺少上班打卡記錄");
            
            return clockRecordRepository.save(record);
        }
        
        ClockRecord record = existingRecord.get();
        record.setClockOutTime(clockOutTime);
        record.setWorkLocation(workLocation);
        record.setIpAddress(ipAddress);
        record.setDeviceInfo(deviceInfo);
        
        if (record.getRemark() == null || record.getRemark().isEmpty()) {
            record.setRemark(remark);
        } else if (remark != null && !remark.isEmpty()) {
            record.setRemark(record.getRemark() + "; " + remark);
        }
        
        // 計算工作時長
        if (record.getClockInTime() != null) {
            Duration duration = Duration.between(record.getClockInTime(), clockOutTime);
            record.setWorkDuration((int) duration.toMinutes());
        }
        
        // 檢查是否異常
        checkAbnormal(record);
        
        return clockRecordRepository.save(record);
    }
    
    @Override
    public Optional<ClockRecord> getRecordById(Long id) {
        return clockRecordRepository.findById(id);
    }
    
    @Override
    public Optional<ClockRecord> getRecordByUserAndDate(Long userId, LocalDate clockDate) {
        return clockRecordRepository.findByUserIdAndClockDate(userId, clockDate);
    }
    
    @Override
    @Transactional
    public ClockRecord updateRecord(ClockRecord record) {
        // 重新計算工作時長
        if (record.getClockInTime() != null && record.getClockOutTime() != null) {
            Duration duration = Duration.between(record.getClockInTime(), record.getClockOutTime());
            record.setWorkDuration((int) duration.toMinutes());
        }
        
        // 檢查是否異常
        checkAbnormal(record);
        
        return clockRecordRepository.save(record);
    }
    
    @Override
    @Transactional
    public void deleteRecord(Long id) {
        clockRecordRepository.deleteById(id);
    }
    
    @Override
    public List<ClockRecord> getRecordsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return clockRecordRepository.findByUserIdAndClockDateBetweenOrderByClockDateAsc(userId, startDate, endDate);
    }
    
    @Override
    public Page<ClockRecord> getRecordsPage(Long userId, LocalDate date, Boolean isAbnormal, Pageable pageable) {
        Specification<ClockRecord> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            
            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("clockDate"), date));
            }
            
            if (isAbnormal != null) {
                predicates.add(criteriaBuilder.equal(root.get("isAbnormal"), isAbnormal));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return clockRecordRepository.findAll(spec, pageable);
    }
    
    @Override
    @Transactional
    public List<ClockRecord> batchImportRecords(List<ClockRecord> records) {
        records.forEach(this::checkAbnormal);
        return clockRecordRepository.saveAll(records);
    }
    
    @Override
    public Integer getTotalWorkDuration(Long userId, LocalDate startDate, LocalDate endDate) {
        Integer totalDuration = clockRecordRepository.sumWorkDurationByUserIdAndDateRange(userId, startDate, endDate);
        return totalDuration != null ? totalDuration : 0;
    }
    
    @Override
    public Optional<ClockRecord> getLatestRecord(Long userId) {
        return clockRecordRepository.findLatestByUserId(userId);
    }
    
    @Override
    public List<ClockRecord> getTodayRecords() {
        return clockRecordRepository.findByClockDateOrderByUserId(LocalDate.now());
    }
    
    @Override
    public List<ClockRecord> getAbnormalRecords(LocalDate startDate, LocalDate endDate) {
        return clockRecordRepository.findByIsAbnormalTrueAndClockDateBetweenOrderByClockDateDesc(startDate, endDate);
    }
    
    /**
     * 檢查打卡記錄是否異常
     *
     * @param record 打卡記錄
     */
    private void checkAbnormal(ClockRecord record) {
        boolean isAbnormal = false;
        StringBuilder reason = new StringBuilder();
        
        // 檢查是否為假日
        if (record.getIsHoliday() != null && record.getIsHoliday()) {
            // 假日打卡，不標記為異常
            record.setIsAbnormal(false);
            record.setAbnormalReason(null);
            return;
        }
        
        // 檢查上班打卡是否缺失
        if (record.getClockInTime() == null && record.getClockOutTime() != null) {
            isAbnormal = true;
            reason.append("缺少上班打卡記錄; ");
        }
        
        // 檢查下班打卡是否缺失
        if (record.getClockInTime() != null && record.getClockOutTime() == null) {
            isAbnormal = true;
            reason.append("缺少下班打卡記錄; ");
        }
        
        // 檢查打卡時間是否異常 (例如：工作時間過短或過長)
        if (record.getClockInTime() != null && record.getClockOutTime() != null) {
            // 計算工作時長
            Duration duration = Duration.between(record.getClockInTime(), record.getClockOutTime());
            int workMinutes = (int) duration.toMinutes();
            record.setWorkDuration(workMinutes);
            
            // 例如：工作時間少於 4 小時或超過 12 小時視為異常
            if (workMinutes < 240) {
                isAbnormal = true;
                reason.append("工作時間過短 (").append(workMinutes / 60).append("小時").append(workMinutes % 60).append("分鐘); ");
            } else if (workMinutes > 720) {
                isAbnormal = true;
                reason.append("工作時間過長 (").append(workMinutes / 60).append("小時").append(workMinutes % 60).append("分鐘); ");
            }
        }
        
        // 設置異常狀態和原因
        record.setIsAbnormal(isAbnormal);
        if (isAbnormal) {
            if (reason.length() > 0) {
                // 移除末尾的分號和空格
                reason.setLength(reason.length() - 2);
            }
            record.setAbnormalReason(reason.toString());
        } else {
            record.setAbnormalReason(null);
        }
    }
}
