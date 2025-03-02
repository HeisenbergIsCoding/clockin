package com.clockin.record.repository;

import com.clockin.record.entity.WorkTimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作時間配置數據訪問層
 */
@Repository
public interface WorkTimeConfigRepository extends JpaRepository<WorkTimeConfig, Long> {

    /**
     * 查詢特定日期生效的所有配置，按優先級降序排序
     *
     * @param date 指定日期
     * @return 工作時間配置列表
     */
    @Query("SELECT w FROM WorkTimeConfig w WHERE w.isActive = true AND w.effectiveDate <= :date " +
            "AND (w.expiredDate IS NULL OR w.expiredDate >= :date) ORDER BY w.priority DESC")
    List<WorkTimeConfig> findEffectiveConfigsOnDate(@Param("date") LocalDate date);

    /**
     * 查詢特定用戶在特定日期生效的配置，按優先級降序排序
     *
     * @param userId 用戶ID
     * @param date 指定日期
     * @return 工作時間配置列表
     */
    @Query("SELECT w FROM WorkTimeConfig w WHERE w.isActive = true AND " +
            "(w.userId = :userId OR (w.userId IS NULL AND w.departmentId = :departmentId) OR (w.userId IS NULL AND w.departmentId IS NULL)) " +
            "AND w.effectiveDate <= :date AND (w.expiredDate IS NULL OR w.expiredDate >= :date) ORDER BY w.priority DESC")
    List<WorkTimeConfig> findEffectiveConfigsForUserOnDate(@Param("userId") Long userId, 
                                                         @Param("departmentId") Long departmentId, 
                                                         @Param("date") LocalDate date);

    /**
     * 根據用戶ID查詢配置列表
     *
     * @param userId 用戶ID
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> findByUserIdOrderByPriorityDesc(Long userId);

    /**
     * 根據部門ID查詢配置列表
     *
     * @param departmentId 部門ID
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> findByDepartmentIdOrderByPriorityDesc(Long departmentId);

    /**
     * 查詢全局配置列表
     *
     * @return 工作時間配置列表
     */
    @Query("SELECT w FROM WorkTimeConfig w WHERE w.userId IS NULL AND w.departmentId IS NULL ORDER BY w.priority DESC")
    List<WorkTimeConfig> findGlobalConfigs();

    /**
     * 根據用戶ID和是否啟用查詢配置列表
     *
     * @param userId 用戶ID
     * @param isActive 是否啟用
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> findByUserIdAndIsActiveOrderByPriorityDesc(Long userId, Boolean isActive);

    /**
     * 根據部門ID和是否啟用查詢配置列表
     *
     * @param departmentId 部門ID
     * @param isActive 是否啟用
     * @return 工作時間配置列表
     */
    List<WorkTimeConfig> findByDepartmentIdAndIsActiveOrderByPriorityDesc(Long departmentId, Boolean isActive);
}
