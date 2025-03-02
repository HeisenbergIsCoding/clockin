package com.clockin.admin.service.impl;

import com.clockin.admin.entity.Holiday;
import com.clockin.admin.repository.HolidayRepository;
import com.clockin.admin.service.HolidayService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 假日配置服務實現
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    
    private final HolidayRepository holidayRepository;
    
    @Override
    @Transactional
    public Holiday createHoliday(Holiday holiday) {
        // 設置年、月、日欄位
        if (holiday.getHolidayDate() != null) {
            setYearMonthDayFromDate(holiday);
        }
        
        return holidayRepository.save(holiday);
    }
    
    @Override
    @Transactional
    public List<Holiday> batchCreateHolidays(List<Holiday> holidays) {
        // 設置年、月、日欄位
        holidays.forEach(holiday -> {
            if (holiday.getHolidayDate() != null) {
                setYearMonthDayFromDate(holiday);
            }
        });
        
        return holidayRepository.saveAll(holidays);
    }
    
    @Override
    @Transactional
    public Holiday updateHoliday(Holiday holiday) {
        // 設置年、月、日欄位
        if (holiday.getHolidayDate() != null) {
            setYearMonthDayFromDate(holiday);
        }
        
        return holidayRepository.save(holiday);
    }
    
    @Override
    @Transactional
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }
    
    @Override
    public Optional<Holiday> getHolidayById(Long id) {
        return holidayRepository.findById(id);
    }
    
    @Override
    public Optional<Holiday> getHolidayByDate(LocalDate date) {
        return holidayRepository.findByHolidayDate(date);
    }
    
    @Override
    @Cacheable(value = "holiday", key = "'year_' + #year")
    public List<Holiday> getHolidaysByYear(Integer year) {
        log.debug("查詢 {} 年所有假日數據", year);
        return holidayRepository.findByYearOrderByHolidayDateAsc(year);
    }
    
    @Override
    @Cacheable(value = "holiday", key = "'year_' + #year + '_month_' + #month")
    public List<Holiday> getHolidaysByYearAndMonth(Integer year, Integer month) {
        log.debug("查詢 {}-{} 月份假日數據", year, month);
        return holidayRepository.findByYearAndMonthOrderByDayAsc(year, month);
    }
    
    @Override
    public List<Holiday> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByHolidayDateBetweenOrderByHolidayDateAsc(startDate, endDate);
    }
    
    @Override
    public List<Holiday> getHolidaysByTypeAndYear(Integer holidayType, Integer year) {
        return holidayRepository.findByHolidayTypeAndYearOrderByHolidayDateAsc(holidayType, year);
    }
    
    @Override
    @Cacheable(value = "holiday", key = "'date_' + #date")
    public boolean isHoliday(LocalDate date) {
        Integer count = holidayRepository.countByHolidayDateAndStatusActive(date);
        return count != null && count > 0;
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "holiday", allEntries = true)
    public List<Holiday> batchImportHolidays(List<Holiday> holidays) {
        log.info("批量匯入 {} 條假日記錄", holidays.size());
        
        // 設置年、月、日欄位
        holidays.forEach(holiday -> {
            if (holiday.getHolidayDate() != null) {
                setYearMonthDayFromDate(holiday);
            }
        });
        
        return holidayRepository.saveAll(holidays);
    }
    
    @Override
    public Page<Holiday> getHolidaysPage(Integer year, Integer month, Integer holidayType, Pageable pageable) {
        Specification<Holiday> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("year"), year));
            }
            
            if (month != null) {
                predicates.add(criteriaBuilder.equal(root.get("month"), month));
            }
            
            if (holidayType != null) {
                predicates.add(criteriaBuilder.equal(root.get("holidayType"), holidayType));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return holidayRepository.findAll(spec, pageable);
    }
    
    @Override
    public int countHolidaysByYearAndMonth(Integer year, Integer month) {
        log.debug("統計 {}-{} 月份假日數量", year, month);
        Long count = holidayRepository.countByYearAndMonthAndStatusActive(year, month);
        return count != null ? count.intValue() : 0;
    }
    
    @Override
    @CacheEvict(value = "holiday", allEntries = true)
    public void refreshHolidayCache(Integer year) {
        log.info("刷新 {} 年假日緩存", year);
        // 通過清除緩存，下次查詢時會自動從數據庫重新加載
    }
    
    /**
     * 根據日期設置年、月、日欄位
     *
     * @param holiday 假日配置
     */
    private void setYearMonthDayFromDate(Holiday holiday) {
        LocalDate date = holiday.getHolidayDate();
        holiday.setYear(date.getYear());
        holiday.setMonth(date.getMonthValue());
        holiday.setDay(date.getDayOfMonth());
    }
}
