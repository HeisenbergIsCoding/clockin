package com.clockin.admin.controller;

import com.clockin.admin.entity.WorkTimeConfig;
import com.clockin.admin.service.WorkTimeConfigService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 工作時間配置控制器
 */
@RestController
@RequestMapping("/api/admin/work-time-config")
@RequiredArgsConstructor
@Tag(name = "工作時間配置管理", description = "工作時間配置相關的API")
public class WorkTimeConfigController {
    
    private final WorkTimeConfigService workTimeConfigService;
    
    @PostMapping
    @Operation(summary = "創建工作時間配置", description = "創建新的工作時間配置")
    public Result<WorkTimeConfig> create(@Valid @RequestBody WorkTimeConfig config) {
        return Result.success(workTimeConfigService.createConfig(config));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新工作時間配置", description = "根據ID更新工作時間配置")
    public Result<WorkTimeConfig> update(
            @Parameter(description = "工作時間配置ID") @PathVariable Long id,
            @Valid @RequestBody WorkTimeConfig config) {
        
        config.setId(id);
        return Result.success(workTimeConfigService.updateConfig(config));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除工作時間配置", description = "根據ID刪除工作時間配置")
    public Result<Void> delete(@Parameter(description = "工作時間配置ID") @PathVariable Long id) {
        workTimeConfigService.deleteConfig(id);
        return Result.success();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "獲取工作時間配置", description = "根據ID獲取工作時間配置")
    public Result<WorkTimeConfig> getById(@Parameter(description = "工作時間配置ID") @PathVariable Long id) {
        return workTimeConfigService.getConfigById(id)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "工作時間配置不存在"));
    }
    
    @GetMapping("/name/{configName}")
    @Operation(summary = "根據名稱獲取工作時間配置", description = "根據配置名稱獲取工作時間配置")
    public Result<WorkTimeConfig> getByName(@Parameter(description = "配置名稱") @PathVariable String configName) {
        return workTimeConfigService.getConfigByName(configName)
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "工作時間配置不存在"));
    }
    
    @GetMapping("/default")
    @Operation(summary = "獲取預設工作時間配置", description = "獲取系統預設的工作時間配置")
    public Result<WorkTimeConfig> getDefault() {
        return workTimeConfigService.getDefaultConfig()
                .map(Result::success)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "預設工作時間配置不存在"));
    }
    
    @PutMapping("/{id}/set-default")
    @Operation(summary = "設置為預設配置", description = "將指定工作時間配置設置為系統預設配置")
    public Result<WorkTimeConfig> setDefault(@Parameter(description = "工作時間配置ID") @PathVariable Long id) {
        return Result.success(workTimeConfigService.setAsDefault(id));
    }
    
    @PutMapping("/{id}/enable")
    @Operation(summary = "啟用工作時間配置", description = "啟用指定工作時間配置")
    public Result<WorkTimeConfig> enable(@Parameter(description = "工作時間配置ID") @PathVariable Long id) {
        return Result.success(workTimeConfigService.enableConfig(id));
    }
    
    @PutMapping("/{id}/disable")
    @Operation(summary = "停用工作時間配置", description = "停用指定工作時間配置")
    public Result<WorkTimeConfig> disable(@Parameter(description = "工作時間配置ID") @PathVariable Long id) {
        return Result.success(workTimeConfigService.disableConfig(id));
    }
    
    @GetMapping
    @Operation(summary = "獲取所有工作時間配置", description = "獲取所有工作時間配置列表")
    public Result<List<WorkTimeConfig>> getAll() {
        return Result.success(workTimeConfigService.getAllConfigs());
    }
    
    @GetMapping("/enabled")
    @Operation(summary = "獲取所有啟用的工作時間配置", description = "獲取所有狀態為啟用的工作時間配置列表")
    public Result<List<WorkTimeConfig>> getAllEnabled() {
        return Result.success(workTimeConfigService.getAllEnabledConfigs());
    }
    
    @GetMapping("/page")
    @Operation(summary = "分頁獲取工作時間配置", description = "根據條件分頁獲取工作時間配置列表")
    public Result<Page<WorkTimeConfig>> getPage(
            @Parameter(description = "配置名稱") @RequestParam(required = false) String configName,
            @Parameter(description = "配置狀態") @RequestParam(required = false) Integer status,
            @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        return Result.success(workTimeConfigService.getConfigsPage(configName, status, pageable));
    }
}
