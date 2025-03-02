package com.clockin.record.service;

import com.clockin.record.dto.LeaveRequestDTO;
import com.clockin.record.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 請假申請服務接口
 */
public interface LeaveRequestService {

    /**
     * 創建請假申請
     *
     * @param requestDTO 請假申請DTO
     * @return 創建後的請假申請DTO
     */
    LeaveRequestDTO createRequest(LeaveRequestDTO requestDTO);

    /**
     * 更新請假申請
     *
     * @param id 請假申請ID
     * @param requestDTO 請假申請DTO
     * @return 更新後的請假申請DTO
     */
    LeaveRequestDTO updateRequest(Long id, LeaveRequestDTO requestDTO);

    /**
     * 審批請假申請
     *
     * @param id 請假申請ID
     * @param approverId 審批人ID
     * @param approverName 審批人名稱
     * @param status 審批狀態
     * @param comment 審批意見
     * @return 審批後的請假申請DTO
     */
    LeaveRequestDTO approveRequest(Long id, Long approverId, String approverName, 
            LeaveRequest.RequestStatus status, String comment);

    /**
     * 根據ID獲取請假申請
     *
     * @param id 請假申請ID
     * @return 請假申請DTO
     */
    LeaveRequestDTO getRequestById(Long id);

    /**
     * 獲取用戶的請假申請列表，分頁
     *
     * @param userId 用戶ID
     * @param pageable 分頁參數
     * @return 請假申請DTO分頁結果
     */
    Page<LeaveRequestDTO> getUserRequests(Long userId, Pageable pageable);

    /**
     * 根據狀態獲取請假申請列表，分頁
     *
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 請假申請DTO分頁結果
     */
    Page<LeaveRequestDTO> getRequestsByStatus(LeaveRequest.RequestStatus status, Pageable pageable);

    /**
     * 檢查時間範圍內是否存在重疊的請假申請
     *
     * @param userId 用戶ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param excludeId 排除的請假申請ID
     * @return 是否存在重疊
     */
    boolean hasOverlappingRequests(Long userId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

    /**
     * 獲取部門待審批的請假申請
     *
     * @param departmentId 部門ID
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 請假申請DTO分頁結果
     */
    Page<LeaveRequestDTO> getDepartmentRequests(Long departmentId, LeaveRequest.RequestStatus status, Pageable pageable);

    /**
     * 批量審批請假申請
     *
     * @param ids 請假申請ID列表
     * @param approverId 審批人ID
     * @param approverName 審批人名稱
     * @param status 審批狀態
     * @param comment 審批意見
     */
    void batchApproveRequests(List<Long> ids, Long approverId, String approverName, 
            LeaveRequest.RequestStatus status, String comment);

    /**
     * 刪除請假申請
     *
     * @param id 請假申請ID
     */
    void deleteRequest(Long id);

    /**
     * 獲取待審批的請假申請數量
     *
     * @return 待審批請假申請數量
     */
    long countPendingRequests();

    /**
     * 獲取用戶在指定日期範圍內的請假記錄
     *
     * @param userId 用戶ID
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 請假申請DTO列表
     */
    List<LeaveRequestDTO> getUserApprovedLeaveInDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
}
