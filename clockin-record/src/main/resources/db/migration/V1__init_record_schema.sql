-- 打卡記錄表
CREATE TABLE IF NOT EXISTS `clock_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '記錄ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `clock_type` tinyint NOT NULL COMMENT '打卡類型(1-上班 2-下班)',
  `clock_time` datetime NOT NULL COMMENT '打卡時間',
  `clock_date` date NOT NULL COMMENT '打卡日期',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-異常 1-正常)',
  `location` varchar(255) DEFAULT NULL COMMENT '打卡位置',
  `device` varchar(100) DEFAULT NULL COMMENT '設備信息',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `is_late` tinyint(1) DEFAULT '0' COMMENT '是否遲到',
  `is_early_leave` tinyint(1) DEFAULT '0' COMMENT '是否早退',
  `is_leave` tinyint(1) DEFAULT '0' COMMENT '是否請假',
  `is_overtime` tinyint(1) DEFAULT '0' COMMENT '是否加班',
  `work_hours` decimal(4,2) DEFAULT NULL COMMENT '工作時長(小時)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`,`clock_date`),
  KEY `idx_clock_date` (`clock_date`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡記錄表';

-- 打卡日報表
CREATE TABLE IF NOT EXISTS `clock_daily_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `summary_date` date NOT NULL COMMENT '統計日期',
  `first_clock_in_time` datetime DEFAULT NULL COMMENT '首次打卡上班時間',
  `last_clock_out_time` datetime DEFAULT NULL COMMENT '最後打卡下班時間',
  `work_hours` decimal(4,2) DEFAULT NULL COMMENT '工作時長(小時)',
  `overtime_hours` decimal(4,2) DEFAULT NULL COMMENT '加班時長(小時)',
  `is_late` tinyint(1) DEFAULT '0' COMMENT '是否遲到',
  `is_early_leave` tinyint(1) DEFAULT '0' COMMENT '是否早退',
  `is_absent` tinyint(1) DEFAULT '0' COMMENT '是否缺勤',
  `is_leave` tinyint(1) DEFAULT '0' COMMENT '是否請假',
  `is_rest_day` tinyint(1) DEFAULT '0' COMMENT '是否休息日',
  `is_holiday` tinyint(1) DEFAULT '0' COMMENT '是否假日',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-異常 1-正常)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`,`summary_date`),
  KEY `idx_summary_date` (`summary_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡日報表';

-- 打卡月報表
CREATE TABLE IF NOT EXISTS `clock_monthly_summary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `year` int NOT NULL COMMENT '年份',
  `month` int NOT NULL COMMENT '月份',
  `work_days` int DEFAULT '0' COMMENT '工作天數',
  `work_hours` decimal(6,2) DEFAULT '0.00' COMMENT '總工作時長(小時)',
  `overtime_hours` decimal(6,2) DEFAULT '0.00' COMMENT '總加班時長(小時)',
  `late_count` int DEFAULT '0' COMMENT '遲到次數',
  `early_leave_count` int DEFAULT '0' COMMENT '早退次數',
  `absent_count` int DEFAULT '0' COMMENT '缺勤次數',
  `leave_count` int DEFAULT '0' COMMENT '請假次數',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-未完成 1-已完成)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_year_month` (`user_id`,`year`,`month`),
  KEY `idx_year_month` (`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡月報表';

-- 請假記錄表
CREATE TABLE IF NOT EXISTS `leave_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '記錄ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `leave_type` tinyint NOT NULL COMMENT '請假類型(1-事假 2-病假 3-年假 4-調休 5-其他)',
  `start_time` datetime NOT NULL COMMENT '開始時間',
  `end_time` datetime NOT NULL COMMENT '結束時間',
  `duration` decimal(6,2) NOT NULL COMMENT '請假時長(小時)',
  `reason` varchar(500) DEFAULT NULL COMMENT '請假原因',
  `status` tinyint DEFAULT '0' COMMENT '狀態(0-待審批 1-已批准 2-已拒絕 3-已取消)',
  `approver_id` bigint DEFAULT NULL COMMENT '審批人ID',
  `approval_time` datetime DEFAULT NULL COMMENT '審批時間',
  `approval_remark` varchar(500) DEFAULT NULL COMMENT '審批備註',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_date_range` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='請假記錄表';

-- 加班記錄表
CREATE TABLE IF NOT EXISTS `overtime_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '記錄ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `overtime_type` tinyint NOT NULL COMMENT '加班類型(1-工作日加班 2-休息日加班 3-節假日加班)',
  `start_time` datetime NOT NULL COMMENT '開始時間',
  `end_time` datetime NOT NULL COMMENT '結束時間',
  `duration` decimal(4,2) NOT NULL COMMENT '加班時長(小時)',
  `reason` varchar(500) DEFAULT NULL COMMENT '加班原因',
  `status` tinyint DEFAULT '0' COMMENT '狀態(0-待審批 1-已批准 2-已拒絕 3-已取消)',
  `approver_id` bigint DEFAULT NULL COMMENT '審批人ID',
  `approval_time` datetime DEFAULT NULL COMMENT '審批時間',
  `approval_remark` varchar(500) DEFAULT NULL COMMENT '審批備註',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_date_range` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='加班記錄表';
