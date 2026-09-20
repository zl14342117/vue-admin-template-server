-- 商品表（已有库可单独执行本文件）
SET NAMES utf8mb4;
USE vue_admin;

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
