package com.clockin.record.service.impl;

import com.clockin.record.dto.LeaveRequestDTO;
import com.clockin.record.entity.LeaveRequest;
import com.clockin.record.exception.BusinessException;
import com.clockin.record.repository.LeaveRequestRepository;
import com.clockin.record.service.LeaveRequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 請假申請服務實現類
 */
@Service
@CacheConfig(cacheNames = "leaveRequest")
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Override
    @Transactional
    public LeaveRequestDTO createRequest(LeaveRequestDTO requestDTO) {
        // 檢查請假時間
        if (requestDTO.getStartTime().isAfter(requestDTO.getEndTime())) {
            throw new BusinessException("請假開始時間不能晚於結束時間");
        }
        
        // 檢查是否存在重疊的請假申請
        if (hasOverlappingRequests(requestDTO.getUserId(), requestDTO.getStartTime(), 
                requestDTO.getEndTime(), null)) {
            throw new BusinessException("該時間段已存在請假申請，請調整請假時間");
        }
        
        LeaveRequest request = new LeaveRequest();
        BeanUtils.copyProperties(requestDTO, request);
        
        // a. 設置初始狀態為待審批
        request.setStatus(LeaveRequest.RequestStatus.PENDING);
        
        // 保存請假申請
        request = leaveRequestRepository.save(request);
        
        // 轉換並返回DTO
        BeanUtils.copyProperties(request, requestDTO);
        requestDTO.setId(request.getId());
        
        return requestDTO;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public LeaveRequestDTO updateRequest(Long id, LeaveRequestDTO requestDTO) {
        // 檢查請假申請是否存在
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("請假申請不存在"));
        
        // 檢查狀態是否為待審批，只有待審批的申請才能修改
        if (request.getStatus() != LeaveRequest.RequestStatus.PENDING) {
            throw new BusinessException("只能修改待審批狀態的請假申請");
        }
        
        // 檢查請假時間
        if (requestDTO.getStartTime().isAfter(requestDTO.getEndTime())) {
            throw new BusinessException("請假開始時間不能晚於結束時間");
        }
        
        // 檢查是否存在重疊的請假申請
        if (hasOverlappingRequests(requestDTO.getUserId(), requestDTO.getStartTime(), 
                requestDTO.getEndTime(), id)) {
            throw new BusinessException("該時間段已存在請假申請，請調整請假時間");
        }
        
        // 更新請假申請
        BeanUtils.copyProperties(requestDTO, request);
        request.setId(id);  // 確保ID不變
        request.setStatus(LeaveRequest.RequestStatus.PENDING);  // 確保狀態為待審批
        
        // 保存更新後的請假申請
        request = leaveRequestRepository.save(request);
        
        // 轉換並返回DTO
        BeanUtils.copyProperties(request, requestDTO);
        
        return requestDTO;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public LeaveRequestDTO approveRequest(Long id, Long approverId, String approverName, 
            LeaveRequest.RequestStatus status, String comment) {
        // 檢查請假申請是否存在
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("請假申請不存在"));
        
        // 檢查狀態是否為待審批
        if (request.getStatus() != LeaveRequest.RequestStatus.PENDING) {
            throw new BusinessException("該請假申請已審批");
        }
        
        // 設置審批信息
        request.setApproverId(approverId);
        request.setApproverName(approverName);
        request.setStatus(status);
        request.setApprovalComment(comment);
        request.setApprovalTime(LocalDateTime.now());
        
        // 保存更新後的請假申請
        request = leaveRequestRepository.save(request);
        
        // 轉換並返回DTO
        LeaveRequestDTO requestDTO = new LeaveRequestDTO();
        BeanUtils.copyProperties(request, requestDTO);
        
        return requestDTO;
    }

    @Override
    @Cacheable(key = "#id")
    public LeaveRequestDTO getRequestById(Long id) {
        // 查詢請假申請
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("請假申請不存在"));
        
        // 轉換並返回DTO
        LeaveRequestDTO requestDTO = new LeaveRequestDTO();
        BeanUtils.copyProperties(request, requestDTO);
        
        return requestDTO;
    }

    @Override
    public Page<LeaveRequestDTO> getUserRequests(Long userId, Pageable pageable) {
        // 查詢用戶的請假申請
        Page<LeaveRequest> requestPage = leaveRequestRepository.findByUserIdOrderByStartTimeDesc(userId, pageable);
        
        // 轉換並返回DTO分頁
        return requestPage.map(request -> {
            LeaveRequestDTO requestDTO = new LeaveRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        });
    }

    @Override
    public Page<LeaveRequestDTO> getRequestsByStatus(LeaveRequest.RequestStatus status, Pageable pageable) {
        // 查詢指定狀態的請假申請
        Page<LeaveRequest> requestPage = leaveRequestRepository.findByStatusOrderByStartTimeDesc(status, pageable);
        
        // 轉換並返回DTO分頁
        return requestPage.map(request -> {
            LeaveRequestDTO requestDTO = new LeaveRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        });
    }

    @Override
    public boolean hasOverlappingRequests(Long userId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        // 查詢重疊的請假申請
        List<LeaveRequest> overlappingRequests = leaveRequestRepository
                .findOverlappingLeaveRequestsByUser(userId, startTime, endTime);
        
        // 排除當前正在編輯的請假申請
        if (excludeId != null) {
            overlappingRequests = overlappingRequests.stream()
                    .filter(request -> !request.getId().equals(excludeId))
                    .collect(Collectors.toList());
        }
        
        // 如果有重疊的請假申請，則返回true
        return !overlappingRequests.isEmpty();
    }

    @Override
    public Page<LeaveRequestDTO> getDepartmentRequests(Long departmentId, LeaveRequest.RequestStatus status, Pageable pageable) {
        // 查詢部門的請假申請
        Page<LeaveRequest> requestPage = leaveRequestRepository
                .findByDepartmentIdAndStatus(departmentId, status, pageable);
        
        // 轉換並返回DTO分頁
        return requestPage.map(request -> {
            LeaveRequestDTO requestDTO = new LeaveRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        });
    }

    @Override
    @Transactional
    public void batchApproveRequests(List<Long> ids, Long approverId, String approverName, 
            LeaveRequest.RequestStatus status, String comment) {
        for (Long id : ids) {
            try {
                approveRequest(id, approverId, approverName, status, comment);
            } catch (Exception e) {
                // 記錄錯誤但繼續處理其他申請
                // 根據業務需求可以選擇拋出異常或者記錄日誌
                throw new BusinessException("批量審批請假申請失敗，ID: " + id + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteRequest(Long id) {
        // 檢查請假申請是否存在
        if (!leaveRequestRepository.existsById(id)) {
            throw new BusinessException("請假申請不存在");
        }
        
        // 刪除請假申請
        leaveRequestRepository.deleteById(id);
    }

    @Override
    public long countPendingRequests() {
        // 查詢待審批的請假申請數量
        return leaveRequestRepository.countByStatus(LeaveRequest.RequestStatus.PENDING);
    }

    @Override
    public List<LeaveRequestDTO> getUserApprovedLeaveInDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        // 查詢用戶在指定日期範圍內已批准的請假記錄
        List<LeaveRequest> leaveRequests = leaveRequestRepository
                .findApprovedLeaveByUserInDateRange(userId, startTime, endTime);
        
        // 轉換並返回DTO列表
        return leaveRequests.stream().map(request -> {
            LeaveRequestDTO requestDTO = new LeaveRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        }).collect(Collectors.toList());
    }
}
