package com.clockin.record.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 補卡申請實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "makeup_clock_in_request")
public class MakeupClockInRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 申請用戶ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 申請用戶名稱
     */
    @Column(name = "user_name", nullable = false, length = 64)
    private String userName;

    /**
     * 申請日期
     */
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    /**
     * 補卡時間
     */
    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;

    /**
     * 打卡類型
     */
    @Column(name = "clock_in_type", nullable = false, length = 20)
    private String clockInType;

    /**
     * 補卡原因
     */
    @Column(name = "reason", nullable = false, length = 512)
    private String reason;

    /**
     * 狀態
     */
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    /**
     * 審批人ID
     */
    @Column(name = "approver_id")
    private Long approverId;

    /**
     * 審批人名稱
     */
    @Column(name = "approver_name", length = 64)
    private String approverName;

    /**
     * 審批時間
     */
    @Column(name = "approval_time")
    private LocalDateTime approvalTime;

    /**
     * 審批意見
     */
    @Column(name = "approval_comment", length = 512)
    private String approvalComment;

    /**
     * 創建時間
     */
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新時間
     */
    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    /**
     * 請求狀態枚舉
     */
    public enum RequestStatus {
        /**
         * 待審批
         */
        PENDING,
        
        /**
         * 已批准
         */
        APPROVED,
        
        /**
         * 已拒絕
         */
        REJECTED
    }
}
