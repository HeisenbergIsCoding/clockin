package com.clockin.record.controller;

import com.clockin.record.dto.ApiResponse;
import com.clockin.record.dto.WorkTimeConfigDTO;
import com.clockin.record.entity.WorkTimeConfig;
import com.clockin.record.exception.BusinessException;
import com.clockin.record.service.WorkTimeConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作時間配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/worktimes")
@Tag(name = "工作時間配置", description = "工作時間配置相關API")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class WorkTimeConfigController {

    private final WorkTimeConfigService workTimeConfigService;

    @GetMapping
    @Operation(summary = "查詢工作時間配置列表", description = "根據條件查詢工作時間配置列表")
    public ApiResponse<List<WorkTimeConfig>> listWorkTimeConfigs(
            @Parameter(description = "用戶ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "部門ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "是否啟用") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "生效日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {

        List<WorkTimeConfig> results = workTimeConfigService.findWorkTimeConfigs(userId, departmentId, isActive, effectiveDate);
        return ApiResponse.success(results);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查詢工作時間配置詳情", description = "根據ID查詢工作時間配置詳情")
    public ApiResponse<WorkTimeConfig> getWorkTimeConfig(@PathVariable Long id) {
        return workTimeConfigService.findById(id)
                .map(ApiResponse::success)
                .orElseThrow(() -> new BusinessException("未找到ID為 " + id + " 的工作時間配置"));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查詢用戶當前工作時間配置", description = "查詢指定用戶在當前日期的工作時間配置")
    public ApiResponse<WorkTimeConfig> getUserCurrentWorkTimeConfig(
            @PathVariable Long userId,
            @Parameter(description = "指定日期，默認為當前日期") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate targetDate = date != null ? date : LocalDate.now();
        WorkTimeConfig config = workTimeConfigService.getWorkTimeConfig(userId, targetDate);
        return ApiResponse.success(config);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "創建工作時間配置", description = "創建新的工作時間配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<WorkTimeConfig> createWorkTimeConfig(@Valid @RequestBody WorkTimeConfigDTO configDTO) {
        WorkTimeConfig createdConfig = workTimeConfigService.createWorkTimeConfig(configDTO);
        return ApiResponse.success("創建工作時間配置成功", createdConfig);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新工作時間配置", description = "根據ID更新工作時間配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<WorkTimeConfig> updateWorkTimeConfig(
            @PathVariable Long id, @Valid @RequestBody WorkTimeConfigDTO configDTO) {
        WorkTimeConfig updatedConfig = workTimeConfigService.updateWorkTimeConfig(id, configDTO);
        return ApiResponse.success("更新工作時間配置成功", updatedConfig);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "刪除工作時間配置", description = "根據ID刪除工作時間配置")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteWorkTimeConfig(@PathVariable Long id) {
        workTimeConfigService.deleteById(id);
        return ApiResponse.success("刪除工作時間配置成功");
    }
}
