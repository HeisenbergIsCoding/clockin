package com.clockin.admin.service.impl;

import com.clockin.admin.entity.UserWorkTimeConfig;
import com.clockin.admin.entity.WorkTimeConfig;
import com.clockin.admin.repository.UserWorkTimeConfigRepository;
import com.clockin.admin.repository.WorkTimeConfigRepository;
import com.clockin.admin.service.UserWorkTimeConfigService;
import com.clockin.admin.service.WorkTimeConfigService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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
 * 用戶工作時間配置關聯服務實現
 */
@Service
@RequiredArgsConstructor
public class UserWorkTimeConfigServiceImpl implements UserWorkTimeConfigService {
    
    private final UserWorkTimeConfigRepository userWorkTimeConfigRepository;
    private final WorkTimeConfigRepository workTimeConfigRepository;
    private final WorkTimeConfigService workTimeConfigService;
    
    @Override
    @Transactional
    public UserWorkTimeConfig createUserConfig(UserWorkTimeConfig userConfig) {
        // 如果設置為當前生效配置，先取消用戶的其他當前生效配置
        if (userConfig.getIsCurrent() != null && userConfig.getIsCurrent()) {
            clearCurrentUserConfig(userConfig.getUserId());
        }
        
        return userWorkTimeConfigRepository.save(userConfig);
    }
    
    @Override
    @Transactional
    public List<UserWorkTimeConfig> batchCreateUserConfigs(List<UserWorkTimeConfig> userConfigs) {
        // 處理每個用戶的當前生效配置
        userConfigs.forEach(userConfig -> {
            if (userConfig.getIsCurrent() != null && userConfig.getIsCurrent()) {
                clearCurrentUserConfig(userConfig.getUserId());
            }
        });
        
        return userWorkTimeConfigRepository.saveAll(userConfigs);
    }
    
    @Override
    @Transactional
    public UserWorkTimeConfig assignConfigToUser(Long userId, Long configId, LocalDate startDate, LocalDate endDate, Boolean isCurrent, Integer status, String remark) {
        // 檢查工作時間配置是否存在
        WorkTimeConfig workTimeConfig = workTimeConfigRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("工作時間配置不存在"));
        
        // 如果設置為當前生效配置，先取消用戶的其他當前生效配置
        if (isCurrent != null && isCurrent) {
            clearCurrentUserConfig(userId);
        }
        
        // 創建用戶工作時間配置關聯
        UserWorkTimeConfig userConfig = new UserWorkTimeConfig();
        userConfig.setUserId(userId);
        userConfig.setConfigId(configId);
        userConfig.setEffectiveStartDate(startDate);
        userConfig.setEffectiveEndDate(endDate);
        userConfig.setIsCurrent(isCurrent);
        userConfig.setStatus(status);
        userConfig.setRemark(remark);
        
        return userWorkTimeConfigRepository.save(userConfig);
    }
    
    @Override
    @Transactional
    public UserWorkTimeConfig updateUserConfig(UserWorkTimeConfig userConfig) {
        // 如果設置為當前生效配置，先取消用戶的其他當前生效配置
        if (userConfig.getIsCurrent() != null && userConfig.getIsCurrent()) {
            clearCurrentUserConfig(userConfig.getUserId());
        }
        
        return userWorkTimeConfigRepository.save(userConfig);
    }
    
    @Override
    @Transactional
    public void deleteUserConfig(Long id) {
        userWorkTimeConfigRepository.deleteById(id);
    }
    
    @Override
    public Optional<UserWorkTimeConfig> getUserConfigById(Long id) {
        return userWorkTimeConfigRepository.findById(id);
    }
    
    @Override
    public Optional<UserWorkTimeConfig> getCurrentUserConfig(Long userId) {
        return userWorkTimeConfigRepository.findByUserIdAndIsCurrentTrue(userId);
    }
    
    @Override
    public Optional<UserWorkTimeConfig> getUserConfigByDate(Long userId, LocalDate date) {
        return userWorkTimeConfigRepository.findEffectiveConfigByUserIdAndDate(userId, date);
    }
    
    @Override
    public Optional<WorkTimeConfig> getEffectiveWorkTimeConfig(Long userId, LocalDate date) {
        // 查詢用戶在指定日期生效的工作時間配置關聯
        Optional<UserWorkTimeConfig> userConfigOpt = userWorkTimeConfigRepository.findEffectiveConfigByUserIdAndDate(userId, date);
        
        if (userConfigOpt.isPresent()) {
            // 如果存在用戶配置關聯，則查詢對應的工作時間配置
            Long configId = userConfigOpt.get().getConfigId();
            return workTimeConfigRepository.findById(configId);
        } else {
            // 如果不存在用戶配置關聯，則返回預設工作時間配置
            return workTimeConfigService.getDefaultConfig();
        }
    }
    
    @Override
    public List<UserWorkTimeConfig> getUserConfigsByUserId(Long userId) {
        return userWorkTimeConfigRepository.findByUserIdOrderByEffectiveStartDateDesc(userId);
    }
    
    @Override
    public List<UserWorkTimeConfig> getUserConfigsByConfigId(Long configId) {
        return userWorkTimeConfigRepository.findByConfigIdOrderByUserId(configId);
    }
    
    @Override
    public List<UserWorkTimeConfig> getAllEffectiveUserConfigsByDate(LocalDate date) {
        return userWorkTimeConfigRepository.findAllEffectiveConfigsByDate(date);
    }
    
    @Override
    @Transactional
    public UserWorkTimeConfig setAsCurrent(Long id) {
        // 獲取用戶工作時間配置關聯
        UserWorkTimeConfig userConfig = userWorkTimeConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用戶工作時間配置關聯不存在"));
        
        // 取消用戶的其他當前生效配置
        clearCurrentUserConfig(userConfig.getUserId());
        
        // 設置為當前生效配置
        userConfig.setIsCurrent(true);
        return userWorkTimeConfigRepository.save(userConfig);
    }
    
    @Override
    @Transactional
    public UserWorkTimeConfig enableUserConfig(Long id) {
        UserWorkTimeConfig userConfig = userWorkTimeConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用戶工作時間配置關聯不存在"));
        
        userConfig.setStatus(1);
        return userWorkTimeConfigRepository.save(userConfig);
    }
    
    @Override
    @Transactional
    public UserWorkTimeConfig disableUserConfig(Long id) {
        UserWorkTimeConfig userConfig = userWorkTimeConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用戶工作時間配置關聯不存在"));
        
        // 如果是當前生效配置，則不允許停用
        if (userConfig.getIsCurrent()) {
            throw new IllegalArgumentException("當前生效配置不能停用");
        }
        
        userConfig.setStatus(0);
        return userWorkTimeConfigRepository.save(userConfig);
    }
    
    @Override
    public Page<UserWorkTimeConfig> getUserConfigsPage(Long userId, Long configId, Boolean isCurrent, Integer status, Pageable pageable) {
        Specification<UserWorkTimeConfig> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            
            if (configId != null) {
                predicates.add(criteriaBuilder.equal(root.get("configId"), configId));
            }
            
            if (isCurrent != null) {
                predicates.add(criteriaBuilder.equal(root.get("isCurrent"), isCurrent));
            }
            
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return userWorkTimeConfigRepository.findAll(spec, pageable);
    }
    
    /**
     * 清除用戶的當前生效配置
     *
     * @param userId 用戶ID
     */
    private void clearCurrentUserConfig(Long userId) {
        Optional<UserWorkTimeConfig> currentUserConfig = userWorkTimeConfigRepository.findByUserIdAndIsCurrentTrue(userId);
        if (currentUserConfig.isPresent()) {
            UserWorkTimeConfig userConfig = currentUserConfig.get();
            userConfig.setIsCurrent(false);
            userWorkTimeConfigRepository.save(userConfig);
        }
    }
}
