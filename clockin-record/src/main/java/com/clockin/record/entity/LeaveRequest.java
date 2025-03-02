package com.clockin.record.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 請假申請實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leave_request")
public class LeaveRequest {

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
     * 請假類型
     */
    @Column(name = "leave_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    /**
     * 開始時間
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 結束時間
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 總天數
     */
    @Column(name = "total_days", nullable = false, precision = 5, scale = 1)
    private BigDecimal totalDays;

    /**
     * 請假原因
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
     * 請假類型枚舉
     */
    public enum LeaveType {
        /**
         * 病假
         */
        SICK,
        
        /**
         * 年假
         */
        ANNUAL,
        
        /**
         * 事假
         */
        PERSONAL,
        
        /**
         * 產假
         */
        MATERNITY,
        
        /**
         * 陪產假
         */
        PATERNITY,
        
        /**
         * 其他
         */
        OTHER
    }

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
