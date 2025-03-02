package com.clockin.admin.service;

import com.clockin.admin.entity.UserWorkTimeConfig;
import com.clockin.admin.entity.WorkTimeConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 用戶工作時間配置關聯服務接口
 */
public interface UserWorkTimeConfigService {
    
    /**
     * 創建用戶工作時間配置關聯
     *
     * @param userConfig 用戶工作時間配置關聯
     * @return 創建後的用戶工作時間配置關聯
     */
    UserWorkTimeConfig createUserConfig(UserWorkTimeConfig userConfig);
    
    /**
     * 批量創建用戶工作時間配置關聯
     *
     * @param userConfigs 用戶工作時間配置關聯列表
     * @return 創建後的用戶工作時間配置關聯列表
     */
    List<UserWorkTimeConfig> batchCreateUserConfigs(List<UserWorkTimeConfig> userConfigs);
    
    /**
     * 為用戶分配工作時間配置
     *
     * @param userId      用戶ID
     * @param configId    工作時間配置ID
     * @param startDate   生效開始日期
     * @param endDate     生效結束日期，可為 null 表示永久生效
     * @param isCurrent   是否為當前生效配置
     * @param status      狀態
     * @param remark      備註
     * @return 創建後的用戶工作時間配置關聯
     */
    UserWorkTimeConfig assignConfigToUser(Long userId, Long configId, LocalDate startDate, LocalDate endDate, Boolean isCurrent, Integer status, String remark);
    
    /**
     * 更新用戶工作時間配置關聯
     *
     * @param userConfig 用戶工作時間配置關聯
     * @return 更新後的用戶工作時間配置關聯
     */
    UserWorkTimeConfig updateUserConfig(UserWorkTimeConfig userConfig);
    
    /**
     * 刪除用戶工作時間配置關聯
     *
     * @param id 關聯ID
     */
    void deleteUserConfig(Long id);
    
    /**
     * 根據ID查詢用戶工作時間配置關聯
     *
     * @param id 關聯ID
     * @return 用戶工作時間配置關聯
     */
    Optional<UserWorkTimeConfig> getUserConfigById(Long id);
    
    /**
     * 查詢用戶當前生效的工作時間配置關聯
     *
     * @param userId 用戶ID
     * @return 用戶工作時間配置關聯
     */
    Optional<UserWorkTimeConfig> getCurrentUserConfig(Long userId);
    
    /**
     * 查詢用戶在指定日期生效的工作時間配置關聯
     *
     * @param userId 用戶ID
     * @param date   日期
     * @return 用戶工作時間配置關聯
     */
    Optional<UserWorkTimeConfig> getUserConfigByDate(Long userId, LocalDate date);
    
    /**
     * 查詢用戶在指定日期生效的工作時間配置
     *
     * @param userId 用戶ID
     * @param date   日期
     * @return 工作時間配置
     */
    Optional<WorkTimeConfig> getEffectiveWorkTimeConfig(Long userId, LocalDate date);
    
    /**
     * 查詢用戶的所有工作時間配置關聯
     *
     * @param userId 用戶ID
     * @return 用戶工作時間配置關聯列表
     */
    List<UserWorkTimeConfig> getUserConfigsByUserId(Long userId);
    
    /**
     * 查詢使用指定工作時間配置的所有用戶關聯
     *
     * @param configId 工作時間配置ID
     * @return 用戶工作時間配置關聯列表
     */
    List<UserWorkTimeConfig> getUserConfigsByConfigId(Long configId);
    
    /**
     * 查詢指定日期生效的所有用戶工作時間配置關聯
     *
     * @param date 日期
     * @return 用戶工作時間配置關聯列表
     */
    List<UserWorkTimeConfig> getAllEffectiveUserConfigsByDate(LocalDate date);
    
    /**
     * 設置用戶工作時間配置為當前生效配置
     *
     * @param id 關聯ID
     * @return 設置後的用戶工作時間配置關聯
     */
    UserWorkTimeConfig setAsCurrent(Long id);
    
    /**
     * 啟用用戶工作時間配置關聯
     *
     * @param id 關聯ID
     * @return 啟用後的用戶工作時間配置關聯
     */
    UserWorkTimeConfig enableUserConfig(Long id);
    
    /**
     * 停用用戶工作時間配置關聯
     *
     * @param id 關聯ID
     * @return 停用後的用戶工作時間配置關聯
     */
    UserWorkTimeConfig disableUserConfig(Long id);
    
    /**
     * 分頁查詢用戶工作時間配置關聯
     *
     * @param userId    用戶ID，可為 null
     * @param configId  工作時間配置ID，可為 null
     * @param isCurrent 是否為當前生效配置，可為 null
     * @param status    狀態，可為 null
     * @param pageable  分頁參數
     * @return 分頁數據
     */
    Page<UserWorkTimeConfig> getUserConfigsPage(Long userId, Long configId, Boolean isCurrent, Integer status, Pageable pageable);
}
