package com.clockin.record.repository;

import com.clockin.record.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 請假申請數據訪問層
 */
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    /**
     * 根據用戶ID查詢請假申請，分頁
     *
     * @param userId 用戶ID
     * @param pageable 分頁參數
     * @return 請假申請分頁結果
     */
    Page<LeaveRequest> findByUserIdOrderByStartTimeDesc(Long userId, Pageable pageable);

    /**
     * 根據時間範圍查詢請假申請
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 請假申請列表
     */
    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "(l.startTime <= :endTime AND l.endTime >= :startTime)")
    List<LeaveRequest> findOverlappingLeaveRequests(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根據用戶ID和時間範圍查詢請假申請
     *
     * @param userId 用戶ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 請假申請列表
     */
    @Query("SELECT l FROM LeaveRequest l WHERE l.userId = :userId AND " +
           "(l.startTime <= :endTime AND l.endTime >= :startTime)")
    List<LeaveRequest> findOverlappingLeaveRequestsByUser(
            @Param("userId") Long userId, 
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根據用戶ID和狀態查詢請假申請，分頁
     *
     * @param userId 用戶ID
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 請假申請分頁結果
     */
    Page<LeaveRequest> findByUserIdAndStatusOrderByStartTimeDesc(
            Long userId, LeaveRequest.RequestStatus status, Pageable pageable);

    /**
     * 根據狀態查詢請假申請，分頁
     *
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 請假申請分頁結果
     */
    Page<LeaveRequest> findByStatusOrderByStartTimeDesc(LeaveRequest.RequestStatus status, Pageable pageable);

    /**
     * 查詢待審批的請假申請數量
     *
     * @return 待審批請假申請數量
     */
    long countByStatus(LeaveRequest.RequestStatus status);

    /**
     * 查詢指定部門用戶的待審批請假申請
     *
     * @param departmentId 部門ID
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 請假申請分頁結果
     */
    @Query("SELECT l FROM LeaveRequest l WHERE l.userId IN " +
            "(SELECT u.id FROM SysUser u WHERE u.departmentId = :departmentId) " +
            "AND l.status = :status ORDER BY l.startTime DESC")
    Page<LeaveRequest> findByDepartmentIdAndStatus(
            @Param("departmentId") Long departmentId, 
            @Param("status") LeaveRequest.RequestStatus status, 
            Pageable pageable);

    /**
     * 查詢指定審批人的請假申請記錄
     *
     * @param approverId 審批人ID
     * @param pageable 分頁參數
     * @return 請假申請分頁結果
     */
    Page<LeaveRequest> findByApproverIdOrderByApprovalTimeDesc(Long approverId, Pageable pageable);

    /**
     * 查詢指定日期範圍內已批准的請假記錄
     *
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 請假申請列表
     */
    @Query("SELECT l FROM LeaveRequest l WHERE l.status = 'APPROVED' AND " +
           "(l.startTime <= :endTime AND l.endTime >= :startTime) " +
           "ORDER BY l.startTime")
    List<LeaveRequest> findApprovedLeaveInDateRange(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查詢指定用戶在指定日期範圍內已批准的請假記錄
     *
     * @param userId 用戶ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 請假申請列表
     */
    @Query("SELECT l FROM LeaveRequest l WHERE l.userId = :userId AND l.status = 'APPROVED' AND " +
           "(l.startTime <= :endTime AND l.endTime >= :startTime) " +
           "ORDER BY l.startTime")
    List<LeaveRequest> findApprovedLeaveByUserInDateRange(
            @Param("userId") Long userId, 
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime);
}
