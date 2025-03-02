package com.clockin.record.service.impl;

import com.clockin.record.dto.ClockRecordDTO;
import com.clockin.record.dto.SysUserDTO;
import com.clockin.record.entity.ClockRecord;
import com.clockin.record.exception.ApiException;
import com.clockin.record.service.ClockRecordDTOService;
import com.clockin.record.service.ClockRecordService;
import com.clockin.record.service.UserService;
import com.clockin.record.util.ClockRecordConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 打卡記錄DTO服務實現類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClockRecordDTOServiceImpl implements ClockRecordDTOService {

    private final ClockRecordService clockRecordService;
    private final UserService userService;
    private final ClockRecordConverter converter;

    @Override
    @Transactional
    public ClockRecordDTO clockIn(Long userId, LocalDateTime clockInTime, String workLocation, String ipAddress, String deviceInfo, String remark) {
        ClockRecord record = clockRecordService.clockIn(userId, clockInTime, workLocation, ipAddress, deviceInfo, remark);
        return convertToDTO(record);
    }

    @Override
    @Transactional
    public ClockRecordDTO clockOut(Long userId, LocalDateTime clockOutTime, String workLocation, String ipAddress, String deviceInfo, String remark) {
        ClockRecord record = clockRecordService.clockOut(userId, clockOutTime, workLocation, ipAddress, deviceInfo, remark);
        return convertToDTO(record);
    }

    @Override
    public Optional<ClockRecordDTO> getRecordById(Long id) {
        return clockRecordService.getRecordById(id)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<ClockRecordDTO> getRecordByUserAndDate(Long userId, LocalDate clockDate) {
        return clockRecordService.getRecordByUserAndDate(userId, clockDate)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public ClockRecordDTO updateRecord(ClockRecordDTO recordDTO) {
        // 將 DTO 轉換為實體
        ClockRecord record = converter.toEntity(recordDTO);
        // 更新記錄
        record = clockRecordService.updateRecord(record);
        // 將更新後的實體轉換回 DTO
        return convertToDTO(record);
    }

    @Override
    public List<ClockRecordDTO> getRecordsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<ClockRecord> records = clockRecordService.getRecordsByUserAndDateRange(userId, startDate, endDate);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClockRecordDTO> getRecordsPage(Long userId, LocalDate date, Boolean isAbnormal, Pageable pageable) {
        Page<ClockRecord> recordPage = clockRecordService.getRecordsPage(userId, date, isAbnormal, pageable);
        
        List<ClockRecordDTO> dtoList = recordPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, recordPage.getTotalElements());
    }

    @Override
    public Optional<ClockRecordDTO> getLatestRecord(Long userId) {
        return clockRecordService.getLatestRecord(userId)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<ClockRecordDTO> getCurrentUserLatestRecord() {
        SysUserDTO currentUser = userService.getCurrentUser();
        return getLatestRecord(currentUser.getId());
    }

    @Override
    public List<ClockRecordDTO> getTodayRecords() {
        List<ClockRecord> records = clockRecordService.getTodayRecords();
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClockRecordDTO> getAbnormalRecords(LocalDate startDate, LocalDate endDate) {
        List<ClockRecord> records = clockRecordService.getAbnormalRecords(startDate, endDate);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClockRecordDTO currentUserClockIn(LocalDateTime clockInTime, String workLocation, String ipAddress, String deviceInfo, String remark) {
        SysUserDTO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ApiException("未獲取到當前用戶信息");
        }
        
        // 如果未提供打卡時間，則使用當前時間
        if (clockInTime == null) {
            clockInTime = LocalDateTime.now();
        }
        
        return clockIn(currentUser.getId(), clockInTime, workLocation, ipAddress, deviceInfo, remark);
    }

    @Override
    @Transactional
    public ClockRecordDTO currentUserClockOut(LocalDateTime clockOutTime, String workLocation, String ipAddress, String deviceInfo, String remark) {
        SysUserDTO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ApiException("未獲取到當前用戶信息");
        }
        
        // 如果未提供打卡時間，則使用當前時間
        if (clockOutTime == null) {
            clockOutTime = LocalDateTime.now();
        }
        
        return clockOut(currentUser.getId(), clockOutTime, workLocation, ipAddress, deviceInfo, remark);
    }

    /**
     * 將打卡記錄實體轉換為DTO並附加用戶和部門信息
     *
     * @param record 打卡記錄實體
     * @return 打卡記錄DTO
     */
    private ClockRecordDTO convertToDTO(ClockRecord record) {
        ClockRecordDTO dto = converter.toDTO(record);
        
        try {
            // 獲取用戶信息
            SysUserDTO user = userService.getUserById(record.getUserId());
            if (user != null) {
                dto.setUserName(user.getUsername());
                
                // 設置部門信息
                if (user.getDepartmentId() != null) {
                    dto.setDepartmentId(user.getDepartmentId());
                    dto.setDepartmentName(user.getDepartmentName());
                }
            }
        } catch (Exception e) {
            log.warn("獲取用戶或部門信息失敗: {}", e.getMessage());
        }
        
        return dto;
    }
}
