# 四川国驰恒创科技有限公司 - 企业官网系统

## 项目简介

四川国驰恒创科技有限公司（GCHC）企业官网及后台管理系统。核心技术源自美国辛辛那提大学博士团队，专注于提供新能源电池快充技术解决方案。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 3.2 + Java 17 + MySQL 8.0 |
| 前端（官网） | Vue 3 + Vite + Element Plus |
| 前端（管理后台） | Vue 3 + Vite + Element Plus + ECharts |
| 安全 | Spring Security + JWT |
| 构建 | Maven（后端）、Vite（前端） |

## 项目结构

```
yayoi/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/       # Java 源码
│   ├── src/main/resources/  # 配置文件
│   ├── target/              # 构建输出（JAR）
│   └── uploads/             # 上传文件存储
├── frontend/
│   ├── company-site/        # 公司官网前端（端口 5173）
│   └── admin-panel/         # 管理后台前端（端口 5174）
├── database/                # 数据库 SQL 文件
├── START.bat                # 一键启动脚本（本地使用）
└── .gitignore
```

## 快速启动

### 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0

### 启动步骤

1. 创建数据库 `gchc_db`，导入 `database/gchc_db.sql`
2. 修改 `backend/src/main/resources/application.yml` 中的数据库连接信息
3. 运行 `START.bat` 一键启动所有服务，或手动启动：

```bash
# 启动后端
cd backend
mvn clean package -DskipTests
java -jar target/gchc-backend-1.0.0.jar

# 启动官网前端
cd frontend/company-site
npm install
npm run dev

# 启动管理后台
cd frontend/admin-panel
npm install
npm run dev
```

### 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 公司官网 | http://localhost:5173 | 对外展示 |
| 管理后台 | http://localhost:5174 | 内容管理 |
| 后端 API | http://localhost:8080 | REST API |

### 默认账号

- 用户名：`admin`
- 密码：`admin123`

## 功能模块

### 公司官网

- 首页：轮播图、核心优势、公司简介、XRE亮点、最新动态、热门产品
- 关于我们：公司介绍、核心价值观、核心团队、发展历程、所获荣誉
- 产品服务：产品分类、产品详情
- 新闻动态：新闻列表、文章详情
- 联系我们：在线留言

### 管理后台

- 仪表盘：数据统计概览
- 首页管理：轮播图、核心优势、公司简介、XRE亮点、最新动态、热门产品、立即行动
- 关于我们：Hero横幅、公司简介、核心价值观、核心团队、发展历程、所获荣誉
- 产品服务：页面文案、分类管理、产品列表
- 新闻动态：文章管理
- 留言管理：用户留言查看与回复
- 网站设置：页脚信息、全局设置
- 用户管理：管理员账号管理

## 开发说明

- 后端使用 Spring Security + JWT 进行认证，管理接口需要 `ADMIN` 角色
- 前端通过 Vite 代理将 `/api` 请求转发到后端 8080 端口
- 上传文件存储在 `backend/uploads/` 目录，通过 `/uploads/` 路径访问
