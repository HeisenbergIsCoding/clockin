package com.clockin.record.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 工作地點實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_work_location")
@EntityListeners(AuditingEntityListener.class)
public class WorkLocation {

    /**
     * 主鍵ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地點名稱
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 地點詳細地址
     */
    @Column(name = "address", nullable = false, length = 200)
    private String address;

    /**
     * 經度
     */
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    /**
     * 緯度
     */
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    /**
     * 允許範圍（米）
     */
    @Column(name = "allowed_range", nullable = false)
    private Integer allowedRange;

    /**
     * WiFi名稱
     */
    @Column(name = "wifi_ssid", length = 100)
    private String wifiSsid;

    /**
     * WiFi MAC地址
     */
    @Column(name = "wifi_mac", length = 100)
    private String wifiMac;

    /**
     * 是否啟用WiFi打卡驗證
     */
    @Column(name = "enable_wifi_validation", nullable = false)
    private Boolean enableWifiValidation;

    /**
     * 創建時間
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 創建者
     */
    @CreatedBy
    @Column(name = "create_by", updatable = false)
    private String createBy;

    /**
     * 更新時間
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 是否啟用
     */
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    /**
     * 刪除標記（0代表存在 1代表刪除）
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * 備註
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
