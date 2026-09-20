-- 订单表（已有库可单独执行本文件）
SET NAMES utf8mb4;
USE vue_admin;

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
