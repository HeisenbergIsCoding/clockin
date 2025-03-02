-- 部門表
CREATE TABLE IF NOT EXISTS `sys_department` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部門ID',
    `dept_name` varchar(50) NOT NULL COMMENT '部門名稱',
    `dept_code` varchar(30) COMMENT '部門編碼',
    `parent_id` bigint(20) COMMENT '父部門ID',
    `order_num` int(4) COMMENT '排序',
    `leader_id` bigint(20) COMMENT '負責人ID',
    `leader_name` varchar(50) COMMENT '負責人姓名',
    `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '部門狀態（0停用 1正常）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`dept_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='部門表';

-- 崗位表
CREATE TABLE IF NOT EXISTS `sys_position` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '崗位ID',
    `position_name` varchar(50) NOT NULL COMMENT '崗位名稱',
    `position_code` varchar(30) COMMENT '崗位編碼',
    `order_num` int(4) COMMENT '排序',
    `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '崗位狀態（0停用 1正常）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_position_code` (`position_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='崗位表';

-- 添加部門相關權限
INSERT INTO `sys_permission` (`id`, `parent_id`, `permission_name`, `permission_code`, `permission_type`, `path`, `component`, `redirect`, `icon`, `order_num`, `status`, `create_time`, `update_time`) VALUES
(50, 1, '部門管理', 'system:dept:list', 'menu', '/system/dept', 'system/dept/index', NULL, 'department', 4, 1, NOW(), NOW()),
(51, 50, '部門查詢', 'system:dept:query', 'button', NULL, NULL, NULL, NULL, 1, 1, NOW(), NOW()),
(52, 50, '部門新增', 'system:dept:add', 'button', NULL, NULL, NULL, NULL, 2, 1, NOW(), NOW()),
(53, 50, '部門修改', 'system:dept:edit', 'button', NULL, NULL, NULL, NULL, 3, 1, NOW(), NOW()),
(54, 50, '部門刪除', 'system:dept:delete', 'button', NULL, NULL, NULL, NULL, 4, 1, NOW(), NOW());

-- 添加崗位相關權限
INSERT INTO `sys_permission` (`id`, `parent_id`, `permission_name`, `permission_code`, `permission_type`, `path`, `component`, `redirect`, `icon`, `order_num`, `status`, `create_time`, `update_time`) VALUES
(60, 1, '崗位管理', 'system:position:list', 'menu', '/system/position', 'system/position/index', NULL, 'position', 5, 1, NOW(), NOW()),
(61, 60, '崗位查詢', 'system:position:query', 'button', NULL, NULL, NULL, NULL, 1, 1, NOW(), NOW()),
(62, 60, '崗位新增', 'system:position:add', 'button', NULL, NULL, NULL, NULL, 2, 1, NOW(), NOW()),
(63, 60, '崗位修改', 'system:position:edit', 'button', NULL, NULL, NULL, NULL, 3, 1, NOW(), NOW()),
(64, 60, '崗位刪除', 'system:position:delete', 'button', NULL, NULL, NULL, NULL, 4, 1, NOW(), NOW());

-- 添加默認部門數據
INSERT INTO `sys_department` (`id`, `dept_name`, `dept_code`, `parent_id`, `order_num`, `status`, `create_time`, `update_time`) VALUES
(100, '總公司', 'HQ', NULL, 1, 1, NOW(), NOW()),
(101, '人力資源部', 'HR', 100, 1, 1, NOW(), NOW()),
(102, '技術部', 'TECH', 100, 2, 1, NOW(), NOW()),
(103, '財務部', 'FIN', 100, 3, 1, NOW(), NOW()),
(104, '市場部', 'MKT', 100, 4, 1, NOW(), NOW()),
(105, '研發部', 'R&D', 102, 1, 1, NOW(), NOW()),
(106, '測試部', 'QA', 102, 2, 1, NOW(), NOW());

-- 添加默認崗位數據
INSERT INTO `sys_position` (`id`, `position_name`, `position_code`, `order_num`, `status`, `create_time`, `update_time`) VALUES
(100, '總經理', 'GM', 1, 1, NOW(), NOW()),
(101, '部門經理', 'DM', 2, 1, NOW(), NOW()),
(102, '技術主管', 'TL', 3, 1, NOW(), NOW()),
(103, '高級工程師', 'SE', 4, 1, NOW(), NOW()),
(104, '工程師', 'ENG', 5, 1, NOW(), NOW()),
(105, '實習生', 'INTERN', 6, 1, NOW(), NOW());

-- 修改用戶表，添加部門和崗位外鍵
ALTER TABLE `sys_user` 
ADD COLUMN `department_id` bigint(20) COMMENT '部門ID',
ADD COLUMN `position_id` bigint(20) COMMENT '崗位ID',
ADD CONSTRAINT `fk_user_dept` FOREIGN KEY (`department_id`) REFERENCES `sys_department` (`id`) ON DELETE SET NULL,
ADD CONSTRAINT `fk_user_position` FOREIGN KEY (`position_id`) REFERENCES `sys_position` (`id`) ON DELETE SET NULL;
