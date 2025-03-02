package com.clockin.record.util;

/**
 * 地理位置相關工具類
 */
public class GeoUtils {

    /**
     * 地球半徑（米）
     */
    private static final double EARTH_RADIUS = 6371000;

    /**
     * 計算兩個坐標點之間的距離（米）
     * 使用 Haversine 公式計算球面距離
     *
     * @param longitude1 起點經度
     * @param latitude1  起點緯度
     * @param longitude2 終點經度
     * @param latitude2  終點緯度
     * @return 距離（米）
     */
    public static double calculateDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 將經緯度轉換為弧度
        double radLat1 = Math.toRadians(latitude1);
        double radLat2 = Math.toRadians(latitude2);
        double radLon1 = Math.toRadians(longitude1);
        double radLon2 = Math.toRadians(longitude2);

        // Haversine 公式
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 距離（米）
        return EARTH_RADIUS * c;
    }

    /**
     * 判斷指定坐標是否在另一個坐標的指定範圍內
     *
     * @param longitude1 坐標1經度
     * @param latitude1  坐標1緯度
     * @param longitude2 坐標2經度
     * @param latitude2  坐標2緯度
     * @param range      範圍（米）
     * @return 是否在範圍內
     */
    public static boolean isInRange(double longitude1, double latitude1, double longitude2, double latitude2, double range) {
        double distance = calculateDistance(longitude1, latitude1, longitude2, latitude2);
        return distance <= range;
    }

    /**
     * 獲取指定坐標與另一個坐標的距離描述
     *
     * @param longitude1 坐標1經度
     * @param latitude1  坐標1緯度
     * @param longitude2 坐標2經度
     * @param latitude2  坐標2緯度
     * @return 距離描述
     */
    public static String getDistanceDescription(double longitude1, double latitude1, double longitude2, double latitude2) {
        double distance = calculateDistance(longitude1, latitude1, longitude2, latitude2);
        
        if (distance < 1000) {
            // 小於1公里，以米為單位
            return String.format("%.0f米", distance);
        } else {
            // 大於等於1公里，以公里為單位
            return String.format("%.2f公里", distance / 1000);
        }
    }
}
