package com.clockin.record.repository;

import com.clockin.record.entity.ClockInSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 打卡匯總資源庫
 */
@Repository
public interface ClockInSummaryRepository extends JpaRepository<ClockInSummary, Long>, JpaSpecificationExecutor<ClockInSummary> {

    /**
     * 根據用戶ID和打卡日期查詢打卡匯總
     *
     * @param userId    用戶ID
     * @param clockDate 打卡日期
     * @param isDeleted 是否刪除
     * @return 打卡匯總
     */
    Optional<ClockInSummary> findByUserIdAndClockDateAndIsDeleted(Long userId, LocalDate clockDate, Integer isDeleted);

    /**
     * 根據用戶ID和打卡日期範圍查詢打卡匯總
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param isDeleted 是否刪除
     * @return 打卡匯總列表
     */
    List<ClockInSummary> findByUserIdAndClockDateBetweenAndIsDeletedOrderByClockDateAsc(
            Long userId, LocalDate startDate, LocalDate endDate, Integer isDeleted);

    /**
     * 統計用戶在指定日期範圍內的缺勤情況
     *
     * @param userId       用戶ID
     * @param startDate    開始日期
     * @param endDate      結束日期
     * @param absenceTypes 缺勤類型列表
     * @param isDeleted    是否刪除
     * @return 缺勤次數
     */
    long countByUserIdAndClockDateBetweenAndAbsenceTypeInAndIsDeleted(
            Long userId, LocalDate startDate, LocalDate endDate, List<Integer> absenceTypes, Integer isDeleted);

    /**
     * 根據部門ID和日期範圍統計出勤率
     *
     * @param departmentId 部門ID
     * @param startDate    開始日期
     * @param endDate      結束日期
     * @param isDeleted    是否刪除
     * @return 出勤率統計結果，返回[日期, 出勤率]數組列表
     */
    @Query(value = "SELECT s.clock_date, " +
            "COUNT(CASE WHEN s.absence_type = 0 THEN 1 END) * 1.0 / COUNT(*) AS attendance_rate " +
            "FROM clock_in_summary s " +
            "JOIN sys_user u ON s.user_id = u.id " +
            "WHERE u.department_id = :departmentId " +
            "AND s.clock_date BETWEEN :startDate AND :endDate " +
            "AND s.is_deleted = :isDeleted " +
            "GROUP BY s.clock_date " +
            "ORDER BY s.clock_date", nativeQuery = true)
    List<Object[]> statisticAttendanceRateByDepartment(
            @Param("departmentId") Long departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") Integer isDeleted);

    /**
     * 根據用戶ID和日期範圍查詢異常打卡記錄數量
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param isDeleted 是否刪除
     * @return 異常打卡記錄數量
     */
    @Query("SELECT COUNT(s) FROM ClockInSummary s WHERE " +
            "s.userId = :userId AND " +
            "s.clockDate BETWEEN :startDate AND :endDate AND " +
            "s.isDeleted = :isDeleted AND " +
            "(s.clockInStatus > 0 OR s.clockOutStatus > 0 OR s.absenceType > 0)")
    long countAbnormalRecords(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isDeleted") Integer isDeleted);
}
