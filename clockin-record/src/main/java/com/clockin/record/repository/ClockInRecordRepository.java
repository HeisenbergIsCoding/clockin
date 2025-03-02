package com.clockin.record.repository;

import com.clockin.record.entity.ClockInRecord;
import com.clockin.record.enums.ClockInType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 打卡記錄資源庫
 */
@Repository
public interface ClockInRecordRepository extends JpaRepository<ClockInRecord, Long>, JpaSpecificationExecutor<ClockInRecord> {

    /**
     * 根據用戶ID和打卡日期查詢打卡記錄
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @return 打卡記錄列表
     */
    List<ClockInRecord> findByUserIdAndClockDateAndIsDeletedOrderByClockTimeAsc(Long userId, LocalDate clockDate, Integer isDeleted);

    /**
     * 根據用戶ID、打卡日期和打卡類型查詢最新的一條打卡記錄
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @param clockType 打卡類型
     * @param isDeleted 是否刪除
     * @return 打卡記錄
     */
    Optional<ClockInRecord> findFirstByUserIdAndClockDateAndClockTypeAndIsDeletedOrderByClockTimeDesc(
            Long userId, LocalDate clockDate, ClockInType clockType, Integer isDeleted);

    /**
     * 檢查用戶在指定日期是否已經打卡
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @param clockType 打卡類型
     * @param isDeleted 是否刪除
     * @return 是否已打卡
     */
    boolean existsByUserIdAndClockDateAndClockTypeAndIsDeleted(
            Long userId, LocalDate clockDate, ClockInType clockType, Integer isDeleted);

    /**
     * 統計用戶在指定日期範圍內的打卡記錄數量
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param isDeleted 是否刪除
     * @return 打卡記錄數量
     */
    long countByUserIdAndClockDateBetweenAndIsDeleted(
            Long userId, LocalDate startDate, LocalDate endDate, Integer isDeleted);

    /**
     * 根據打卡日期範圍統計各用戶的打卡記錄數量
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param isDeleted 是否刪除
     * @return 用戶ID和打卡記錄數量
     */
    @Query("SELECT r.userId, COUNT(r) FROM ClockInRecord r WHERE r.clockDate BETWEEN :startDate AND :endDate AND r.isDeleted = :isDeleted GROUP BY r.userId")
    List<Object[]> countByUserGroupAndDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") Integer isDeleted);
}
