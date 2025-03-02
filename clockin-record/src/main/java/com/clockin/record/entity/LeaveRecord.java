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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 請假記錄實體
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_leave_record")
@EntityListeners(AuditingEntityListener.class)
public class LeaveRecord {

    /**
     * 主鍵ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用戶ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 請假類型: 
     * 1-年假, 2-事假, 3-病假, 4-婚假, 5-產假, 6-喪假, 7-調休, 8-其他
     */
    @Column(name = "leave_type", nullable = false)
    private Integer leaveType;

    /**
     * 請假開始日期
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * 請假結束日期
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * 請假天數
     */
    @Column(name = "leave_days", nullable = false)
    private Float leaveDays;

    /**
     * 請假原因
     */
    @Column(name = "reason", length = 500)
    private String reason;

    /**
     * 審批狀態: 
     * 0-待審批, 1-已通過, 2-已拒絕, 3-已取消
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 審批意見
     */
    @Column(name = "approval_comment", length = 500)
    private String approvalComment;

    /**
     * 審批人ID
     */
    @Column(name = "approver_id")
    private Long approverId;

    /**
     * 審批時間
     */
    @Column(name = "approval_time")
    private LocalDateTime approvalTime;

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
     * 備註
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 刪除標記（0代表存在 1代表刪除）
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
