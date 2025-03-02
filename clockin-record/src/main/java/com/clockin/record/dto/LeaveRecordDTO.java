package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 請假記錄數據傳輸對象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRecordDTO {

    /**
     * 主鍵ID
     */
    private Long id;

    /**
     * 用戶ID
     */
    private Long userId;

    /**
     * 用戶名稱
     */
    private String userName;

    /**
     * 部門ID
     */
    private Long departmentId;

    /**
     * 部門名稱
     */
    private String departmentName;

    /**
     * 請假類型ID
     */
    private Integer leaveType;

    /**
     * 請假類型名稱
     */
    private String leaveTypeName;

    /**
     * 請假開始日期
     */
    private LocalDate startDate;

    /**
     * 請假結束日期
     */
    private LocalDate endDate;

    /**
     * 請假天數
     */
    private Float leaveDays;

    /**
     * 請假原因
     */
    private String reason;

    /**
     * 審批狀態
     */
    private Integer status;

    /**
     * 審批狀態名稱
     */
    private String statusName;

    /**
     * 審批意見
     */
    private String approvalComment;

    /**
     * 審批人ID
     */
    private Long approverId;

    /**
     * 審批人名稱
     */
    private String approverName;

    /**
     * 審批時間
     */
    private LocalDateTime approvalTime;

    /**
     * 創建時間
     */
    private LocalDateTime createTime;

    /**
     * 創建者
     */
    private String createBy;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 備註
     */
    private String remark;
}
