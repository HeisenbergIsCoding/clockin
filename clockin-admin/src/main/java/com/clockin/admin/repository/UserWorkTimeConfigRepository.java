package com.clockin.admin.repository;

import com.clockin.admin.entity.UserWorkTimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 用戶工作時間配置關聯數據庫操作
 */
@Repository
public interface UserWorkTimeConfigRepository extends JpaRepository<UserWorkTimeConfig, Long>, JpaSpecificationExecutor<UserWorkTimeConfig> {
    
    /**
     * 查詢用戶當前生效的工作時間配置
     *
     * @param userId 用戶ID
     * @return 用戶工作時間配置關聯
     */
    Optional<UserWorkTimeConfig> findByUserIdAndIsCurrentTrue(Long userId);
    
    /**
     * 查詢用戶在指定日期生效的工作時間配置
     *
     * @param userId 用戶ID
     * @param date   日期
     * @return 用戶工作時間配置關聯
     */
    @Query("SELECT u FROM UserWorkTimeConfig u WHERE u.userId = :userId AND u.effectiveStartDate <= :date AND (u.effectiveEndDate IS NULL OR u.effectiveEndDate >= :date) AND u.status = 1")
    Optional<UserWorkTimeConfig> findEffectiveConfigByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    /**
     * 查詢用戶的所有工作時間配置關聯
     *
     * @param userId 用戶ID
     * @return 用戶工作時間配置關聯列表
     */
    List<UserWorkTimeConfig> findByUserIdOrderByEffectiveStartDateDesc(Long userId);
    
    /**
     * 查詢使用指定工作時間配置的所有用戶關聯
     *
     * @param configId 工作時間配置ID
     * @return 用戶工作時間配置關聯列表
     */
    List<UserWorkTimeConfig> findByConfigIdOrderByUserId(Long configId);
    
    /**
     * 查詢指定日期生效的所有用戶工作時間配置關聯
     *
     * @param date 日期
     * @return 用戶工作時間配置關聯列表
     */
    @Query("SELECT u FROM UserWorkTimeConfig u WHERE u.effectiveStartDate <= :date AND (u.effectiveEndDate IS NULL OR u.effectiveEndDate >= :date) AND u.status = 1")
    List<UserWorkTimeConfig> findAllEffectiveConfigsByDate(@Param("date") LocalDate date);
}
