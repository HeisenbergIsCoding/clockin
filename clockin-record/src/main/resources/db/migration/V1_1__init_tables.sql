-- 建立打卡記錄表
CREATE TABLE IF NOT EXISTS `clock_in_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `user_name` varchar(64) NOT NULL COMMENT '用戶名稱',
  `clock_in_time` datetime NOT NULL COMMENT '打卡時間',
  `clock_in_type` varchar(20) NOT NULL COMMENT '打卡類型 (CLOCK_IN, CLOCK_OUT)',
  `clock_in_status` varchar(20) NOT NULL COMMENT '打卡狀態 (NORMAL, LATE, EARLY_LEAVE, MAKEUP)',
  `clock_in_location` varchar(255) DEFAULT NULL COMMENT '打卡位置',
  `notes` varchar(255) DEFAULT NULL COMMENT '備註',
  `device_info` varchar(255) DEFAULT NULL COMMENT '設備信息',
  `ip_address` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  KEY `idx_user_id_clock_in_time` (`user_id`, `clock_in_time`),
  KEY `idx_clock_in_time` (`clock_in_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡記錄表';

-- 建立打卡匯總表
CREATE TABLE IF NOT EXISTS `clock_in_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `user_name` varchar(64) NOT NULL COMMENT '用戶名稱',
  `record_date` date NOT NULL COMMENT '記錄日期',
  `first_clock_in_id` bigint DEFAULT NULL COMMENT '首次打卡進入記錄ID',
  `first_clock_in_time` datetime DEFAULT NULL COMMENT '首次打卡進入時間',
  `first_clock_in_status` varchar(20) DEFAULT NULL COMMENT '首次打卡進入狀態',
  `last_clock_out_id` bigint DEFAULT NULL COMMENT '最後打卡離開記錄ID',
  `last_clock_out_time` datetime DEFAULT NULL COMMENT '最後打卡離開時間',
  `last_clock_out_status` varchar(20) DEFAULT NULL COMMENT '最後打卡離開狀態',
  `total_work_minutes` int DEFAULT '0' COMMENT '總工作時長(分鐘)',
  `absence_type` varchar(20) DEFAULT 'NORMAL' COMMENT '缺勤類型 (NORMAL, LATE, EARLY_LEAVE, ABSENT, LEAVE, BUSINESS_TRIP)',
  `absence_minutes` int DEFAULT '0' COMMENT '缺勤時長(分鐘)',
  `is_complete` tinyint(1) DEFAULT '0' COMMENT '打卡是否完整',
  `is_workday` tinyint(1) DEFAULT '1' COMMENT '是否工作日',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id_record_date` (`user_id`, `record_date`),
  KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡匯總表';

-- 建立補卡申請表
CREATE TABLE IF NOT EXISTS `makeup_clock_in_request` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `user_id` bigint NOT NULL COMMENT '申請用戶ID',
  `user_name` varchar(64) NOT NULL COMMENT '申請用戶名稱',
  `request_date` date NOT NULL COMMENT '申請日期',
  `request_time` datetime NOT NULL COMMENT '補卡時間',
  `clock_in_type` varchar(20) NOT NULL COMMENT '打卡類型 (CLOCK_IN, CLOCK_OUT)',
  `reason` varchar(512) NOT NULL COMMENT '補卡原因',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '狀態 (PENDING, APPROVED, REJECTED)',
  `approver_id` bigint DEFAULT NULL COMMENT '審批人ID',
  `approver_name` varchar(64) DEFAULT NULL COMMENT '審批人名稱',
  `approval_time` datetime DEFAULT NULL COMMENT '審批時間',
  `approval_comment` varchar(512) DEFAULT NULL COMMENT '審批意見',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  KEY `idx_user_id_request_date` (`user_id`, `request_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='補卡申請表';

-- 建立請假申請表
CREATE TABLE IF NOT EXISTS `leave_request` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `user_id` bigint NOT NULL COMMENT '申請用戶ID',
  `user_name` varchar(64) NOT NULL COMMENT '申請用戶名稱',
  `leave_type` varchar(20) NOT NULL COMMENT '請假類型 (SICK, ANNUAL, PERSONAL, MATERNITY, PATERNITY, OTHER)',
  `start_time` datetime NOT NULL COMMENT '開始時間',
  `end_time` datetime NOT NULL COMMENT '結束時間',
  `total_days` decimal(5,1) NOT NULL COMMENT '總天數',
  `reason` varchar(512) NOT NULL COMMENT '請假原因',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '狀態 (PENDING, APPROVED, REJECTED)',
  `approver_id` bigint DEFAULT NULL COMMENT '審批人ID',
  `approver_name` varchar(64) DEFAULT NULL COMMENT '審批人名稱',
  `approval_time` datetime DEFAULT NULL COMMENT '審批時間',
  `approval_comment` varchar(512) DEFAULT NULL COMMENT '審批意見',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  KEY `idx_user_id_start_time_end_time` (`user_id`, `start_time`, `end_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='請假申請表';

-- 建立工作時間配置表
CREATE TABLE IF NOT EXISTS `work_time_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `user_id` bigint DEFAULT NULL COMMENT '用戶ID，為NULL表示全局配置',
  `department_id` bigint DEFAULT NULL COMMENT '部門ID，為NULL表示全局配置',
  `morning_start_time` time NOT NULL DEFAULT '09:00:00' COMMENT '上午上班時間',
  `morning_end_time` time NOT NULL DEFAULT '12:00:00' COMMENT '上午下班時間',
  `afternoon_start_time` time NOT NULL DEFAULT '13:00:00' COMMENT '下午上班時間',
  `afternoon_end_time` time NOT NULL DEFAULT '18:00:00' COMMENT '下午下班時間',
  `flexible_minutes` int NOT NULL DEFAULT '10' COMMENT '彈性時間(分鐘)',
  `effective_date` date NOT NULL COMMENT '生效日期',
  `expired_date` date DEFAULT NULL COMMENT '過期日期，為NULL表示永久有效',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否啟用',
  `priority` int NOT NULL DEFAULT '0' COMMENT '優先級，數字越大優先級越高',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_effective_date_expired_date` (`effective_date`, `expired_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工作時間配置表';

-- 建立節假日配置表
CREATE TABLE IF NOT EXISTS `holiday_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `holiday_date` date NOT NULL COMMENT '假日日期',
  `holiday_name` varchar(100) NOT NULL COMMENT '假日名稱',
  `holiday_type` varchar(20) NOT NULL COMMENT '假日類型 (PUBLIC_HOLIDAY, COMPANY_HOLIDAY, WEEKEND_ADJUSTMENT)',
  `is_workday` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否工作日',
  `remark` varchar(255) DEFAULT NULL COMMENT '備註',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_holiday_date` (`holiday_date`),
  KEY `idx_holiday_type` (`holiday_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='節假日配置表';
