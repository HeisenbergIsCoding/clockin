package com.clockin.record.controller;

import com.clockin.record.dto.ApiResponse;
import com.clockin.record.dto.MakeupClockInRequestDTO;
import com.clockin.record.entity.MakeupClockInRequest;
import com.clockin.record.service.MakeupClockInRequestService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 補卡申請控制器
 */
@RestController
@RequestMapping("/api/makeup-clock-in")
@Tag(name = "補卡申請管理", description = "補卡申請相關接口")
public class MakeupClockInRequestController {

    @Autowired
    private MakeupClockInRequestService makeupClockInRequestService;

    @PostMapping
    @Operation(summary = "創建補卡申請")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_CREATE')")
    public ApiResponse<MakeupClockInRequestDTO> createRequest(@Valid @RequestBody MakeupClockInRequestDTO requestDTO) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        String username = authentication.getPrincipal().toString();
        
        // 設置用戶信息
        requestDTO.setUserId(userId);
        requestDTO.setUserName(username);
        
        // 創建補卡申請
        MakeupClockInRequestDTO createdRequest = makeupClockInRequestService.createRequest(requestDTO);
        return ApiResponse.success("補卡申請創建成功", createdRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新補卡申請")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_UPDATE')")
    public ApiResponse<MakeupClockInRequestDTO> updateRequest(
            @Parameter(description = "補卡申請ID") @PathVariable Long id,
            @Valid @RequestBody MakeupClockInRequestDTO requestDTO) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 獲取當前申請
        MakeupClockInRequestDTO currentRequest = makeupClockInRequestService.getRequestById(id);
        
        // 檢查是否為本人申請或管理員
        if (!currentRequest.getUserId().equals(userId) && 
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ApiResponse.error("無權修改他人的補卡申請");
        }
        
        // 更新補卡申請
        MakeupClockInRequestDTO updatedRequest = makeupClockInRequestService.updateRequest(id, requestDTO);
        return ApiResponse.success("補卡申請更新成功", updatedRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "獲取補卡申請詳情")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_VIEW')")
    public ApiResponse<MakeupClockInRequestDTO> getRequest(
            @Parameter(description = "補卡申請ID") @PathVariable Long id) {
        // 獲取補卡申請
        MakeupClockInRequestDTO request = makeupClockInRequestService.getRequestById(id);
        return ApiResponse.success(request);
    }

    @GetMapping("/user")
    @Operation(summary = "獲取當前用戶的補卡申請列表")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_VIEW')")
    public ApiResponse<Page<MakeupClockInRequestDTO>> getUserRequests(
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestDate"));
        
        // 獲取用戶的補卡申請
        Page<MakeupClockInRequestDTO> requests = makeupClockInRequestService.getUserRequests(userId, pageRequest);
        return ApiResponse.success(requests);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "獲取指定用戶的補卡申請列表")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_ADMIN')")
    public ApiResponse<Page<MakeupClockInRequestDTO>> getUserRequestsById(
            @Parameter(description = "用戶ID") @PathVariable Long userId,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestDate"));
        
        // 獲取用戶的補卡申請
        Page<MakeupClockInRequestDTO> requests = makeupClockInRequestService.getUserRequests(userId, pageRequest);
        return ApiResponse.success(requests);
    }

    @GetMapping("/pending")
    @Operation(summary = "獲取待審批的補卡申請列表")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_APPROVE')")
    public ApiResponse<Page<MakeupClockInRequestDTO>> getPendingRequests(
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestDate"));
        
        // 獲取待審批的補卡申請
        Page<MakeupClockInRequestDTO> requests = makeupClockInRequestService
                .getRequestsByStatus(MakeupClockInRequest.RequestStatus.PENDING, pageRequest);
        return ApiResponse.success(requests);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "獲取部門待審批的補卡申請列表")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_APPROVE')")
    public ApiResponse<Page<MakeupClockInRequestDTO>> getDepartmentRequests(
            @Parameter(description = "部門ID") @PathVariable Long departmentId,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size) {
        // 創建分頁請求
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "requestDate"));
        
        // 獲取部門待審批的補卡申請
        Page<MakeupClockInRequestDTO> requests = makeupClockInRequestService
                .getDepartmentRequests(departmentId, MakeupClockInRequest.RequestStatus.PENDING, pageRequest);
        return ApiResponse.success(requests);
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "審批補卡申請")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_APPROVE')")
    public ApiResponse<MakeupClockInRequestDTO> approveRequest(
            @Parameter(description = "補卡申請ID") @PathVariable Long id,
            @RequestBody Map<String, Object> approvalInfo) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long approverId = Long.parseLong(authentication.getName());
        String approverName = authentication.getPrincipal().toString();
        
        // 獲取審批信息
        MakeupClockInRequest.RequestStatus status = MakeupClockInRequest.RequestStatus
                .valueOf((String) approvalInfo.get("status"));
        String comment = (String) approvalInfo.get("comment");
        
        // 審批補卡申請
        MakeupClockInRequestDTO approvedRequest = makeupClockInRequestService
                .approveRequest(id, approverId, approverName, status, comment);
        return ApiResponse.success("補卡申請審批成功", approvedRequest);
    }

    @PostMapping("/batch-approve")
    @Operation(summary = "批量審批補卡申請")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_APPROVE')")
    public ApiResponse<String> batchApproveRequests(@RequestBody Map<String, Object> batchApprovalInfo) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long approverId = Long.parseLong(authentication.getName());
        String approverName = authentication.getPrincipal().toString();
        
        // 獲取批量審批信息
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) batchApprovalInfo.get("ids");
        MakeupClockInRequest.RequestStatus status = MakeupClockInRequest.RequestStatus
                .valueOf((String) batchApprovalInfo.get("status"));
        String comment = (String) batchApprovalInfo.get("comment");
        
        // 批量審批補卡申請
        makeupClockInRequestService.batchApproveRequests(ids, approverId, approverName, status, comment);
        return ApiResponse.success("批量審批補卡申請成功");
    }

    @GetMapping("/date-range")
    @Operation(summary = "獲取日期範圍內的補卡申請")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_VIEW')")
    public ApiResponse<List<MakeupClockInRequestDTO>> getRequestsByDateRange(
            @Parameter(description = "開始日期") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "結束日期") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 獲取日期範圍內的補卡申請
        List<MakeupClockInRequestDTO> requests = makeupClockInRequestService
                .getUserRequestsByDateRange(userId, startDate, endDate);
        return ApiResponse.success(requests);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除補卡申請")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_DELETE')")
    public ApiResponse<String> deleteRequest(
            @Parameter(description = "補卡申請ID") @PathVariable Long id) {
        // 獲取當前登錄用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        // 獲取當前申請
        MakeupClockInRequestDTO currentRequest = makeupClockInRequestService.getRequestById(id);
        
        // 檢查是否為本人申請或管理員
        if (!currentRequest.getUserId().equals(userId) && 
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ApiResponse.error("無權刪除他人的補卡申請");
        }
        
        // 刪除補卡申請
        makeupClockInRequestService.deleteRequest(id);
        return ApiResponse.success("補卡申請刪除成功");
    }

    @GetMapping("/count-pending")
    @Operation(summary = "獲取待審批的補卡申請數量")
    @PreAuthorize("hasAuthority('MAKEUP_CLOCK_IN_APPROVE')")
    public ApiResponse<Long> countPendingRequests() {
        // 獲取待審批的補卡申請數量
        long count = makeupClockInRequestService.countPendingRequests();
        return ApiResponse.success(count);
    }
}
