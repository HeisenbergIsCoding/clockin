-- 創建數據庫
CREATE DATABASE IF NOT EXISTS clockin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE clockin;

-- 用戶表
CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主鍵ID',
  email VARCHAR(100) NOT NULL COMMENT '郵箱（Google帳號）',
  name VARCHAR(50) NOT NULL COMMENT '姓名',
  avatar VARCHAR(255) DEFAULT NULL COMMENT '頭像URL',
  role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：USER-普通用戶, ADMIN-管理員',
  enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否啟用：0-禁用, 1-啟用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否刪除：0-未刪除, 1-已刪除',
  UNIQUE KEY uk_email (email, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用戶表';

-- 打卡記錄表
CREATE TABLE IF NOT EXISTS clock_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主鍵ID',
  user_id BIGINT NOT NULL COMMENT '用戶ID',
  clock_type TINYINT NOT NULL COMMENT '打卡類型：1-上班, 2-下班',
  clock_time DATETIME NOT NULL COMMENT '打卡時間',
  clock_date DATE NOT NULL COMMENT '打卡日期',
  clock_status TINYINT NOT NULL DEFAULT 0 COMMENT '打卡狀態：0-正常, 1-遲到, 2-早退',
  location VARCHAR(255) DEFAULT NULL COMMENT '打卡位置',
  remark VARCHAR(255) DEFAULT NULL COMMENT '備註',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否刪除：0-未刪除, 1-已刪除',
  INDEX idx_user_date (user_id, clock_date, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡記錄表';

-- 工作時間設置表
CREATE TABLE IF NOT EXISTS work_time_config (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主鍵ID',
  start_time TIME NOT NULL COMMENT '上班時間',
  end_time TIME NOT NULL COMMENT '下班時間',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expire_date DATE DEFAULT NULL COMMENT '失效日期，空表示永久有效',
  remark VARCHAR(255) DEFAULT NULL COMMENT '備註',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否刪除：0-未刪除, 1-已刪除',
  UNIQUE KEY uk_date (effective_date, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作時間設置表';

-- 假日表
CREATE TABLE IF NOT EXISTS holiday (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主鍵ID',
  holiday_date DATE NOT NULL COMMENT '假日日期',
  holiday_type TINYINT NOT NULL COMMENT '假日類型：1-週末, 2-國定假日, 3-公司假日',
  holiday_name VARCHAR(50) DEFAULT NULL COMMENT '假日名稱',
  is_work_day TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否工作日：0-不是, 1-是（例如週末需要補班）',
  remark VARCHAR(255) DEFAULT NULL COMMENT '備註',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否刪除：0-未刪除, 1-已刪除',
  UNIQUE KEY uk_date (holiday_date, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='假日表';

-- 初始化管理員
INSERT INTO sys_user (email, name, role, enabled) VALUES ('admin@example.com', '系統管理員', 'ADMIN', 1);
