package com.clockin.admin.service;

import com.clockin.admin.entity.WorkTimeConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 工作時間配置服務接口
 */
public interface WorkTimeConfigService {
    
    /**
     * 創建工作時間配置
     *
     * @param config 工作時間配置
     * @return 創建後的工作時間配置
     */
    WorkTimeConfig createConfig(WorkTimeConfig config);
    
    /**
     * 更新工作時間配置
     *
     * @param config 工作時間配置
     * @return 更新後的工作時間配置
     */
    WorkTimeConfig updateConfig(WorkTimeConfig config);
    
    /**
     * 刪除工作時間配置
     *
     * @param id 配置ID
     */
    void deleteConfig(Long id);
    
    /**
     * 根據ID查詢工作時間配置
     *
     * @param id 配置ID
     * @return 工作時間配置
     */
    Optional<WorkTimeConfig> getConfigById(Long id);
    
    /**
     * 根據配置名稱查詢工作時間配置
     *
     * @param configName 配置名稱
     * @return 工作時間配置
     */
    Optional<WorkTimeConfig> getConfigByName(String configName);
    
    /**
     * 獲取預設工作時間配置
     *
     * @return 預設工作時間配置
     */
    Optional<WorkTimeConfig> getDefaultConfig();
    
    /**
     * 設置工作時間配置為預設配置
     *
     * @param id 配置ID
     * @return 設置後的工作時間配置
     */
    WorkTimeConfig setAsDefault(Long id);
    
    /**
     * 啟用工作時間配置
     *
     * @param id 配置ID
     * @return 啟用後的工作時間配置
     */
    WorkTimeConfig enableConfig(Long id);
    
    /**
     * 停用工作時間配置
     *
     * @param id 配置ID
     * @return 停用後的工作時間配置
     */
    WorkTimeConfig disableConfig(Long id);
    
    /**
     * 查詢所有工作時間配置
     *
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> getAllConfigs();
    
    /**
     * 查詢所有啟用的工作時間配置
     *
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> getAllEnabledConfigs();
    
    /**
     * 分頁查詢工作時間配置
     *
     * @param configName 配置名稱，可為 null
     * @param status     狀態，可為 null
     * @param pageable   分頁參數
     * @return 分頁數據
     */
    Page<WorkTimeConfig> getConfigsPage(String configName, Integer status, Pageable pageable);
    
    /**
     * 檢查是否存在預設工作時間配置
     *
     * @return 是否存在預設配置
     */
    boolean hasDefaultConfig();
    
    /**
     * 創建預設工作時間配置
     *
     * @return 創建後的預設配置
     */
    WorkTimeConfig createDefaultConfig();
}
