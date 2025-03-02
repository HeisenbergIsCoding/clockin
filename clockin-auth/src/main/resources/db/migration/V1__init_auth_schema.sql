-- 用戶表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用戶ID',
  `username` varchar(50) NOT NULL COMMENT '用戶名',
  `password` varchar(100) NOT NULL COMMENT '密碼',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真實姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '電子郵件',
  `phone` varchar(20) DEFAULT NULL COMMENT '手機號碼',
  `sex` tinyint DEFAULT '0' COMMENT '性別(0-未知 1-男 2-女)',
  `avatar` varchar(255) DEFAULT NULL COMMENT '頭像',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `dept_id` bigint DEFAULT NULL COMMENT '部門ID',
  `position` varchar(50) DEFAULT NULL COMMENT '職位',
  `employee_id` varchar(50) DEFAULT NULL COMMENT '工號',
  `entry_date` date DEFAULT NULL COMMENT '入職日期',
  `leave_date` date DEFAULT NULL COMMENT '離職日期',
  `last_login_time` datetime DEFAULT NULL COMMENT '最後登錄時間',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status` (`status`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用戶表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名稱',
  `role_key` varchar(50) NOT NULL COMMENT '角色權限字符串',
  `sort` int DEFAULT '0' COMMENT '顯示順序',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- 權限表
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '權限ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父權限ID',
  `name` varchar(50) NOT NULL COMMENT '權限名稱',
  `permission_key` varchar(100) NOT NULL COMMENT '權限標識',
  `type` tinyint NOT NULL COMMENT '類型(1-目錄 2-選單 3-按鈕)',
  `path` varchar(255) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '組件路徑',
  `sort` int DEFAULT '0' COMMENT '顯示順序',
  `icon` varchar(100) DEFAULT NULL COMMENT '圖標',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `visible` tinyint DEFAULT '1' COMMENT '是否顯示(0-隱藏 1-顯示)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_key` (`permission_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='權限表';

-- 用戶角色關聯表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用戶ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用戶角色關聯表';

-- 角色權限關聯表
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '權限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色權限關聯表';

-- 部門表
CREATE TABLE IF NOT EXISTS `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部門ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父部門ID',
  `dept_name` varchar(50) NOT NULL COMMENT '部門名稱',
  `sort` int DEFAULT '0' COMMENT '顯示順序',
  `leader` varchar(50) DEFAULT NULL COMMENT '負責人',
  `phone` varchar(20) DEFAULT NULL COMMENT '聯絡電話',
  `email` varchar(100) DEFAULT NULL COMMENT '電子郵件',
  `status` tinyint DEFAULT '1' COMMENT '狀態(0-禁用 1-啟用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `create_by` varchar(64) DEFAULT NULL COMMENT '創建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(500) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部門表';

-- 插入默認管理員用戶
INSERT INTO `sys_user` 
  (`username`, `password`, `real_name`, `email`, `status`, `position`, `employee_id`) 
VALUES 
  ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系統管理員', 'admin@clockin.com', 1, '系統管理員', '0001');

-- 插入默認角色
INSERT INTO `sys_role` 
  (`role_name`, `role_key`, `sort`, `status`, `remark`) 
VALUES 
  ('超級管理員', 'ROLE_ADMIN', 1, 1, '擁有所有功能權限'),
  ('普通用戶', 'ROLE_USER', 2, 1, '普通用戶角色');

-- 關聯管理員和角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);
