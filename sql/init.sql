-- vue-admin-template-server 初始化脚本
-- Docker 首次启动会自动执行；本机已有库也可手动：
--   mysql -h127.0.0.1 -uroot -proot123 --default-character-set=utf8mb4 < sql/init.sql

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS vue_admin
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE vue_admin;

CREATE TABLE IF NOT EXISTS sys_user (
  id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  username    VARCHAR(64)  NOT NULL COMMENT '登录用户名',
  password    VARCHAR(128) NOT NULL COMMENT '登录密码（练手明文）',
  nickname    VARCHAR(64)  NOT NULL COMMENT '昵称',
  role        VARCHAR(32)  NOT NULL COMMENT '角色：admin/editor/operator',
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- 种子数据（密码均为 111111）
INSERT INTO sys_user (id, username, password, nickname, role, status, created_at) VALUES
  (1, 'admin',    '111111', '超级管理员', 'admin',    1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
  (2, 'editor',   '111111', '运营编辑',   'editor',   1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
  (3, 'zhangsan', '111111', '张三',       'operator', 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
  (4, 'lisi',     '111111', '李四',       'operator', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
  (5, 'wangwu',   '111111', '王五',       'editor',   1, DATE_SUB(NOW(), INTERVAL 1 DAY))
ON DUPLICATE KEY UPDATE
  password = VALUES(password),
  nickname = VALUES(nickname),
  role = VALUES(role),
  status = VALUES(status);
