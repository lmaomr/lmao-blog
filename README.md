# 个人博客系统

这是一个使用Vue 3和Spring Boot构建的个人博客系统。

## 技术栈

### 前端

- Vue 3
- Vue Router
- Pinia
- Element Plus
- Axios

### 后端

- Spring Boot
- Spring Security
- MyBatis Plus
- MySQL
- JWT

## 功能特性

- 用户认证和授权
- 文章的CRUD操作
- 评论系统
- 文章分类和标签
- 响应式设计

## 项目结构

```
blog/
├── blog-frontend/          # Vue前端项目
│   ├── src/
│   │   ├── assets/        # 静态资源
│   │   ├── components/    # 组件
│   │   ├── views/         # 页面
│   │   ├── router/        # 路由配置
│   │   ├── store/         # 状态管理
│   │   └── api/           # API请求
│   └── package.json
│
└── blog-backend/          # Spring Boot后端项目
    ├── src/
    │   ├── main/
    │   │   ├── java/     # Java源代码
    │   │   └── resources/# 配置文件
    │   └── test/         # 测试代码
    └── pom.xml
```

## 开发环境要求

- Node.js >= 16
- Java >= 17
- MySQL >= 8.0
- Maven >= 3.6

## 快速开始

### 前端开发

```bash
cd blog-frontend
npm install
npm run dev
```

### 后端开发

```bash
cd blog-backend
mvn spring-boot:run
```

## 数据库配置

1. 创建MySQL数据库
2. 修改`application.properties`中的数据库配置
3. 运行数据库迁移脚本

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

MIT 