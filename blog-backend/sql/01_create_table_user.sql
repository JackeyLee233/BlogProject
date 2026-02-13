-- =============================================
-- 博客系统 - 用户表
-- =============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `blog` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `blog`;

-- 删除表（如果存在）
DROP TABLE IF EXISTS `t_user`;

-- 创建用户表
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名（登录账号）',
  `password` varchar(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称（显示用）',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) NOT NULL DEFAULT 'ADMIN' COMMENT '角色：ADMIN-管理员',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入初始管理员账号
-- 用户名：admin
-- 密码：admin123（BCrypt 加密后）
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '管理员', 'ADMIN', 1);

-- 注意：上面的密码是示例，实际部署时应该：
-- 1. 使用 BCryptPasswordEncoder 生成真实密码
-- 2. 在 Java 代码中生成：new BCryptPasswordEncoder().encode("admin123")
