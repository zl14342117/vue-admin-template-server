-- 客户表（已有库可单独执行本文件）
SET NAMES utf8mb4;
USE vue_admin;

CREATE TABLE IF NOT EXISTS sys_customer (
  id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  customer_no  VARCHAR(32)  NOT NULL COMMENT '客户编号',
  name         VARCHAR(64)  NOT NULL COMMENT '客户名称',
  contact      VARCHAR(64)  NOT NULL COMMENT '联系人',
  phone        VARCHAR(32)  NOT NULL COMMENT '手机号',
  status       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
  created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_customer_no (customer_no),
  KEY idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户';

INSERT INTO sys_customer (id, customer_no, name, contact, phone, status, created_at) VALUES
  (1, 'C202609150001', '星河科技', '王强', '13800001111', 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
  (2, 'C202609160001', '青禾贸易', '李敏', '13900002222', 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
  (3, 'C202609170001', '云帆物流', '赵磊', '13700003333', 0, DATE_SUB(NOW(), INTERVAL 3 DAY)),
  (4, 'C202609180001', '蓝海电商', '周倩', '13600004444', 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
  (5, 'C202609190001', '南山食品', '陈晨', '13500005555', 1, DATE_SUB(NOW(), INTERVAL 1 DAY))
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  contact = VALUES(contact),
  phone = VALUES(phone),
  status = VALUES(status);
