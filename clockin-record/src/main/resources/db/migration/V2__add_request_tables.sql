-- 補卡申請表
CREATE TABLE makeup_clock_in_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '申請用戶ID',
    user_name VARCHAR(64) NOT NULL COMMENT '申請用戶名稱',
    request_date DATE NOT NULL COMMENT '申請日期',
    request_time DATETIME NOT NULL COMMENT '補卡時間',
    clock_in_type VARCHAR(20) NOT NULL COMMENT '打卡類型',
    reason VARCHAR(512) NOT NULL COMMENT '補卡原因',
    status VARCHAR(20) NOT NULL COMMENT '狀態',
    approver_id BIGINT COMMENT '審批人ID',
    approver_name VARCHAR(64) COMMENT '審批人名稱',
    approval_time DATETIME COMMENT '審批時間',
    approval_comment VARCHAR(512) COMMENT '審批意見',
    created_time DATETIME NOT NULL COMMENT '創建時間',
    updated_time DATETIME NOT NULL COMMENT '更新時間',
    INDEX idx_user_id (user_id),
    INDEX idx_request_date (request_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='補卡申請表';

-- 請假申請表
CREATE TABLE leave_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '申請用戶ID',
    user_name VARCHAR(64) NOT NULL COMMENT '申請用戶名稱',
    leave_type VARCHAR(20) NOT NULL COMMENT '請假類型',
    start_time DATETIME NOT NULL COMMENT '開始時間',
    end_time DATETIME NOT NULL COMMENT '結束時間',
    total_days DECIMAL(5,1) NOT NULL COMMENT '總天數',
    reason VARCHAR(512) NOT NULL COMMENT '請假原因',
    status VARCHAR(20) NOT NULL COMMENT '狀態',
    approver_id BIGINT COMMENT '審批人ID',
    approver_name VARCHAR(64) COMMENT '審批人名稱',
    approval_time DATETIME COMMENT '審批時間',
    approval_comment VARCHAR(512) COMMENT '審批意見',
    created_time DATETIME NOT NULL COMMENT '創建時間',
    updated_time DATETIME NOT NULL COMMENT '更新時間',
    INDEX idx_user_id (user_id),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='請假申請表';

-- 注意：權限相關的插入操作應該在 auth 模塊中執行
-- 已移除對 sys_permission 表的插入操作
