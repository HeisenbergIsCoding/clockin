package com.clockin.record.service;

import com.clockin.record.dto.MakeupClockInRequestDTO;
import com.clockin.record.entity.MakeupClockInRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * 補卡申請服務接口
 */
public interface MakeupClockInRequestService {

    /**
     * 創建補卡申請
     *
     * @param requestDTO 補卡申請DTO
     * @return 創建後的補卡申請DTO
     */
    MakeupClockInRequestDTO createRequest(MakeupClockInRequestDTO requestDTO);

    /**
     * 更新補卡申請
     *
     * @param id 補卡申請ID
     * @param requestDTO 補卡申請DTO
     * @return 更新後的補卡申請DTO
     */
    MakeupClockInRequestDTO updateRequest(Long id, MakeupClockInRequestDTO requestDTO);

    /**
     * 審批補卡申請
     *
     * @param id 補卡申請ID
     * @param approverId 審批人ID
     * @param approverName 審批人名稱
     * @param status 審批狀態
     * @param comment 審批意見
     * @return 審批後的補卡申請DTO
     */
    MakeupClockInRequestDTO approveRequest(Long id, Long approverId, String approverName, 
            MakeupClockInRequest.RequestStatus status, String comment);

    /**
     * 根據ID獲取補卡申請
     *
     * @param id 補卡申請ID
     * @return 補卡申請DTO
     */
    MakeupClockInRequestDTO getRequestById(Long id);

    /**
     * 獲取用戶的補卡申請列表，分頁
     *
     * @param userId 用戶ID
     * @param pageable 分頁參數
     * @return 補卡申請DTO分頁結果
     */
    Page<MakeupClockInRequestDTO> getUserRequests(Long userId, Pageable pageable);

    /**
     * 根據狀態獲取補卡申請列表，分頁
     *
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 補卡申請DTO分頁結果
     */
    Page<MakeupClockInRequestDTO> getRequestsByStatus(MakeupClockInRequest.RequestStatus status, Pageable pageable);

    /**
     * 獲取用戶在日期範圍內的補卡申請
     *
     * @param userId 用戶ID
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 補卡申請DTO列表
     */
    List<MakeupClockInRequestDTO> getUserRequestsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 獲取部門待審批的補卡申請
     *
     * @param departmentId 部門ID
     * @param status 狀態
     * @param pageable 分頁參數
     * @return 補卡申請DTO分頁結果
     */
    Page<MakeupClockInRequestDTO> getDepartmentRequests(Long departmentId, MakeupClockInRequest.RequestStatus status, Pageable pageable);

    /**
     * 批量審批補卡申請
     *
     * @param ids 補卡申請ID列表
     * @param approverId 審批人ID
     * @param approverName 審批人名稱
     * @param status 審批狀態
     * @param comment 審批意見
     */
    void batchApproveRequests(List<Long> ids, Long approverId, String approverName, 
            MakeupClockInRequest.RequestStatus status, String comment);

    /**
     * 刪除補卡申請
     *
     * @param id 補卡申請ID
     */
    void deleteRequest(Long id);

    /**
     * 獲取待審批的補卡申請數量
     *
     * @return 待審批補卡申請數量
     */
    long countPendingRequests();
}
