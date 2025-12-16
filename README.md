# 🌸 Flower Mall | 基于 DDD 架构的鲜花商城系统
> 👨‍💻 **开发者**：蓝家俊 &nbsp;|&nbsp;  🎓 **学号**：202330450841
>
> 一个采用 **领域驱动设计 (DDD)** 架构开发的全栈电商系统。
>
> 集成了 **JWT 鉴权**、**RBAC 权限控制**、**异步邮件通知** 以及 **Docker 云原生部署** 等企业级特性。前端采用 **Vue 3 + Vite**，设计了简单苹果风格 UI 交互体验。
---

## ✨ 项目亮点 (Highlights)

*   🏗️ **标准 DDD 分层架构**：拒绝 MVC 面条代码，采用 `Api` - `App` - `Domain` - `Infra` - `Trigger` 的严谨分层，业务逻辑纯粹且高内聚。
*   🛡️ **企业级安全闭环**：
    *   手写 **AOP 切面** + 自定义注解 `@AdminOnly` 实现权限控制。
    *   **防越权 (IDOR)** 设计：在敏感操作中强制使用 Token 解析的用户 ID，而非前端传参。
    *   **无状态认证**：基于 JWT + ThreadLocal 实现用户上下文透传。
*   🎨 **极致前端体验**：
    *   基于 **Vue 3 + Element Plus**，自定义 CSS 实现 **毛玻璃 (Frosted Glass)**、大圆角、丝滑悬浮等 **Apple Style** 视觉效果。
    *   完善的 **路由守卫 (Router Guard)**，实现基于角色的页面访问控制。
*   🚀 **DevOps 云原生**：
    *   **Docker Compose** 一键编排 MySQL、后端、Nginx。
    *   **Nginx 反向代理**：配置动静分离与跨域转发，生产环境级配置。
    *   **网络复用**：容器接入服务器现有网络，实现资源最大化利用。
*   📧 **完整业务流**：
    *   集成 `JavaMailSender`，实现下单后 **异步发送** 确认邮件（解决云服务器 25 端口封禁问题）。
    *   实现购物车 **内存与数据库状态同步** 的原子性操作。
 
## 🛠️ 技术栈 (Tech Stack)

### 后端 (Backend)
*   **核心框架**: Spring Boot 2.x
*   **架构模式**: DDD (Domain-Driven Design)
*   **ORM 框架**: MyBatis + MySQL
*   **权限安全**: JWT (JSON Web Token) + AOP + ThreadLocal
*   **工具库**: Guava, Lombok, Fastjson
*   **中间件**: Java Mail (SMTP/SSL)

### 前端 (Frontend)
*   **核心框架**: Vue 3 (Composition API)
*   **构建工具**: Vite 5
*   **UI 组件库**: Element Plus (Customized)
*   **路由管理**: Vue Router 4
*   **网络请求**: Axios (Interceptors Encapsulated)

### 部署 (Deployment)
*   **容器化**: Docker & Docker Compose
*   **网关/代理**: Nginx
*   **数据库**: MySQL 8.0
---

## 🏗️ 系统架构 (Architecture)

项目遵循严格的 DDD 四层架构：（简单架构设计展示，实际上预留了六边形结构）

```text
flower-mall
├── flower-mall-api           # 【用户接口层】 DTO, Response 定义
├── flower-mall-app           # 【应用层】 全局配置, 异常处理, 启动入口
├── flower-mall-domain        # 【领域层】 核心业务逻辑 (Model, Service, Factory)
│   ├── cart                  # 购物车领域
│   ├── flower                # 商品领域
│   ├── order                 # 订单领域
│   └── user                  # 用户领域
├── flower-mall-infrastructure # 【基础设施层】 数据库交互 (DAO, PO, RepositoryImpl), Redis, Utils
├── flower-mall-trigger       # 【触发器层】 Controller, AOP, Interceptor, Job
└── flower-mall-web           # 【前端工程】 Vue3 + Vite
```

---
## 📦 功能列表 (Features)（包括但不限于以下功能）

### 👤 顾客端 (Customer)
- [x] **账户体系**：注册、登录、Token 自动续期。
- [x] **商品浏览**：瀑布流展示，支持毛玻璃卡片交互。
- [x] **购物车**：实时金额计算、数量增减、一键清空。
- [x] **下单流程**：收货地址填写、**异步邮件通知**。
- [x] **我的订单**：查看历史订单状态（待发货/已发货）。

### 👨‍💻 管理端 (Admin)
- [x] **权限隔离**：通过 `@AdminOnly` 切面保护敏感接口。
- [x] **商品管理**：上架新花材、修改价格库存、下架商品。
- [x] **分类管理**：商品品类维护。
- [x] **订单管理**：查看所有用户订单、**一键发货**。
- [x] **数据看板**：实时统计销售额与订单量。

---
## 📄 License

MIT License © 2025 Flower Mall Contributors
