-- 初始化資料庫結構

-- 工作時間配置表
CREATE TABLE IF NOT EXISTS `work_time_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(50) NOT NULL COMMENT '配置名稱',
  `work_start_time` time NOT NULL COMMENT '上班時間',
  `work_end_time` time NOT NULL COMMENT '下班時間',
  `lunch_start_time` time DEFAULT NULL COMMENT '午休開始時間',
  `lunch_end_time` time DEFAULT NULL COMMENT '午休結束時間',
  `daily_work_hours` decimal(4,2) NOT NULL COMMENT '工作日每天工作時長(小時)',
  `weekly_work_days` int NOT NULL COMMENT '每週工作天數',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否設置為預設配置',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `latest_clock_in_time` time DEFAULT NULL COMMENT '最遲上班時間(遲到判斷時間)',
  `earliest_clock_out_time` time DEFAULT NULL COMMENT '最早下班時間(早退判斷時間)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_name` (`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工作時間配置表';

-- 假日配置表
CREATE TABLE IF NOT EXISTS `holiday` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '假日ID',
  `holiday_date` date NOT NULL COMMENT '假日日期',
  `holiday_name` varchar(50) NOT NULL COMMENT '假日名稱',
  `holiday_type` int NOT NULL COMMENT '假日類型(1-法定節假日 2-公司假日 3-調休日 4-工作日)',
  `year` int NOT NULL COMMENT '年份',
  `month` int NOT NULL COMMENT '月份',
  `day` int NOT NULL COMMENT '日',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_holiday_date` (`holiday_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='假日配置表';

-- 用戶工作時間配置關聯表
CREATE TABLE IF NOT EXISTS `user_work_time_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '關聯ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `config_id` bigint NOT NULL COMMENT '配置ID',
  `effective_start_date` date NOT NULL COMMENT '生效開始日期',
  `effective_end_date` date DEFAULT NULL COMMENT '生效結束日期',
  `is_current` tinyint(1) DEFAULT '0' COMMENT '是否為當前生效配置',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_config_id` (`config_id`),
  KEY `idx_effective_date` (`effective_start_date`,`effective_end_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用戶工作時間配置關聯表';

-- 插入默認工作時間配置
INSERT INTO `work_time_config` 
  (`config_name`, `work_start_time`, `work_end_time`, `lunch_start_time`, `lunch_end_time`, 
   `daily_work_hours`, `weekly_work_days`, `is_default`, `status`, 
   `latest_clock_in_time`, `earliest_clock_out_time`, `remark`) 
VALUES 
  ('標準工作配置', '09:00:00', '18:00:00', '12:00:00', '13:00:00', 
   8.00, 5, 1, 1, 
   '09:30:00', '17:30:00', '標準辦公時間 9 點至 18 點，含 1 小時午休');
