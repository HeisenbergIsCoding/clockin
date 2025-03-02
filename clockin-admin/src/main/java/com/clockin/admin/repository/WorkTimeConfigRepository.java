package com.clockin.admin.repository;

import com.clockin.admin.entity.WorkTimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 工作時間配置數據庫操作
 */
@Repository
public interface WorkTimeConfigRepository extends JpaRepository<WorkTimeConfig, Long>, JpaSpecificationExecutor<WorkTimeConfig> {
    
    /**
     * 根據配置名稱查詢工作時間配置
     *
     * @param configName 配置名稱
     * @return 工作時間配置
     */
    Optional<WorkTimeConfig> findByConfigName(String configName);
    
    /**
     * 查詢預設工作時間配置
     *
     * @return 預設工作時間配置
     */
    Optional<WorkTimeConfig> findByIsDefaultTrue();
    
    /**
     * 根據狀態查詢工作時間配置列表
     *
     * @param status 狀態
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> findByStatus(Integer status);
    
    /**
     * 統計預設工作時間配置數量
     *
     * @return 預設配置數量
     */
    @Query("SELECT COUNT(w) FROM WorkTimeConfig w WHERE w.isDefault = true")
    long countByIsDefaultTrue();
}
