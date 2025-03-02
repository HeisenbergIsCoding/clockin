package com.clockin.record.service.impl;

import com.clockin.record.dto.MakeupClockInRequestDTO;
import com.clockin.record.entity.MakeupClockInRequest;
import com.clockin.record.exception.BusinessException;
import com.clockin.record.repository.MakeupClockInRequestRepository;
import com.clockin.record.service.MakeupClockInRequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 補卡申請服務實現類
 */
@Service
@CacheConfig(cacheNames = "makeupClockInRequest")
public class MakeupClockInRequestServiceImpl implements MakeupClockInRequestService {

    @Autowired
    private MakeupClockInRequestRepository makeupClockInRequestRepository;

    @Override
    @Transactional
    public MakeupClockInRequestDTO createRequest(MakeupClockInRequestDTO requestDTO) {
        MakeupClockInRequest request = new MakeupClockInRequest();
        BeanUtils.copyProperties(requestDTO, request);
        
        // 設置初始狀態為待審批
        request.setStatus(MakeupClockInRequest.RequestStatus.PENDING);
        
        // 保存補卡申請
        request = makeupClockInRequestRepository.save(request);
        
        // 轉換並返回DTO
        BeanUtils.copyProperties(request, requestDTO);
        requestDTO.setId(request.getId());
        
        return requestDTO;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public MakeupClockInRequestDTO updateRequest(Long id, MakeupClockInRequestDTO requestDTO) {
        // 檢查補卡申請是否存在
        MakeupClockInRequest request = makeupClockInRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("補卡申請不存在"));
        
        // 檢查狀態是否為待審批，只有待審批的申請才能修改
        if (request.getStatus() != MakeupClockInRequest.RequestStatus.PENDING) {
            throw new BusinessException("只能修改待審批狀態的補卡申請");
        }
        
        // 更新補卡申請
        BeanUtils.copyProperties(requestDTO, request);
        request.setId(id);  // 確保ID不變
        request.setStatus(MakeupClockInRequest.RequestStatus.PENDING);  // 確保狀態為待審批
        
        // 保存更新後的補卡申請
        request = makeupClockInRequestRepository.save(request);
        
        // 轉換並返回DTO
        BeanUtils.copyProperties(request, requestDTO);
        
        return requestDTO;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public MakeupClockInRequestDTO approveRequest(Long id, Long approverId, String approverName, 
            MakeupClockInRequest.RequestStatus status, String comment) {
        // 檢查補卡申請是否存在
        MakeupClockInRequest request = makeupClockInRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("補卡申請不存在"));
        
        // 檢查狀態是否為待審批
        if (request.getStatus() != MakeupClockInRequest.RequestStatus.PENDING) {
            throw new BusinessException("該補卡申請已審批");
        }
        
        // 設置審批信息
        request.setApproverId(approverId);
        request.setApproverName(approverName);
        request.setStatus(status);
        request.setApprovalComment(comment);
        request.setApprovalTime(LocalDateTime.now());
        
        // 保存更新後的補卡申請
        request = makeupClockInRequestRepository.save(request);
        
        // 轉換並返回DTO
        MakeupClockInRequestDTO requestDTO = new MakeupClockInRequestDTO();
        BeanUtils.copyProperties(request, requestDTO);
        
        return requestDTO;
    }

    @Override
    @Cacheable(key = "#id")
    public MakeupClockInRequestDTO getRequestById(Long id) {
        // 查詢補卡申請
        MakeupClockInRequest request = makeupClockInRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("補卡申請不存在"));
        
        // 轉換並返回DTO
        MakeupClockInRequestDTO requestDTO = new MakeupClockInRequestDTO();
        BeanUtils.copyProperties(request, requestDTO);
        
        return requestDTO;
    }

    @Override
    public Page<MakeupClockInRequestDTO> getUserRequests(Long userId, Pageable pageable) {
        // 查詢用戶的補卡申請
        Page<MakeupClockInRequest> requestPage = makeupClockInRequestRepository.findByUserIdOrderByRequestDateDesc(userId, pageable);
        
        // 轉換並返回DTO分頁
        return requestPage.map(request -> {
            MakeupClockInRequestDTO requestDTO = new MakeupClockInRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        });
    }

    @Override
    public Page<MakeupClockInRequestDTO> getRequestsByStatus(MakeupClockInRequest.RequestStatus status, Pageable pageable) {
        // 查詢指定狀態的補卡申請
        Page<MakeupClockInRequest> requestPage = makeupClockInRequestRepository.findByStatusOrderByRequestDateDesc(status, pageable);
        
        // 轉換並返回DTO分頁
        return requestPage.map(request -> {
            MakeupClockInRequestDTO requestDTO = new MakeupClockInRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        });
    }

    @Override
    public List<MakeupClockInRequestDTO> getUserRequestsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        // 查詢用戶在日期範圍內的補卡申請
        List<MakeupClockInRequest> requests = makeupClockInRequestRepository
                .findByUserIdAndRequestDateBetweenOrderByRequestDateDesc(userId, startDate, endDate);
        
        // 轉換並返回DTO列表
        return requests.stream().map(request -> {
            MakeupClockInRequestDTO requestDTO = new MakeupClockInRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<MakeupClockInRequestDTO> getDepartmentRequests(Long departmentId, MakeupClockInRequest.RequestStatus status, Pageable pageable) {
        // 查詢部門的補卡申請
        Page<MakeupClockInRequest> requestPage = makeupClockInRequestRepository
                .findByDepartmentIdAndStatus(departmentId, status, pageable);
        
        // 轉換並返回DTO分頁
        return requestPage.map(request -> {
            MakeupClockInRequestDTO requestDTO = new MakeupClockInRequestDTO();
            BeanUtils.copyProperties(request, requestDTO);
            return requestDTO;
        });
    }

    @Override
    @Transactional
    public void batchApproveRequests(List<Long> ids, Long approverId, String approverName, 
            MakeupClockInRequest.RequestStatus status, String comment) {
        for (Long id : ids) {
            try {
                approveRequest(id, approverId, approverName, status, comment);
            } catch (Exception e) {
                // 記錄錯誤但繼續處理其他申請
                // 根據業務需求可以選擇拋出異常或者記錄日誌
                throw new BusinessException("批量審批補卡申請失敗，ID: " + id + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteRequest(Long id) {
        // 檢查補卡申請是否存在
        if (!makeupClockInRequestRepository.existsById(id)) {
            throw new BusinessException("補卡申請不存在");
        }
        
        // 刪除補卡申請
        makeupClockInRequestRepository.deleteById(id);
    }

    @Override
    public long countPendingRequests() {
        // 查詢待審批的補卡申請數量
        return makeupClockInRequestRepository.countByStatus(MakeupClockInRequest.RequestStatus.PENDING);
    }
}
