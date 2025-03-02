package com.clockin.record.util;

import com.clockin.record.dto.ClockRecordDTO;
import com.clockin.record.entity.ClockRecord;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 打卡記錄轉換器
 * <p>
 * 用於在 ClockRecord 實體和 ClockRecordDTO 之間進行轉換
 */
@Component
public class ClockRecordConverter {

    /**
     * 將打卡記錄實體轉換為DTO
     *
     * @param record 打卡記錄實體
     * @return 打卡記錄DTO
     */
    public ClockRecordDTO toDTO(ClockRecord record) {
        if (record == null) {
            return null;
        }
        
        ClockRecordDTO dto = new ClockRecordDTO();
        
        dto.setId(record.getId());
        dto.setUserId(record.getUserId());
        dto.setClockDate(record.getClockDate());
        dto.setClockInTime(record.getClockInTime());
        dto.setClockOutTime(record.getClockOutTime());
        dto.setClockType(record.getClockType());
        dto.setWorkLocation(record.getWorkLocation());
        dto.setIpAddress(record.getIpAddress());
        dto.setDeviceInfo(record.getDeviceInfo());
        dto.setRemark(record.getRemark());
        dto.setIsAbnormal(record.getIsAbnormal());
        dto.setAbnormalReason(record.getAbnormalReason());
        dto.setWorkDuration(record.getWorkDuration());
        dto.setIsHoliday(record.getIsHoliday());
        dto.setHolidayType(record.getHolidayType());
        dto.setCreateTime(record.getCreateTime());
        dto.setUpdateTime(record.getUpdateTime());
        
        // 設置工作時長格式化字符串
        if (record.getWorkDuration() != null) {
            int hours = record.getWorkDuration() / 60;
            int minutes = record.getWorkDuration() % 60;
            dto.setWorkDurationFormatted(String.format("%d小時%d分鐘", hours, minutes));
        }
        
        // 設置打卡類型描述
        if (record.getClockType() != null) {
            switch (record.getClockType()) {
                case 1:
                    dto.setClockTypeDesc("手動打卡");
                    break;
                case 2:
                    dto.setClockTypeDesc("自動打卡");
                    break;
                case 3:
                    dto.setClockTypeDesc("系統修正");
                    break;
                default:
                    dto.setClockTypeDesc("未知");
            }
        }
        
        // 設置假日類型描述
        if (Boolean.TRUE.equals(record.getIsHoliday()) && record.getHolidayType() != null) {
            switch (record.getHolidayType()) {
                case 1:
                    dto.setHolidayTypeDesc("法定假日");
                    break;
                case 2:
                    dto.setHolidayTypeDesc("公司假日");
                    break;
                case 3:
                    dto.setHolidayTypeDesc("個人假日");
                    break;
                default:
                    dto.setHolidayTypeDesc("未知");
            }
        }
        
        return dto;
    }

    /**
     * 將打卡記錄DTO轉換為實體
     *
     * @param dto 打卡記錄DTO
     * @return 打卡記錄實體
     */
    public ClockRecord toEntity(ClockRecordDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ClockRecord record = new ClockRecord();
        
        record.setId(dto.getId());
        record.setUserId(dto.getUserId());
        record.setClockDate(dto.getClockDate());
        record.setClockInTime(dto.getClockInTime());
        record.setClockOutTime(dto.getClockOutTime());
        record.setClockType(dto.getClockType());
        record.setWorkLocation(dto.getWorkLocation());
        record.setIpAddress(dto.getIpAddress());
        record.setDeviceInfo(dto.getDeviceInfo());
        record.setRemark(dto.getRemark());
        record.setIsAbnormal(dto.getIsAbnormal());
        record.setAbnormalReason(dto.getAbnormalReason());
        record.setWorkDuration(dto.getWorkDuration());
        record.setIsHoliday(dto.getIsHoliday());
        record.setHolidayType(dto.getHolidayType());
        
        // 如果有上下班時間，重新計算工作時長
        if (dto.getClockInTime() != null && dto.getClockOutTime() != null) {
            Duration duration = Duration.between(dto.getClockInTime(), dto.getClockOutTime());
            record.setWorkDuration((int) duration.toMinutes());
        }
        
        return record;
    }
}
