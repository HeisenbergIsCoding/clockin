package com.clockin.record.service.impl;

import com.clockin.record.dto.WorkTimeConfigDTO;
import com.clockin.record.entity.WorkTimeConfig;
import com.clockin.record.exception.BusinessException;
import com.clockin.record.repository.WorkTimeConfigRepository;
import com.clockin.record.service.WorkTimeConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 工作時間配置服務實現
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTimeConfigServiceImpl implements WorkTimeConfigService {

    private final WorkTimeConfigRepository workTimeConfigRepository;
    private final HolidayConfigServiceImpl holidayConfigService;

    @Override
    @Cacheable(cacheNames = "workTimeConfig", key = "'user_' + #userId + '_date_' + #date.toString()")
    public WorkTimeConfig getWorkTimeConfig(Long userId, LocalDate date) {
        // 首先查詢用戶特定配置
        List<WorkTimeConfig> configs = workTimeConfigRepository.findEffectiveConfigsForUserOnDate(userId, null, date);
        
        if (!configs.isEmpty()) {
            WorkTimeConfig config = configs.get(0);
            log.debug("找到用戶 {} 在 {} 的工作時間配置: {}", userId, date, config);
            return config;
        }
        
        // 如果沒有找到用戶特定配置，則查詢部門配置
        // 這裡假設用戶所屬部門的邏輯，真實場景可能需要通過其他服務獲取用戶部門
        Long departmentId = getDepartmentIdForUser(userId);
        if (departmentId != null) {
            configs = workTimeConfigRepository.findEffectiveConfigsForUserOnDate(null, departmentId, date);
            if (!configs.isEmpty()) {
                WorkTimeConfig config = configs.get(0);
                log.debug("找到部門 {} 在 {} 的工作時間配置: {}", departmentId, date, config);
                return config;
            }
        }
        
        // 如果還沒找到，則查詢全局配置
        configs = workTimeConfigRepository.findEffectiveConfigsOnDate(date);
        if (!configs.isEmpty()) {
            for (WorkTimeConfig config : configs) {
                if (config.getUserId() == null && config.getDepartmentId() == null) {
                    log.debug("找到全局在 {} 的工作時間配置: {}", date, config);
                    return config;
                }
            }
        }
        
        // 如果沒有找到任何配置，則返回默認配置
        log.debug("未找到用戶 {} 在 {} 的工作時間配置，使用默認配置", userId, date);
        return createDefaultConfig();
    }

    @Override
    public LocalTime getMorningStartTime(Long userId, LocalDate date) {
        return getWorkTimeConfig(userId, date).getMorningStartTime();
    }

    @Override
    public LocalTime getMorningEndTime(Long userId, LocalDate date) {
        return getWorkTimeConfig(userId, date).getMorningEndTime();
    }

    @Override
    public LocalTime getAfternoonStartTime(Long userId, LocalDate date) {
        return getWorkTimeConfig(userId, date).getAfternoonStartTime();
    }

    @Override
    public LocalTime getAfternoonEndTime(Long userId, LocalDate date) {
        return getWorkTimeConfig(userId, date).getAfternoonEndTime();
    }

    @Override
    public int getFlexibleMinutes(Long userId, LocalDate date) {
        return getWorkTimeConfig(userId, date).getFlexibleMinutes();
    }

    @Override
    public boolean isWorkday(LocalDate date) {
        return holidayConfigService.isWorkday(date);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "workTimeConfig", allEntries = true)
    public WorkTimeConfig saveWorkTimeConfig(WorkTimeConfig config) {
        return workTimeConfigRepository.save(config);
    }

    @Override
    public List<WorkTimeConfig> findWorkTimeConfigs(Long userId, Long departmentId, Boolean isActive, LocalDate effectiveDate) {
        if (userId != null) {
            if (isActive != null) {
                return workTimeConfigRepository.findByUserIdAndIsActiveOrderByPriorityDesc(userId, isActive);
            } else {
                return workTimeConfigRepository.findByUserIdOrderByPriorityDesc(userId);
            }
        } else if (departmentId != null) {
            if (isActive != null) {
                return workTimeConfigRepository.findByDepartmentIdAndIsActiveOrderByPriorityDesc(departmentId, isActive);
            } else {
                return workTimeConfigRepository.findByDepartmentIdOrderByPriorityDesc(departmentId);
            }
        } else if (effectiveDate != null) {
            return workTimeConfigRepository.findEffectiveConfigsOnDate(effectiveDate);
        } else {
            return workTimeConfigRepository.findGlobalConfigs();
        }
    }

    @Override
    public Optional<WorkTimeConfig> findById(Long id) {
        return workTimeConfigRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "workTimeConfig", allEntries = true)
    public void deleteById(Long id) {
        workTimeConfigRepository.deleteById(id);
    }

    @Override
    public boolean isTimeInAllowedRange(Long userId, LocalDate date, LocalTime time, boolean allowedLateness) {
        WorkTimeConfig config = getWorkTimeConfig(userId, date);
        
        LocalTime morningStart = config.getMorningStartTime();
        LocalTime morningEnd = config.getMorningEndTime();
        LocalTime afternoonStart = config.getAfternoonStartTime();
        LocalTime afternoonEnd = config.getAfternoonEndTime();
        
        if (allowedLateness) {
            int flexibleMinutes = config.getFlexibleMinutes();
            morningStart = morningStart.plusMinutes(flexibleMinutes);
        }
        
        // 檢查是否在上午工作時間範圍內
        boolean isInMorningRange = !time.isBefore(morningStart) && !time.isAfter(morningEnd);
        
        // 檢查是否在下午工作時間範圍內
        boolean isInAfternoonRange = !time.isBefore(afternoonStart) && !time.isAfter(afternoonEnd);
        
        return isInMorningRange || isInAfternoonRange;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "workTimeConfig", allEntries = true)
    public WorkTimeConfig createWorkTimeConfig(WorkTimeConfigDTO configDTO) {
        // 檢查時間邏輯
        validateWorkTimeLogic(configDTO);
        
        WorkTimeConfig config = WorkTimeConfig.builder()
                .userId(configDTO.getUserId())
                .departmentId(configDTO.getDepartmentId())
                .morningStartTime(configDTO.getMorningStartTime())
                .morningEndTime(configDTO.getMorningEndTime())
                .afternoonStartTime(configDTO.getAfternoonStartTime())
                .afternoonEndTime(configDTO.getAfternoonEndTime())
                .flexibleMinutes(configDTO.getFlexibleMinutes())
                .effectiveDate(configDTO.getEffectiveDate())
                .expiredDate(configDTO.getExpiredDate())
                .isActive(configDTO.getIsActive())
                .priority(configDTO.getPriority())
                .build();
        
        return saveWorkTimeConfig(config);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "workTimeConfig", allEntries = true)
    public WorkTimeConfig updateWorkTimeConfig(Long id, WorkTimeConfigDTO configDTO) {
        // 檢查時間邏輯
        validateWorkTimeLogic(configDTO);
        
        WorkTimeConfig existingConfig = workTimeConfigRepository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到ID為 " + id + " 的工作時間配置"));
        
        existingConfig.setUserId(configDTO.getUserId());
        existingConfig.setDepartmentId(configDTO.getDepartmentId());
        existingConfig.setMorningStartTime(configDTO.getMorningStartTime());
        existingConfig.setMorningEndTime(configDTO.getMorningEndTime());
        existingConfig.setAfternoonStartTime(configDTO.getAfternoonStartTime());
        existingConfig.setAfternoonEndTime(configDTO.getAfternoonEndTime());
        existingConfig.setFlexibleMinutes(configDTO.getFlexibleMinutes());
        existingConfig.setEffectiveDate(configDTO.getEffectiveDate());
        existingConfig.setExpiredDate(configDTO.getExpiredDate());
        existingConfig.setIsActive(configDTO.getIsActive());
        existingConfig.setPriority(configDTO.getPriority());
        
        return saveWorkTimeConfig(existingConfig);
    }

    /**
     * 獲取用戶所屬部門ID
     * 實際場景中可能需要調用用戶服務獲取
     *
     * @param userId 用戶ID
     * @return 部門ID
     */
    private Long getDepartmentIdForUser(Long userId) {
        // 這裡為了演示，直接返回null
        // 實際場景中，可能需要通過Feign客戶端調用用戶服務獲取用戶所屬部門
        return null;
    }

    /**
     * 創建默認工作時間配置
     *
     * @return 默認工作時間配置
     */
    private WorkTimeConfig createDefaultConfig() {
        return WorkTimeConfig.builder()
                .morningStartTime(LocalTime.of(9, 0))
                .morningEndTime(LocalTime.of(12, 0))
                .afternoonStartTime(LocalTime.of(13, 0))
                .afternoonEndTime(LocalTime.of(18, 0))
                .flexibleMinutes(10)
                .effectiveDate(LocalDate.of(2000, 1, 1))
                .isActive(true)
                .priority(0)
                .build();
    }

    /**
     * 驗證工作時間邏輯
     *
     * @param configDTO 工作時間配置DTO
     */
    private void validateWorkTimeLogic(WorkTimeConfigDTO configDTO) {
        // 檢查上午時間段
        if (configDTO.getMorningStartTime().isAfter(configDTO.getMorningEndTime())) {
            throw new BusinessException("上午上班時間必須早於上午下班時間");
        }
        
        // 檢查下午時間段
        if (configDTO.getAfternoonStartTime().isAfter(configDTO.getAfternoonEndTime())) {
            throw new BusinessException("下午上班時間必須早於下午下班時間");
        }
        
        // 檢查上午和下午時間段不重疊
        if (!configDTO.getMorningEndTime().isBefore(configDTO.getAfternoonStartTime())) {
            throw new BusinessException("上午下班時間必須早於下午上班時間");
        }
        
        // 檢查生效日期和過期日期
        if (configDTO.getExpiredDate() != null && 
                configDTO.getEffectiveDate().isAfter(configDTO.getExpiredDate())) {
            throw new BusinessException("生效日期必須早於或等於過期日期");
        }
    }
}
