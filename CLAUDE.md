# Blog - 个人博客项目

## 技术栈
- 后端: Spring Boot 3.4 + JDK 21 + Maven 3.9.4
- 前端: Vue 3 + Vite 8 + Element Plus + Pinia + Vue Router
- 数据库: MySQL 9.5
- 认证: JWT (jjwt 0.12.6)
- 端口: 后端 8080 / 前端 5173

## 项目结构
```
blog/
├── backend/          # Spring Boot 后端
│   └── src/main/java/com/blog/
│       ├── entity/       # User, Article, Tag
│       ├── repository/   # JPA 数据访问
│       ├── service/      # AuthService, ArticleService, TagService
│       ├── controller/   # AuthController, ArticleController, TagController, AdminController
│       ├── security/     # JwtProvider, JwtAuthFilter, SecurityConfig
│       ├── common/       # Result, GlobalExceptionHandler
│       └── dto/          # AuthDTO, ArticleDTO
├── frontend/         # Vue 3 前端
│   └── src/
│       ├── views/        # Home, ArticleDetail, Login, Register
│       │   └── admin/    # Layout, Articles, ArticleEditor, Tags
│       ├── api/          # request.js, auth.js, article.js, tag.js
│       ├── router/       # 路由配置
│       └── stores/       # auth store (Pinia)
```

## 功能状态
- [x] 用户注册/登录 (JWT)
- [x] 文章 CRUD + Markdown
- [x] 标签管理
- [x] 后台管理 (文章列表/编辑/标签管理)
- [ ] 待开发: 评论区、访问统计、图片上传、部署

## API 接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/auth/register | 注册 | 否 |
| POST | /api/auth/login | 登录 | 否 |
| GET | /api/articles | 已发布文章列表 | 否 |
| GET | /api/articles/{id} | 文章详情 | 否 |
| GET | /api/tags | 标签列表 | 否 |
| GET | /api/admin/articles | 所有文章(含草稿) | 是 |
| POST | /api/admin/articles | 创建文章 | 是 |
| PUT | /api/admin/articles/{id} | 更新文章 | 是 |
| DELETE | /api/admin/articles/{id} | 删除文章 | 是 |
| POST | /api/admin/tags | 创建标签 | 是 |
| DELETE | /api/admin/tags/{id} | 删除标签 | 是 |

## 启动方式
```bash
# 后端
cd backend && mvn spring-boot:run

# 前端
cd frontend && npm run dev
```

## 数据库
- 数据库名: blog (JPA 自动建表)
- application.yml 中可配置数据库密码

## GitHub
- https://github.com/wazma3/blog
- Git 代理: http://127.0.0.1:7890 (VPN)

## 项目进度
- 一期核心功能已完成，可正常使用
- 后续可继续开发评论区、访问统计、图片上传、部署上线等功能
