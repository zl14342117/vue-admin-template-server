-- 修复 nickname 中文乱码（已有库执行一次即可）
SET NAMES utf8mb4;
USE vue_admin;

UPDATE sys_user SET nickname = '超级管理员' WHERE username = 'admin';
UPDATE sys_user SET nickname = '运营编辑' WHERE username = 'editor';
UPDATE sys_user SET nickname = '张三' WHERE username = 'zhangsan';
UPDATE sys_user SET nickname = '李四' WHERE username = 'lisi';
UPDATE sys_user SET nickname = '王五' WHERE username = 'wangwu';

SELECT id, username, nickname, role, status FROM sys_user ORDER BY id;
