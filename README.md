# 清新杂志风全栈个人博客（Spring Boot + Next.js）

这是一个前后端分离的个人博客项目骨架，后端使用 Spring Boot 3 + Java 17，前端使用 Next.js 15（App Router）+ TypeScript + Tailwind CSS，整体视觉定位为「清新杂志风」。

## 项目结构

```text
myBlog/
├─ pom.xml                           # 后端 Maven 配置
├─ src/
│  ├─ main/
│  │  ├─ java/com/bookstore/blog/
│  │  │  ├─ BookstoreApplication.java
│  │  │  ├─ common/
│  │  │  │  └─ Result.java
│  │  │  ├─ config/
│  │  │  │  ├─ DataInitializer.java
│  │  │  │  ├─ JwtAuthenticationFilter.java
│  │  │  │  ├─ SecurityConfig.java
│  │  │  │  └─ UserPrincipalService.java
│  │  │  ├─ controller/
│  │  │  │  ├─ AuthController.java
│  │  │  │  └─ PostController.java
│  │  │  ├─ dto/
│  │  │  │  ├─ PostRequest.java
│  │  │  │  └─ PostResponse.java
│  │  │  ├─ entity/
│  │  │  │  ├─ Category.java
│  │  │  │  ├─ Post.java
│  │  │  │  ├─ Tag.java
│  │  │  │  └─ User.java
│  │  │  ├─ repository/
│  │  │  │  ├─ PostRepository.java
│  │  │  │  └─ UserRepository.java
│  │  │  └─ security/
│  │  │     ├─ JwtService.java
│  │  │     └─ LoginRequest.java
│  │  └─ resources/
│  │     └─ application.yml
│  └─ test/
│     └─ java/com/bookstore/blog/
└─ frontend/                         # Next.js 前端应用
   ├─ app/
   │  ├─ globals.css
   │  ├─ layout.tsx
   │  └─ page.tsx
   └─ ...
```

## 技术栈

### 后端
- Java 17
- Spring Boot 3.x
- Spring Web / Spring Security
- Spring Data JPA
- MySQL（本地或 Railway 等托管）
- JWT（登录签发 + 请求鉴权过滤）
- Maven

### 前端
- Next.js 16（App Router）
- React + TypeScript
- Tailwind CSS
- shadcn/ui（后续按组件需求逐步接入）

## 当前已实现能力（骨架阶段）

### 后端
- Spring Boot 启动类 `BookstoreApplication`
- 统一返回体：`Result<T>`
- 实体模型：
  - `User`（用户）
  - `Post`（文章，包含 `@Version` 乐观锁字段）
  - `Category`（分类）
  - `Tag`（标签）
- JWT 认证基础链路：
  - `POST /api/auth/login`：用户名密码登录，签发 token
  - `JwtAuthenticationFilter`：解析并校验 `Authorization: Bearer <token>`
  - `SecurityConfig`：放行登录、H2、文章 GET 接口，其余接口需认证
- 文章接口：`/api/posts`
  - `GET /api/posts`：返回文章列表
  - `GET /api/posts/{id}`：按 id 获取文章
  - `POST /api/posts`：创建文章（需认证）
  - `PUT /api/posts/{id}`：更新文章（需认证）
  - `DELETE /api/posts/{id}`：删除文章（需认证）
- 全局异常处理：`@RestControllerAdvice`
- 初始化数据：启动后自动创建测试账号与示例文章

### 前端
- Next.js + Tailwind 脚手架初始化
- shadcn/ui 初始化完成（可按需继续安装组件）
- 清新杂志风基础主题（`globals.css`）
  - 背景色 `#f9f8f6`
  - 主文字色 `#2d2d2d`
  - 标题衬线、正文无衬线
- 基础全局布局 `layout.tsx`
- 首页基础区块（导航栏、Hero、文章卡片）
- 首页已对接后端 `GET /api/posts`（无数据时有空态提示）
- 新增简易管理页：`/admin`（登录、发文、删文）

## 快速开始

## 1) 启动后端

```bash
# 在项目根目录（首次无管理员时须配置密码，见下方「管理员初始化」）
mvn spring-boot:run

# 或本地图省事：使用 dev 配置中的仅开发用默认密码（勿用于生产）
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```

默认地址：
- API: `http://localhost:8080`

启动前请确保 MySQL 已运行，并已创建库名与 `application.yml` 中一致（默认库名 `myblog`，可用环境变量 `MYSQLDATABASE` 覆盖）。Railway 上关联 MySQL 插件后会自动注入 `MYSQLHOST`、`MYSQLPORT`、`MYSQLDATABASE`、`MYSQLUSER`、`MYSQLPASSWORD`；也可单独设置 `SPRING_DATASOURCE_URL` 为完整 JDBC 地址以覆盖默认拼接方式。

## 2) 启动前端

```bash
cd frontend
npm install
npm run dev
```

默认地址：
- Web: `http://localhost:3000`

部署在 Vercel 时，前端默认在 **生产构建** 下请求后端  
`https://my-block-backend-production.up.railway.app`（见 `frontend/lib/api.ts`）。  
若需换地址，在 Vercel 环境变量中设置 `NEXT_PUBLIC_API_BASE_URL` 即可覆盖。

## 环境配置

后端配置在 `src/main/resources/application.yml`，包含：
- MySQL 数据源（支持 `SPRING_DATASOURCE_*` 与 Railway 的 `MYSQL*` 环境变量）
- JPA 设置
- JWT 密钥、过期时间

### 管理员初始化

- 默认管理员用户名为 **`caixukun`**，初始密码见 `application.yml` 中 `blog.admin`（个人自用；公开仓库请用环境变量 **`BLOG_ADMIN_USERNAME` / `BLOG_ADMIN_INITIAL_PASSWORD`** 覆盖）。
- 若数据库里**只有旧用户 `admin`**，应用启动时会自动将其**改名为当前配置的用户名**并重置为上述初始密码。
- **本地**：也可使用 `--spring.profiles.active=dev`（`application-dev.yml` 与主配置默认一致）。

> 若仓库会公开，请勿把真实密码写进 YAML，改用 Railway / 本机环境变量。

## 前后端联调说明

### 建议联调顺序
1. 启动后端，确认 `/api/auth/login` 可用（默认管理员见 `application.yml` 的 `blog.admin`）。
2. 在 `/admin` 使用你自己的管理员账号密码登录（页面不再预填凭据）。
3. 前端调用登录接口获取 token。
4. 将 token 存储在前端（建议 HttpOnly Cookie 或受控存储策略）。
5. 访问受保护接口时带上 `Authorization: Bearer <token>`。
6. 若 token 过期，跳转登录并重新签发。

### 快速手工联调（最简单路径）
1. 打开 `http://localhost:3000/admin`。
2. 使用你在后端配置的管理员账号与密码登录。
3. 在管理页发布一篇文章。
4. 回到首页确认文章卡片已更新。

### 接口返回约定
统一返回 `Result<T>`：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

建议：
- `code = 0` 表示成功
- 非 0 表示业务失败
- HTTP 状态码表达协议级语义（200/401/403/500 等）

## 项目路线图（Roadmap）

### Phase 1 - 骨架与基础能力（当前）
- [x] 前后端项目初始化
- [x] 统一返回体
- [x] JWT 登录与鉴权过滤基础链路
- [x] 核心实体搭建
- [x] 杂志风首页基础布局

### Phase 2 - 业务功能完善
- [ ] 用户注册、密码加密、刷新令牌
- [x] 文章 CRUD（基础版）
- [ ] 分类/标签管理与关联查询
- [ ] 分页、搜索、归档接口
- [x] 接口参数校验与全局异常处理（基础版）

### Phase 3 - 前端体验升级
- [x] 接入 shadcn/ui 组件体系（初始化）
- [ ] 文章列表页、详情页、分类页
- [ ] Markdown 渲染与代码高亮
- [ ] 响应式优化与暗色模式方案
- [ ] SEO（metadata、sitemap、RSS）

### Phase 4 - 工程化与部署
- [ ] 测试（后端单元/集成，前端组件/E2E）
- [ ] CI/CD 与质量门禁
- [x] MySQL 生产库（Railway 等）
- [ ] PostgreSQL 备选 / 迁移脚本
- [ ] Docker 化部署与反向代理
- [ ] 日志、监控与告警

## 注意事项

- 当前前端依赖版本要求 Node.js `>=20.9`，本机若低于此版本会有 engine warning，建议尽快升级 Node。
- Maven 编译使用的是 JDK 版本由 `JAVA_HOME` 决定，若不是 17 会导致后端无法编译。
- 当前后端已具备基础联调能力，下一步建议补充注册、刷新令牌、文章分页与标签分类筛选。
- 为便于本地联调，后端已放开 `http://localhost:3000` 的 CORS。
