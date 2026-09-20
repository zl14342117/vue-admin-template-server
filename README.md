# vue-admin-template-server

配套 [vue-admin-template](https://github.com/zl14342117/vue-admin-template) 的 **Spring Boot 练手后端**。

用户数据已接入 **真实 MySQL**（MyBatis-Plus），登录与用户管理 CRUD 共用 `sys_user` 表。

## 技术栈

- Java 8
- Spring Boot 2.7
- MyBatis-Plus 3.5
- MySQL 8（Docker 一键启动）

## 项目结构

```text
vue-admin-template-server/
├── docker-compose.yml                 # 本地 MySQL
├── sql/init.sql                       # 建库建表 + 种子用户
├── pom.xml
└── src/main/java/com/demo/admin/
    ├── VueAdminTemplateApplication.java
    ├── config/                        # 跨域、MyBatis-Plus 分页
    ├── controller/                    # 接口层（对应前端 api/）
    ├── mapper/                        # 数据访问层（对应表）
    ├── service/                       # 业务层
    │   └── impl/
    ├── pojo/
    │   ├── entity/                    # 表实体 DO
    │   ├── dto/                       # 入参
    │   └── vo/                        # 出参
    ├── common/
    └── exception/
```

和 `dc-portal-server` 的对应关系：

| 本 Demo | dc-portal-server |
|--------|------------------|
| `Controller` | `Controller` |
| `Service` / `ServiceImpl` | `Service` / `ServiceImpl` |
| `Mapper` + MyBatis-Plus | `Mapper` + MyBatis-Plus |
| `dto` / `vo` / `entity` | `pojo/dto` / `pojo/vo` / DO |
| `ApiResponse` | `RetVo` |

## 已实现接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/vue-admin-template/user/login` | 登录 |
| GET | `/vue-admin-template/user/info?token=xxx` | 获取用户信息 |
| POST | `/vue-admin-template/user/logout` | 登出 |
| GET | `/vue-admin-template/system/user/list` | 用户分页列表 |
| GET | `/vue-admin-template/system/user/{id}` | 用户详情 |
| POST | `/vue-admin-template/system/user` | 新增用户 |
| PUT | `/vue-admin-template/system/user/{id}` | 编辑用户 |
| DELETE | `/vue-admin-template/system/user/{id}` | 删除用户 |

统一响应格式（与前端 `request.js` 一致）：

```json
{
  "code": 20000,
  "message": "success",
  "data": {}
}
```

## 1. 启动 MySQL（必须先做）

本机未装 MySQL 时，用 Docker：

```bash
cd vue-admin-template-server

# 需要先打开 Docker Desktop
docker compose up -d

# 看是否 healthy
docker compose ps
```

默认连接（见 `application.yml`）：

| 项 | 值 |
|----|----|
| host | `127.0.0.1:3306` |
| database | `vue_admin` |
| username | `root` |
| password | `root123` |

### 可视化查看表数据（Adminer）

`docker compose up -d` 后会同时启动网页版数据库管理：

1. 浏览器打开 [http://127.0.0.1:8080](http://127.0.0.1:8080)
2. 登录信息：

| 项 | 值 |
|----|----|
| 系统 | MySQL |
| 服务器 | `mysql`（页面默认即可） |
| 用户名 | `root` |
| 密码 | `root123` |
| 数据库 | `vue_admin` |

3. 点开左侧 `sys_user` 即可表格查看 / 编辑数据

首次启动会自动执行 `sql/init.sql` 建表并写入种子用户。若你改过 `init.sql` 但容器数据已存在，可重建：

```bash
docker compose down -v
docker compose up -d
```

## 2. 启动后端

```bash
cd vue-admin-template-server
mvn spring-boot:run
```

调试模式：

```bash
./start-debug.sh
# 再在 Cursor 里 F5 Attach（端口 5005）
```

验证登录：

```bash
curl -X POST http://127.0.0.1:8090/vue-admin-template/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"111111"}'
```

验证用户列表：

```bash
curl "http://127.0.0.1:8090/vue-admin-template/system/user/list?page=1&limit=10"
```

## 3. 前端联调

确保 `vue-admin-template/vue.config.js` 已代理到 `http://127.0.0.1:8090` 且关闭 mock，然后：

```bash
cd vue-admin-template
npm run dev
```

### 默认账号（密码均为 `111111`）

| 用户名 | 昵称 | 角色 | 状态 |
|--------|------|------|------|
| admin | 超级管理员 | admin | 启用 |
| editor | 运营编辑 | editor | 启用 |
| zhangsan | 张三 | operator | 启用 |
| lisi | 李四 | operator | 禁用 |
| wangwu | 王五 | editor | 启用 |

## 常见问题

**Q: 启动报 `Communications link failure` / 连不上 MySQL？**  
先确认 Docker Desktop 已打开，再执行 `docker compose up -d`，等 `healthy` 后再启后端。

**Q: 前端报 404？**  
检查 proxy 是否去掉 `/dev-api` 前缀，后端路径是 `/vue-admin-template/...`。

**Q: 登录报账号或密码错误？**  
密码是 `111111`（不是任意密码）。禁用用户（如 `lisi`）无法登录。

**Q: code 不是 20000？**  
前端 `request.js` 只认 `code === 20000` 为成功。
