package com.clockin.record.repository;

import com.clockin.record.entity.MakeupClockInRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 補卡申請數據訪問層
 */
@Repository
public interface MakeupClockInRequestRepository extends JpaRepository<MakeupClockInRequest, Long> {

    /**
     * 根據用戶ID和日期範圍查詢補卡申請
     *
     * @param userId 用戶ID
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 補卡申請列表
     */
    List<MakeupClockInRequest> findByUserIdAndRequestDateBetweenOrderByRequestDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 根據用戶ID查詢補卡申請，分頁
     *
     * @param userId 用戶ID
     * @param pageable 分頁參數
     * @return 補卡申請分頁結果
     */
    Page<MakeupClockInRequest> findByUserIdOrderByRequestDateDesc(Long userId, Pageable pageable);

    /**
     * 根據狀態查詢補卡申請，分頁
     *
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 補卡申請分頁結果
     */
    Page<MakeupClockInRequest> findByStatusOrderByRequestDateDesc(
            MakeupClockInRequest.RequestStatus status, Pageable pageable);

    /**
     * 根據用戶ID和狀態查詢補卡申請，分頁
     *
     * @param userId 用戶ID
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 補卡申請分頁結果
     */
    Page<MakeupClockInRequest> findByUserIdAndStatusOrderByRequestDateDesc(
            Long userId, MakeupClockInRequest.RequestStatus status, Pageable pageable);

    /**
     * 查詢待審批的補卡申請數量
     *
     * @return 待審批補卡申請數量
     */
    long countByStatus(MakeupClockInRequest.RequestStatus status);

    /**
     * 查詢指定部門用戶的待審批補卡申請
     *
     * @param departmentId 部門ID
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 補卡申請分頁結果
     */
    @Query("SELECT m FROM MakeupClockInRequest m WHERE m.userId IN " +
            "(SELECT u.id FROM SysUser u WHERE u.departmentId = :departmentId) " +
            "AND m.status = :status ORDER BY m.requestDate DESC")
    Page<MakeupClockInRequest> findByDepartmentIdAndStatus(
            @Param("departmentId") Long departmentId, 
            @Param("status") MakeupClockInRequest.RequestStatus status, 
            Pageable pageable);

    /**
     * 查詢指定審批人的補卡申請記錄
     *
     * @param approverId 審批人ID
     * @param pageable 分頁參數
     * @return 補卡申請分頁結果
     */
    Page<MakeupClockInRequest> findByApproverIdOrderByApprovalTimeDesc(Long approverId, Pageable pageable);
}
