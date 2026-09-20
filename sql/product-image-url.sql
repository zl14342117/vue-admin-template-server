-- 商品表增加图片字段（已有库执行）
SET NAMES utf8mb4;
USE vue_admin;

ALTER TABLE sys_product
  ADD COLUMN image_url VARCHAR(255) DEFAULT NULL COMMENT '商品图片路径' AFTER status;
