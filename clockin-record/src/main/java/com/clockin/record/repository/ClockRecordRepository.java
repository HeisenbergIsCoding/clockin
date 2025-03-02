package com.clockin.record.repository;

import com.clockin.record.entity.ClockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 打卡記錄數據庫操作
 */
@Repository
public interface ClockRecordRepository extends JpaRepository<ClockRecord, Long>, JpaSpecificationExecutor<ClockRecord> {
    
    /**
     * 根據用戶ID和日期查詢打卡記錄
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @return 打卡記錄
     */
    Optional<ClockRecord> findByUserIdAndClockDate(Long userId, LocalDate clockDate);
    
    /**
     * 查詢用戶指定日期範圍內的打卡記錄
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 打卡記錄列表
     */
    List<ClockRecord> findByUserIdAndClockDateBetweenOrderByClockDateAsc(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 查詢指定日期所有用戶的打卡記錄
     *
     * @param clockDate 打卡日期
     * @return 打卡記錄列表
     */
    List<ClockRecord> findByClockDateOrderByUserId(LocalDate clockDate);
    
    /**
     * 查詢用戶最近一次打卡記錄
     *
     * @param userId 用戶ID
     * @return 打卡記錄
     */
    @Query("SELECT cr FROM ClockRecord cr WHERE cr.userId = :userId ORDER BY cr.clockDate DESC LIMIT 1")
    Optional<ClockRecord> findLatestByUserId(@Param("userId") Long userId);
    
    /**
     * 統計用戶指定日期範圍內的工作總時長
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 工作總時長（分鐘）
     */
    @Query("SELECT SUM(cr.workDuration) FROM ClockRecord cr WHERE cr.userId = :userId AND cr.clockDate BETWEEN :startDate AND :endDate AND cr.isAbnormal = false")
    Integer sumWorkDurationByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 查詢指定日期範圍內所有異常打卡記錄
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 異常打卡記錄列表
     */
    List<ClockRecord> findByIsAbnormalTrueAndClockDateBetweenOrderByClockDateDesc(LocalDate startDate, LocalDate endDate);
}
