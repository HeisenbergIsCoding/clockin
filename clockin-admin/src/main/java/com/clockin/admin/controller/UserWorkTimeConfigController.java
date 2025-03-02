package com.clockin.admin.controller;

import com.clockin.admin.entity.UserWorkTimeConfig;
import com.clockin.admin.entity.WorkTimeConfig;
import com.clockin.admin.service.UserWorkTimeConfigService;
import com.clockin.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * 用戶工作時間配置關聯控制器
 */
@RestController
@RequestMapping("/api/admin/user-work-time-config")
@RequiredArgsConstructor
@Tag(name = "用戶工作時間配置管理", description = "用戶工作時間配置關聯相關的API")
public class UserWorkTimeConfigController {
    
    private final UserWorkTimeConfigService userWorkTimeConfigService;
    
    @PostMapping
    @Operation(summary = "創建用戶工作時間配置關聯", description = "創建新的用戶工作時間配置關聯")
    public Result<UserWorkTimeConfig> create(@Valid @RequestBody UserWorkTimeConfig userConfig) {
        return Result.success(userWorkTimeConfigService.createUserConfig(userConfig));
    }
    
    @PostMapping("/batch")
    @Operation(summary = "批量創建用戶工作時間配置關聯", description = "批量創建新的用戶工作時間配置關聯")
    public Result<List<UserWorkTimeConfig>> batchCreate(@Valid @RequestBody List<UserWorkTimeConfig> userConfigs) {
        return Result.success(userWorkTimeConfigService.batchCreateUserConfigs(userConfigs));
    }
    
    @PostMapping("/assign")
    @Operation(summary = "分配工作時間配置給用戶", description = "給用戶分配工作時間配置")
    public Result<UserWorkTimeConfig> assignToUser(
            @Parameter(description = "用戶ID") @RequestParam Long userId,
            @Parameter(description = "工作時間配置ID") @RequestParam Long configId,
            @Parameter(description = "生效開始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "生效結束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "是否為當前生效配置") @RequestParam(required = false) Boolean isCurrent,
            @Parameter(description = "狀態") @RequestParam(required = false, defaultValue = "1") Integer status,
            @Parameter(description = "備註") @RequestParam(required = false) String remark) {
        
        return Result.success(userWorkTimeConfigService.assignConfigToUser(userId, configId, startDate, endDate, isCurrent, status, remark));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新用戶工作時間配置關聯", description = "根據ID更新用戶工作時間配置關聯")
    public Result<UserWorkTimeConfig> update(
            @Parameter(description = "用戶工作時間配置關聯ID") @PathVariable Long id,
            @Valid @RequestBody UserWorkTimeConfig userConfig) {
        
        userConfig.setId(id);
        return Result.success(userWorkTimeConfigService.updateUserConfig(userConfig));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除用戶工作時間配置關聯", description = "根據ID刪除用戶工作時間配置關聯")
    public Result<Void> delete(@Parameter(description = "用戶工作時間配置關聯ID") @PathVariable Long id) {
        userWorkTimeConfigService.deleteUserConfig(id);
        return Result.success();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "獲取用戶工作時間配置關聯", description = "根據ID獲取用戶工作時間配置關聯")
    public Result<UserWorkTimeConfig> getById(@Parameter(description = "用戶工作時間配置關聯ID") @PathVariable Long id) {
        return userWorkTimeConfigService.getUserConfigById(id)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用戶工作時間配置關聯不存在"));
    }
    
    @GetMapping("/user/{userId}/current")
    @Operation(summary = "獲取用戶當前生效的工作時間配置關聯", description = "獲取用戶當前生效的工作時間配置關聯")
    public Result<UserWorkTimeConfig> getCurrentUserConfig(@Parameter(description = "用戶ID") @PathVariable Long userId) {
        return userWorkTimeConfigService.getCurrentUserConfig(userId)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用戶當前沒有生效的工作時間配置"));
    }
    
    @GetMapping("/user/{userId}/date/{date}")
    @Operation(summary = "獲取用戶在指定日期生效的工作時間配置關聯", description = "獲取用戶在指定日期生效的工作時間配置關聯")
    public Result<UserWorkTimeConfig> getUserConfigByDate(
            @Parameter(description = "用戶ID") @PathVariable Long userId,
            @Parameter(description = "日期，格式：yyyy-MM-dd") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return userWorkTimeConfigService.getUserConfigByDate(userId, date)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用戶在指定日期沒有生效的工作時間配置"));
    }
    
    @GetMapping("/user/{userId}/effective-config/{date}")
    @Operation(summary = "獲取用戶在指定日期生效的工作時間配置", description = "獲取用戶在指定日期生效的具體工作時間配置")
    public Result<WorkTimeConfig> getEffectiveWorkTimeConfig(
            @Parameter(description = "用戶ID") @PathVariable Long userId,
            @Parameter(description = "日期，格式：yyyy-MM-dd") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return userWorkTimeConfigService.getEffectiveWorkTimeConfig(userId, date)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到用戶在指定日期生效的工作時間配置"));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "獲取用戶的所有工作時間配置關聯", description = "根據用戶ID獲取所有工作時間配置關聯")
    public Result<List<UserWorkTimeConfig>> getByUserId(@Parameter(description = "用戶ID") @PathVariable Long userId) {
        return Result.success(userWorkTimeConfigService.getUserConfigsByUserId(userId));
    }
    
    @GetMapping("/config/{configId}")
    @Operation(summary = "獲取工作時間配置的所有用戶關聯", description = "根據工作時間配置ID獲取所有用戶關聯")
    public Result<List<UserWorkTimeConfig>> getByConfigId(@Parameter(description = "工作時間配置ID") @PathVariable Long configId) {
        return Result.success(userWorkTimeConfigService.getUserConfigsByConfigId(configId));
    }
    
    @GetMapping("/effective/{date}")
    @Operation(summary = "獲取指定日期所有生效的用戶工作時間配置關聯", description = "獲取指定日期所有生效的用戶工作時間配置關聯")
    public Result<List<UserWorkTimeConfig>> getAllEffectiveByDate(
            @Parameter(description = "日期，格式：yyyy-MM-dd") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return Result.success(userWorkTimeConfigService.getAllEffectiveUserConfigsByDate(date));
    }
    
    @PutMapping("/{id}/set-current")
    @Operation(summary = "設置為當前生效配置", description = "將用戶工作時間配置關聯設置為當前生效配置")
    public Result<UserWorkTimeConfig> setAsCurrent(@Parameter(description = "用戶工作時間配置關聯ID") @PathVariable Long id) {
        return Result.success(userWorkTimeConfigService.setAsCurrent(id));
    }
    
    @PutMapping("/{id}/enable")
    @Operation(summary = "啟用用戶工作時間配置關聯", description = "啟用指定用戶工作時間配置關聯")
    public Result<UserWorkTimeConfig> enable(@Parameter(description = "用戶工作時間配置關聯ID") @PathVariable Long id) {
        return Result.success(userWorkTimeConfigService.enableUserConfig(id));
    }
    
    @PutMapping("/{id}/disable")
    @Operation(summary = "停用用戶工作時間配置關聯", description = "停用指定用戶工作時間配置關聯")
    public Result<UserWorkTimeConfig> disable(@Parameter(description = "用戶工作時間配置關聯ID") @PathVariable Long id) {
        return Result.success(userWorkTimeConfigService.disableUserConfig(id));
    }
    
    @GetMapping("/page")
    @Operation(summary = "分頁獲取用戶工作時間配置關聯", description = "根據條件分頁獲取用戶工作時間配置關聯列表")
    public Result<Page<UserWorkTimeConfig>> getPage(
            @Parameter(description = "用戶ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "工作時間配置ID") @RequestParam(required = false) Long configId,
            @Parameter(description = "是否為當前生效配置") @RequestParam(required = false) Boolean isCurrent,
            @Parameter(description = "狀態") @RequestParam(required = false) Integer status,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        return Result.success(userWorkTimeConfigService.getUserConfigsPage(userId, configId, isCurrent, status, pageable));
    }
}
