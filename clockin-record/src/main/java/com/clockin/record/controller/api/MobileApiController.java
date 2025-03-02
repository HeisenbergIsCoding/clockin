package com.clockin.record.controller.api;

import com.clockin.record.common.R;
import com.clockin.record.dto.ClockRecordDTO;
import com.clockin.record.dto.MobileClockInRequestDTO;
import com.clockin.record.service.MobileClockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 移動端 API 控制器
 */
@Tag(name = "移動端 API", description = "移動端相關接口")
@RestController
@RequestMapping("/api/mobile")
@RequiredArgsConstructor
public class MobileApiController {

    private final MobileClockService mobileClockService;

    @Operation(summary = "移動端打卡", description = "提交移動端位置打卡")
    @PostMapping("/clock")
    public R<ClockRecordDTO> clockIn(@Valid @RequestBody MobileClockInRequestDTO requestDTO) {
        ClockRecordDTO recordDTO = mobileClockService.clockIn(requestDTO);
        return R.ok(recordDTO);
    }

    @Operation(summary = "校驗位置", description = "校驗用戶位置是否在有效打卡範圍")
    @GetMapping("/validate-location")
    public R<Boolean> validateLocation(@RequestParam Double longitude, @RequestParam Double latitude) {
        boolean isValid = mobileClockService.isInValidLocation(longitude, latitude);
        return R.ok(isValid);
    }

    @Operation(summary = "校驗WiFi", description = "校驗WiFi是否為有效打卡WiFi")
    @GetMapping("/validate-wifi")
    public R<Boolean> validateWifi(@RequestParam String ssid, @RequestParam(required = false) String mac) {
        boolean isValid = mobileClockService.isValidWifi(ssid, mac);
        return R.ok(isValid);
    }

    @Operation(summary = "獲取最近工作地點", description = "獲取用戶當前位置最近的工作地點信息")
    @GetMapping("/nearest-location")
    public R<String> getNearestLocation(@RequestParam Double longitude, @RequestParam Double latitude) {
        String locationInfo = mobileClockService.getNearestWorkLocation(longitude, latitude);
        return R.ok(locationInfo);
    }

    @Operation(summary = "獲取所有有效工作地點", description = "獲取系統中所有有效的工作地點信息")
    @GetMapping("/work-locations")
    public R<String> getAllWorkLocations() {
        String locations = mobileClockService.getAllValidWorkLocations();
        return R.ok(locations);
    }

    @Operation(summary = "外勤打卡", description = "提交外勤打卡")
    @PostMapping("/out-working-clock")
    public R<ClockRecordDTO> outWorkingClockIn(@Valid @RequestBody MobileClockInRequestDTO requestDTO) {
        // 設置為外勤打卡
        requestDTO.setIsOutWorking(true);
        ClockRecordDTO recordDTO = mobileClockService.processOutWorkingClockIn(requestDTO);
        return R.ok(recordDTO);
    }
}
