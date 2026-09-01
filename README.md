# vue-admin-template-server

配套 [vue-admin-template](https://github.com/zl14342117/vue-admin-template) 的 **Spring Boot 练手后端**。

Demo 业务代码已清空，仅保留登录鉴权骨架，方便按《练手项目执行清单》逐步新增模块。

## 技术栈

- Java 8
- Spring Boot 2.7
- 无数据库（内存 Mock 数据，适合练手）

## 项目结构

```text
vue-admin-template-server/
├── pom.xml
└── src/main/java/com/demo/admin/
    ├── VueAdminTemplateApplication.java   # 启动类
    ├── config/                            # 跨域等配置
    ├── controller/                        # 接口层（对应前端 api/）
    ├── service/                           # 业务层
    │   └── impl/
    ├── pojo/
    │   ├── dto/                           # 入参
    │   └── vo/                            # 出参
    ├── common/                            # 统一响应、异常处理
    └── exception/
```

和 `dc-portal-server` 的对应关系：

| 本 Demo | dc-portal-server |
|--------|------------------|
| `Controller` | `Controller` |
| `Service` / `ServiceImpl` | `Service` / `ServiceImpl` |
| `dto` / `vo` | `pojo/dto` / `pojo/vo` |
| `ApiResponse` | `RetVo` |

## 已实现接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/vue-admin-template/user/login` | 登录，账号 `admin` / `editor` |
| GET | `/vue-admin-template/user/info?token=xxx` | 获取用户信息 |
| POST | `/vue-admin-template/user/logout` | 登出 |

统一响应格式（与前端 `request.js` 一致）：

```json
{
  "code": 20000,
  "message": "success",
  "data": {}
}
```

## 启动后端

```bash
cd vue-admin-template-server

# 编译并启动（端口 8090）
mvn spring-boot:run
```

验证登录：

```bash
curl -X POST http://127.0.0.1:8090/vue-admin-template/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

## 前端联调

### 1. 关闭前端 Mock

编辑 `vue-admin-template/vue.config.js`，注释掉 mock：

```js
devServer: {
  port: port,
  open: true,
  proxy: {
    '/dev-api': {
      target: 'http://127.0.0.1:8090',
      changeOrigin: true,
      pathRewrite: {
        '^/dev-api': ''
      }
    }
  }
  // before: require('./mock/mock-server.js')  // 注释这行
},
```

### 2. 启动前端

```bash
cd vue-admin-template
npm run dev
```

### 3. 登录测试

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 任意 | 管理员 |
| editor | 任意 | 编辑 |

## 建议练习

按前端仓库 `练手项目执行清单.md` 逐步新增模块，例如：

1. **DashboardController**：工作台统计数据接口
2. **SysUserController**：用户管理 CRUD
3. **CustomerController**：客户管理 CRUD
4. **对照 dc-portal-server**：打开 `LeadTimeReportController`，对比分层写法

## 常见问题

**Q: 前端报 404？**  
检查 proxy 是否去掉 `/dev-api` 前缀，后端路径是 `/vue-admin-template/...` 不是 `/dev-api/...`。

**Q: 登录报 60204？**  
用户名必须是 `admin` 或 `editor`。

**Q: code 不是 20000？**  
前端 `request.js` 只认 `code === 20000` 为成功。
