-- 角色表（已有库可单独执行本文件）
SET NAMES utf8mb4;
USE vue_admin;

CREATE TABLE IF NOT EXISTS sys_role (
  id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  name         VARCHAR(64)  NOT NULL COMMENT '角色名称',
  code         VARCHAR(32)  NOT NULL COMMENT '角色标识',
  description  VARCHAR(255) DEFAULT NULL COMMENT '描述',
  created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色';

INSERT INTO sys_role (id, name, code, description, created_at) VALUES
  (1, '管理员', 'admin', '系统内置管理员，拥有全部权限', DATE_SUB(NOW(), INTERVAL 5 DAY)),
  (2, '运营', 'editor', '内容与运营相关权限', DATE_SUB(NOW(), INTERVAL 4 DAY)),
  (3, '普通用户', 'operator', '基础业务操作权限', DATE_SUB(NOW(), INTERVAL 3 DAY))
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  description = VALUES(description);
