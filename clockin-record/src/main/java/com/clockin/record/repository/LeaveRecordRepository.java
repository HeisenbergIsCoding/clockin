package com.clockin.record.repository;

import com.clockin.record.entity.LeaveRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 請假記錄數據訪問層
 */
@Repository
public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Long>, JpaSpecificationExecutor<LeaveRecord> {

    /**
     * 查詢用戶在指定日期範圍內的請假記錄
     *
     * @param userId    用戶ID
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param isDeleted 是否已刪除
     * @return 請假記錄列表
     */
    List<LeaveRecord> findByUserIdAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndIsDeleted(
            Long userId, LocalDate startDate, LocalDate endDate, Boolean isDeleted);

    /**
     * 查詢指定狀態的請假記錄
     *
     * @param status    審批狀態
     * @param isDeleted 是否已刪除
     * @param pageable  分頁參數
     * @return 請假記錄分頁結果
     */
    Page<LeaveRecord> findByStatusAndIsDeleted(Integer status, Boolean isDeleted, Pageable pageable);

    /**
     * 查詢待審批的請假記錄
     *
     * @param isDeleted 是否已刪除
     * @param pageable  分頁參數
     * @return 請假記錄分頁結果
     */
    default Page<LeaveRecord> findPendingApproval(Boolean isDeleted, Pageable pageable) {
        return findByStatusAndIsDeleted(0, isDeleted, pageable);
    }

    /**
     * 查詢某用戶的請假記錄，按創建時間降序排序
     *
     * @param userId    用戶ID
     * @param isDeleted 是否已刪除
     * @param pageable  分頁參數
     * @return 請假記錄分頁結果
     */
    Page<LeaveRecord> findByUserIdAndIsDeletedOrderByCreateTimeDesc(Long userId, Boolean isDeleted, Pageable pageable);

    /**
     * 查詢在指定日期範圍內狀態為已通過的請假記錄
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param status    審批狀態
     * @param isDeleted 是否已刪除
     * @return 請假記錄列表
     */
    @Query("SELECT l FROM LeaveRecord l WHERE " +
           "((l.startDate BETWEEN :startDate AND :endDate) OR " +
           "(l.endDate BETWEEN :startDate AND :endDate) OR " +
           "(l.startDate <= :startDate AND l.endDate >= :endDate)) " +
           "AND l.status = :status AND l.isDeleted = :isDeleted")
    List<LeaveRecord> findApprovedLeaveInDateRange(
            LocalDate startDate, LocalDate endDate, Integer status, Boolean isDeleted);

    /**
     * 統計用戶在指定年份的各類型請假天數
     *
     * @param userId    用戶ID
     * @param year      年份
     * @param status    審批狀態（已通過）
     * @param isDeleted 是否已刪除
     * @return 各類型請假天數統計
     */
    @Query("SELECT l.leaveType, SUM(l.leaveDays) FROM LeaveRecord l " +
           "WHERE l.userId = :userId " +
           "AND YEAR(l.startDate) = :year " +
           "AND l.status = :status " +
           "AND l.isDeleted = :isDeleted " +
           "GROUP BY l.leaveType")
    List<Object[]> countLeaveTypeUsageByUserAndYear(Long userId, int year, Integer status, Boolean isDeleted);
}
