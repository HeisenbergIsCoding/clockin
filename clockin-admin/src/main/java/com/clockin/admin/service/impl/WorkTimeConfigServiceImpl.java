package com.clockin.admin.service.impl;

import com.clockin.admin.entity.WorkTimeConfig;
import com.clockin.admin.repository.WorkTimeConfigRepository;
import com.clockin.admin.service.WorkTimeConfigService;
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

import java.time.LocalTime;
import java.util.ArrayList;
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
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public WorkTimeConfig createConfig(WorkTimeConfig config) {
        // 如果設置為預設配置，先取消其他預設配置
        if (config.getIsDefault() != null && config.getIsDefault()) {
            clearDefaultConfig();
        }
        
        return workTimeConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public WorkTimeConfig updateConfig(WorkTimeConfig config) {
        // 如果設置為預設配置，先取消其他預設配置
        if (config.getIsDefault() != null && config.getIsDefault()) {
            clearDefaultConfig();
        }
        
        return workTimeConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public void deleteConfig(Long id) {
        workTimeConfigRepository.deleteById(id);
    }
    
    @Override
    @Cacheable(value = "workTimeConfig", key = "'id_' + #id")
    public Optional<WorkTimeConfig> getConfigById(Long id) {
        return workTimeConfigRepository.findById(id);
    }
    
    @Override
    @Cacheable(value = "workTimeConfig", key = "'name_' + #configName")
    public Optional<WorkTimeConfig> getConfigByName(String configName) {
        return workTimeConfigRepository.findByConfigName(configName);
    }
    
    @Override
    @Cacheable(value = "workTimeConfig", key = "'default'")
    public Optional<WorkTimeConfig> getDefaultConfig() {
        return workTimeConfigRepository.findByIsDefaultTrue();
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public WorkTimeConfig setAsDefault(Long id) {
        // 先取消其他預設配置
        clearDefaultConfig();
        
        // 設置當前配置為預設配置
        Optional<WorkTimeConfig> configOptional = workTimeConfigRepository.findById(id);
        if (configOptional.isEmpty()) {
            throw new IllegalArgumentException("工作時間配置不存在");
        }
        
        WorkTimeConfig config = configOptional.get();
        config.setIsDefault(true);
        
        // 如果配置被禁用，則啟用它
        if (config.getStatus() == 0) {
            config.setStatus(1);
        }
        
        return workTimeConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public WorkTimeConfig enableConfig(Long id) {
        Optional<WorkTimeConfig> configOptional = workTimeConfigRepository.findById(id);
        if (configOptional.isEmpty()) {
            throw new IllegalArgumentException("工作時間配置不存在");
        }
        
        WorkTimeConfig config = configOptional.get();
        config.setStatus(1);
        
        return workTimeConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public WorkTimeConfig disableConfig(Long id) {
        Optional<WorkTimeConfig> configOptional = workTimeConfigRepository.findById(id);
        if (configOptional.isEmpty()) {
            throw new IllegalArgumentException("工作時間配置不存在");
        }
        
        WorkTimeConfig config = configOptional.get();
        
        // 預設配置不能停用
        if (config.getIsDefault()) {
            throw new IllegalArgumentException("預設配置不能停用");
        }
        
        config.setStatus(0);
        return workTimeConfigRepository.save(config);
    }
    
    @Override
    public List<WorkTimeConfig> getAllConfigs() {
        return workTimeConfigRepository.findAll();
    }
    
    @Override
    @Cacheable(value = "workTimeConfig", key = "'allEnabled'")
    public List<WorkTimeConfig> getAllEnabledConfigs() {
        return workTimeConfigRepository.findByStatus(1);
    }
    
    @Override
    public Page<WorkTimeConfig> getConfigsPage(String configName, Integer status, Pageable pageable) {
        Specification<WorkTimeConfig> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (configName != null && !configName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("configName"), "%" + configName + "%"));
            }
            
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return workTimeConfigRepository.findAll(spec, pageable);
    }
    
    @Override
    public boolean hasDefaultConfig() {
        log.debug("檢查是否存在預設工作時間配置");
        return workTimeConfigRepository.countByIsDefaultTrue() > 0;
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "workTimeConfig", allEntries = true)
    public WorkTimeConfig createDefaultConfig() {
        log.info("創建預設工作時間配置");
        
        // 先檢查是否已存在預設配置
        if (hasDefaultConfig()) {
            log.info("已存在預設工作時間配置，不再創建");
            return getDefaultConfig().orElse(null);
        }
        
        // 創建預設配置
        WorkTimeConfig config = new WorkTimeConfig();
        config.setConfigName("預設工作時間");
        config.setWorkStartTime(LocalTime.of(9, 0)); // 上午9:00
        config.setLunchStartTime(LocalTime.of(12, 0));  // 中午12:00
        config.setLunchEndTime(LocalTime.of(13, 0)); // 下午1:00
        config.setWorkEndTime(LocalTime.of(18, 0));   // 下午6:00
        config.setStandardWorkDuration(480);   // 標準工作8小時
        config.setMinWorkDuration(450);  // 最少7.5小時
        config.setMaxWorkDuration(600);  // 最多10小時
        config.setFlexibleDuration(30);  // 彈性30分鐘
        config.setEnableLunchTime(true);
        config.setEnableFlexibleTime(true);
        config.setIsDefault(true);
        config.setStatus(1);         // 啟用狀態
        config.setRemark("系統自動創建的預設工作時間配置");
        
        return workTimeConfigRepository.save(config);
    }
    
    /**
     * 清除所有預設配置
     */
    private void clearDefaultConfig() {
        Optional<WorkTimeConfig> defaultConfig = workTimeConfigRepository.findByIsDefaultTrue();
        if (defaultConfig.isPresent()) {
            WorkTimeConfig config = defaultConfig.get();
            config.setIsDefault(false);
            workTimeConfigRepository.save(config);
        }
    }
}
