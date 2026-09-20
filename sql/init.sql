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

CREATE TABLE IF NOT EXISTS sys_product (
  id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  name        VARCHAR(128)   NOT NULL COMMENT '商品名称',
  code        VARCHAR(64)    NOT NULL COMMENT '商品编码',
  category    VARCHAR(32)    NOT NULL COMMENT '分类：digital/daily/food',
  price       DECIMAL(12, 2) NOT NULL COMMENT '单价',
  stock       INT            NOT NULL DEFAULT 0 COMMENT '库存',
  status      TINYINT        NOT NULL DEFAULT 1 COMMENT '状态：1上架 0下架',
  created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品';

INSERT INTO sys_product (id, name, code, category, price, stock, status, created_at) VALUES
  (1, '无线鼠标', 'P-DIG-001', 'digital', 99.00, 120, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
  (2, '机械键盘', 'P-DIG-002', 'digital', 399.00, 45, 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
  (3, '保温杯', 'P-DAY-001', 'daily', 59.90, 200, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
  (4, '抽纸箱装', 'P-DAY-002', 'daily', 29.90, 0, 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
  (5, '坚果礼盒', 'P-FOOD-001', 'food', 128.00, 80, 1, DATE_SUB(NOW(), INTERVAL 1 DAY))
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  category = VALUES(category),
  price = VALUES(price),
  stock = VALUES(stock),
  status = VALUES(status);

CREATE TABLE IF NOT EXISTS sys_order (
  id            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_no      VARCHAR(32)    NOT NULL COMMENT '订单号',
  customer_id   BIGINT         NOT NULL COMMENT '客户ID',
  customer_name VARCHAR(64)    NOT NULL COMMENT '客户名称快照',
  total_amount  DECIMAL(12, 2) NOT NULL COMMENT '订单总金额',
  status        TINYINT        NOT NULL DEFAULT 0 COMMENT '0待支付 1已支付 2已取消',
  created_at    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_customer_id (customer_id),
  KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

CREATE TABLE IF NOT EXISTS sys_order_item (
  id            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_id      BIGINT         NOT NULL COMMENT '订单ID',
  product_id    BIGINT         NOT NULL COMMENT '商品ID',
  product_name  VARCHAR(128)   NOT NULL COMMENT '商品名称快照',
  product_code  VARCHAR(64)    NOT NULL COMMENT '商品编码快照',
  price         DECIMAL(12, 2) NOT NULL COMMENT '下单单价',
  quantity      INT            NOT NULL COMMENT '数量',
  amount        DECIMAL(12, 2) NOT NULL COMMENT '小计',
  PRIMARY KEY (id),
  KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细';
