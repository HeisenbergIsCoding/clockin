package com.clockin.record.service;

import com.clockin.record.dto.ClockRecordDTO;
import com.clockin.record.dto.MobileClockInRequestDTO;

/**
 * 移動端打卡服務接口
 */
public interface MobileClockService {

    /**
     * 移動端打卡
     *
     * @param requestDTO 打卡請求數據
     * @return 打卡記錄DTO
     */
    ClockRecordDTO clockIn(MobileClockInRequestDTO requestDTO);

    /**
     * 校驗用戶是否在指定打卡範圍內
     *
     * @param longitude 經度
     * @param latitude  緯度
     * @return 是否在範圍內
     */
    boolean isInValidLocation(Double longitude, Double latitude);

    /**
     * 校驗WiFi是否為有效打卡WiFi
     *
     * @param ssid WiFi SSID
     * @param mac  WiFi MAC地址
     * @return 是否為有效打卡WiFi
     */
    boolean isValidWifi(String ssid, String mac);

    /**
     * 獲取最近的工作地點信息
     *
     * @param longitude 經度
     * @param latitude  緯度
     * @return 最近的工作地點名稱及距離信息
     */
    String getNearestWorkLocation(Double longitude, Double latitude);

    /**
     * 計算兩個坐標之間的距離（米）
     *
     * @param longitude1 坐標1經度
     * @param latitude1  坐標1緯度
     * @param longitude2 坐標2經度
     * @param latitude2  坐標2緯度
     * @return 距離（米）
     */
    double calculateDistance(Double longitude1, Double latitude1, Double longitude2, Double latitude2);

    /**
     * 處理外勤打卡
     *
     * @param requestDTO 打卡請求數據
     * @return 打卡記錄DTO
     */
    ClockRecordDTO processOutWorkingClockIn(MobileClockInRequestDTO requestDTO);

    /**
     * 獲取當前所有有效的工作地點
     *
     * @return 有效工作地點的JSON字符串
     */
    String getAllValidWorkLocations();
}
