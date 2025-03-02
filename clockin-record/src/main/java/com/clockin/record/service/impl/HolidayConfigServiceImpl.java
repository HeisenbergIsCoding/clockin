package com.clockin.record.service.impl;

import com.clockin.record.dto.HolidayConfigDTO;
import com.clockin.record.entity.HolidayConfig;
import com.clockin.record.exception.BusinessException;
import com.clockin.record.repository.HolidayConfigRepository;
import com.clockin.record.service.HolidayConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 節假日配置服務實現
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayConfigServiceImpl implements HolidayConfigService {

    private final HolidayConfigRepository holidayConfigRepository;

    @Override
    @Cacheable(cacheNames = "workdayCheck", key = "#date.toString()")
    public boolean isWorkday(LocalDate date) {
        // 首先檢查是否在節假日表中定義了該日期
        Optional<HolidayConfig> holidayConfig = holidayConfigRepository.findByHolidayDate(date);
        if (holidayConfig.isPresent()) {
            return holidayConfig.get().getIsWorkday();
        }
        
        // 如果沒有定義，則判斷是否為週末
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "workdayCheck", key = "#config.holidayDate.toString()")
    public HolidayConfig saveHolidayConfig(HolidayConfig config) {
        try {
            return holidayConfigRepository.save(config);
        } catch (DataIntegrityViolationException e) {
            log.error("保存節假日配置失敗，可能是日期重複: {}", e.getMessage());
            throw new BusinessException("保存節假日配置失敗，日期可能重複");
        }
    }

    @Override
    public Optional<HolidayConfig> findById(Long id) {
        return holidayConfigRepository.findById(id);
    }

    @Override
    @Cacheable(cacheNames = "holidayConfig", key = "#date.toString()")
    public Optional<HolidayConfig> findByDate(LocalDate date) {
        return holidayConfigRepository.findByHolidayDate(date);
    }

    @Override
    @Cacheable(cacheNames = "holidaysByYear", key = "#year")
    public List<HolidayConfig> findHolidaysByYear(int year) {
        return holidayConfigRepository.findByYear(year);
    }

    @Override
    @Cacheable(cacheNames = "holidaysByYearAndMonth", key = "#year + '-' + #month")
    public List<HolidayConfig> findHolidaysByYearAndMonth(int year, int month) {
        return holidayConfigRepository.findByYearAndMonth(year, month);
    }

    @Override
    public List<HolidayConfig> findHolidaysBetween(LocalDate startDate, LocalDate endDate) {
        return holidayConfigRepository.findByHolidayDateBetweenOrderByHolidayDate(startDate, endDate);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"workdayCheck", "holidayConfig", "holidaysByYear", "holidaysByYearAndMonth"}, allEntries = true)
    public void deleteById(Long id) {
        holidayConfigRepository.findById(id).ifPresent(config -> {
            holidayConfigRepository.deleteById(id);
            log.info("已刪除ID為 {} 的節假日配置", id);
        });
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"workdayCheck", "holidayConfig", "holidaysByYear", "holidaysByYearAndMonth"}, allEntries = true)
    public List<HolidayConfig> batchImportHolidays(List<HolidayConfigDTO> holidays) {
        List<HolidayConfig> savedConfigs = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        for (HolidayConfigDTO dto : holidays) {
            try {
                HolidayConfig config = HolidayConfig.builder()
                        .holidayDate(dto.getHolidayDate())
                        .holidayName(dto.getHolidayName())
                        .holidayType(dto.getHolidayType())
                        .isWorkday(dto.getIsWorkday())
                        .remark(dto.getRemark())
                        .build();
                
                savedConfigs.add(holidayConfigRepository.save(config));
            } catch (Exception e) {
                String errorMsg = String.format("導入日期 %s 的節假日失敗: %s", dto.getHolidayDate(), e.getMessage());
                log.error(errorMsg, e);
                errors.add(errorMsg);
            }
        }
        
        if (!errors.isEmpty()) {
            log.warn("批量導入節假日時發生 {} 個錯誤", errors.size());
        }
        
        return savedConfigs;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"workdayCheck", "holidayConfig", "holidaysByYear", "holidaysByYearAndMonth"}, 
                key = "#holidayDTO.holidayDate.toString()")
    public HolidayConfig createHolidayConfig(HolidayConfigDTO holidayDTO) {
        // 檢查是否已存在該日期的配置
        if (holidayConfigRepository.findByHolidayDate(holidayDTO.getHolidayDate()).isPresent()) {
            throw new BusinessException("該日期已存在節假日配置");
        }
        
        HolidayConfig config = HolidayConfig.builder()
                .holidayDate(holidayDTO.getHolidayDate())
                .holidayName(holidayDTO.getHolidayName())
                .holidayType(holidayDTO.getHolidayType())
                .isWorkday(holidayDTO.getIsWorkday())
                .remark(holidayDTO.getRemark())
                .build();
        
        return saveHolidayConfig(config);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"workdayCheck", "holidayConfig", "holidaysByYear", "holidaysByYearAndMonth"}, allEntries = true)
    public HolidayConfig updateHolidayConfig(Long id, HolidayConfigDTO holidayDTO) {
        HolidayConfig existingConfig = holidayConfigRepository.findById(id)
                .orElseThrow(() -> new BusinessException("未找到ID為 " + id + " 的節假日配置"));
        
        // 如果更改了日期，檢查新日期是否與現有的其他配置衝突
        if (!existingConfig.getHolidayDate().equals(holidayDTO.getHolidayDate())) {
            holidayConfigRepository.findByHolidayDate(holidayDTO.getHolidayDate())
                    .ifPresent(config -> {
                        if (!config.getId().equals(id)) {
                            throw new BusinessException("新日期已存在其他節假日配置");
                        }
                    });
        }
        
        existingConfig.setHolidayDate(holidayDTO.getHolidayDate());
        existingConfig.setHolidayName(holidayDTO.getHolidayName());
        existingConfig.setHolidayType(holidayDTO.getHolidayType());
        existingConfig.setIsWorkday(holidayDTO.getIsWorkday());
        existingConfig.setRemark(holidayDTO.getRemark());
        
        return saveHolidayConfig(existingConfig);
    }
}
