package com.clockin.record.controller;

import com.clockin.record.dto.ApiResponse;
import com.clockin.record.dto.LeaveRequestDTO;
import com.clockin.record.entity.LeaveRequest;
import com.clockin.record.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 請假申請控制器
 */
@RestController
@RequestMapping("/api/leave")
@Tag(name = "請假申請管理", description = "請假申請相關接口")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    @Operation(summary = "創建請假申請")
    @PreAuthorize("hasAuthority('LEAVE_CREATE')")
    public ApiResponse<LeaveRequestDTO> createRequest(@Valid @RequestBody LeaveRequestDTO requestDTO) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        String username = authentication.getPrincipal().toString();
        
        // 設置用戶信息
        requestDTO.setUserId(userId);
        requestDTO.setUserName(username);
        
        // 創建請假申請
        LeaveRequestDTO createdRequest = leaveRequestService.createRequest(requestDTO);
        return ApiResponse.success("請假申請創建成功", createdRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新請假申請")
    @PreAuthorize("hasAuthority('LEAVE_UPDATE')")
    public ApiResponse<LeaveRequestDTO> updateRequest(
            @Parameter(description = "請假申請ID") @PathVariable Long id,
            @Valid @RequestBody LeaveRequestDTO requestDTO) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 獲取當前申請
        LeaveRequestDTO currentRequest = leaveRequestService.getRequestById(id);
        
        // 檢查是否為本人申請或管理員
        if (!currentRequest.getUserId().equals(userId) && 
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ApiResponse.error("無權修改他人的請假申請");
        }
        
        // 更新請假申請
        LeaveRequestDTO updatedRequest = leaveRequestService.updateRequest(id, requestDTO);
        return ApiResponse.success("請假申請更新成功", updatedRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "獲取請假申請詳情")
    @PreAuthorize("hasAuthority('LEAVE_VIEW')")
    public ApiResponse<LeaveRequestDTO> getRequest(
            @Parameter(description = "請假申請ID") @PathVariable Long id) {
        // 獲取請假申請
        LeaveRequestDTO request = leaveRequestService.getRequestById(id);
        return ApiResponse.success(request);
    }

    @GetMapping("/user")
    @Operation(summary = "獲取當前用戶的請假申請列表")
    @PreAuthorize("hasAuthority('LEAVE_VIEW')")
    public ApiResponse<Page<LeaveRequestDTO>> getUserRequests(
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        
        // 獲取用戶的請假申請
        Page<LeaveRequestDTO> requests = leaveRequestService.getUserRequests(userId, pageRequest);
        return ApiResponse.success(requests);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "獲取指定用戶的請假申請列表")
    @PreAuthorize("hasAuthority('LEAVE_ADMIN')")
    public ApiResponse<Page<LeaveRequestDTO>> getUserRequestsById(
            @Parameter(description = "用戶ID") @PathVariable Long userId,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        
        // 獲取用戶的請假申請
        Page<LeaveRequestDTO> requests = leaveRequestService.getUserRequests(userId, pageRequest);
        return ApiResponse.success(requests);
    }

    @GetMapping("/pending")
    @Operation(summary = "獲取待審批的請假申請列表")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ApiResponse<Page<LeaveRequestDTO>> getPendingRequests(
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        
        // 獲取待審批的請假申請
        Page<LeaveRequestDTO> requests = leaveRequestService
                .getRequestsByStatus(LeaveRequest.RequestStatus.PENDING, pageRequest);
        return ApiResponse.success(requests);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "獲取部門待審批的請假申請列表")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ApiResponse<Page<LeaveRequestDTO>> getDepartmentRequests(
            @Parameter(description = "部門ID") @PathVariable Long departmentId,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        
        // 獲取部門待審批的請假申請
        Page<LeaveRequestDTO> requests = leaveRequestService
                .getDepartmentRequests(departmentId, LeaveRequest.RequestStatus.PENDING, pageRequest);
        return ApiResponse.success(requests);
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "審批請假申請")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ApiResponse<LeaveRequestDTO> approveRequest(
            @Parameter(description = "請假申請ID") @PathVariable Long id,
            @RequestBody Map<String, Object> approvalInfo) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long approverId = Long.parseLong(authentication.getName());
        String approverName = authentication.getPrincipal().toString();
        
        // 獲取審批信息
        LeaveRequest.RequestStatus status = LeaveRequest.RequestStatus
                .valueOf((String) approvalInfo.get("status"));
        String comment = (String) approvalInfo.get("comment");
        
        // 審批請假申請
        LeaveRequestDTO approvedRequest = leaveRequestService
                .approveRequest(id, approverId, approverName, status, comment);
        return ApiResponse.success("請假申請審批成功", approvedRequest);
    }

    @PostMapping("/batch-approve")
    @Operation(summary = "批量審批請假申請")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ApiResponse<String> batchApproveRequests(@RequestBody Map<String, Object> batchApprovalInfo) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long approverId = Long.parseLong(authentication.getName());
        String approverName = authentication.getPrincipal().toString();
        
        // 獲取批量審批信息
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) batchApprovalInfo.get("ids");
        LeaveRequest.RequestStatus status = LeaveRequest.RequestStatus
                .valueOf((String) batchApprovalInfo.get("status"));
        String comment = (String) batchApprovalInfo.get("comment");
        
        // 批量審批請假申請
        leaveRequestService.batchApproveRequests(ids, approverId, approverName, status, comment);
        return ApiResponse.success("批量審批請假申請成功");
    }

    @GetMapping("/date-range")
    @Operation(summary = "獲取日期範圍內的請假記錄")
    @PreAuthorize("hasAuthority('LEAVE_VIEW')")
    public ApiResponse<List<LeaveRequestDTO>> getApprovedLeaveInDateRange(
            @Parameter(description = "開始時間") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "結束時間") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 獲取日期範圍內的請假記錄
        List<LeaveRequestDTO> requests = leaveRequestService
                .getUserApprovedLeaveInDateRange(userId, startTime, endTime);
        return ApiResponse.success(requests);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除請假申請")
    @PreAuthorize("hasAuthority('LEAVE_DELETE')")
    public ApiResponse<String> deleteRequest(
            @Parameter(description = "請假申請ID") @PathVariable Long id) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 獲取當前申請
        LeaveRequestDTO currentRequest = leaveRequestService.getRequestById(id);
        
        // 檢查是否為本人申請或管理員
        if (!currentRequest.getUserId().equals(userId) && 
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ApiResponse.error("無權刪除他人的請假申請");
        }
        
        // 檢查申請狀態，已審批的不能刪除
        if (currentRequest.getStatus() != LeaveRequest.RequestStatus.PENDING) {
            return ApiResponse.error("已審批的請假申請不能刪除");
        }
        
        // 刪除請假申請
        leaveRequestService.deleteRequest(id);
        return ApiResponse.success("請假申請刪除成功");
    }

    @GetMapping("/check-overlap")
    @Operation(summary = "檢查請假時間是否重疊")
    @PreAuthorize("hasAuthority('LEAVE_CREATE')")
    public ApiResponse<Boolean> checkOverlap(
            @Parameter(description = "開始時間") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "結束時間") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "排除的申請ID") 
            @RequestParam(required = false) Long excludeId) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 檢查是否有重疊的請假申請
        boolean hasOverlap = leaveRequestService.hasOverlappingRequests(userId, startTime, endTime, excludeId);
        return ApiResponse.success(hasOverlap);
    }

    @GetMapping("/count-pending")
    @Operation(summary = "獲取待審批的請假申請數量")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ApiResponse<Long> countPendingRequests() {
        // 獲取待審批的請假申請數量
        long count = leaveRequestService.countPendingRequests();
        return ApiResponse.success(count);
    }
}
