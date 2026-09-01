# vue-admin-template-server

配套 [vue-admin-template](https://github.com/PanJiaChen/vue-admin-template) 的 **Spring Boot 学习用后端**，接口契约与前端 `mock/` 目录保持一致，方便你从 mock 切换到真实后端。

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
| GET | `/vue-admin-template/table/list` | 表格列表（30 条模拟数据） |

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

验证：

```bash
curl http://127.0.0.1:8090/vue-admin-template/table/list
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

1. **加接口**：在 `TableController` 新增 `GET /detail/{id}` 返回单条数据
2. **接入 H2/MySQL**：把 `TableServiceImpl` 的内存数据改成数据库查询
3. **加参数校验**：给 `table/list` 加分页参数 `page`、`limit`
4. **对照 dc-portal-server**：打开 `LeadTimeReportController`，对比分层写法

## 常见问题

**Q: 前端报 404？**  
检查 proxy 是否去掉 `/dev-api` 前缀，后端路径是 `/vue-admin-template/...` 不是 `/dev-api/...`。

**Q: 登录报 60204？**  
用户名必须是 `admin` 或 `editor`（与 mock 一致）。

**Q: code 不是 20000？**  
前端 `request.js` 只认 `code === 20000` 为成功。
